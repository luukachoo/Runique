package com.luukachoo.run.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.luukachoo.core.database.dao.RunPendingSyncDao
import com.luukachoo.core.database.entity.RunPendingSyncEntity
import com.luukachoo.core.database.mappers.toRunEntity
import com.luukachoo.core.domain.SessionStorage
import com.luukachoo.core.domain.run.Run
import com.luukachoo.run.domain.SyncRunScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncRunWorkerScheduler(
    private val context: Context,
    private val pendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage
) : SyncRunScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(syncType: SyncRunScheduler.SyncType) {
         when (syncType) {
            is SyncRunScheduler.SyncType.CreateRun -> scheduleCreateRunWorker(syncType.run, syncType.mapPictureBytes)
            is SyncRunScheduler.SyncType.DeleteRun -> TODO()
            is SyncRunScheduler.SyncType.FetchRuns -> scheduleFetchRunWorker(syncType.interval)
        }
    }

    override suspend fun cancelAllSyncs() {
        WorkManager.getInstance(context)
            .cancelAllWork()
            .await()
    }

    private suspend fun scheduleFetchRunWorker(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager
                .getWorkInfosByTag("sync_work")
                .get()
                .isNotEmpty()
        }

        if (isSyncScheduled) return

        val workRequest = PeriodicWorkRequestBuilder<FetchRunsWorker>(
            repeatInterval = interval.toJavaDuration()
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInitialDelay(
                duration = 30,
                timeUnit = TimeUnit.MINUTES
            )
            .addTag("sync_work")
            .build()

        workManager.enqueue(workRequest).await()
    }

    private suspend fun scheduleCreateRunWorker(run: Run, mapPictureBytes: ByteArray) {
        val userId = sessionStorage.get()?.userId ?: return
        val pendingRun = RunPendingSyncEntity(
            run = run.toRunEntity(),
            userId = userId,
            mapPictureBytes = mapPictureBytes
        )
        pendingSyncDao.upsertRunPendingSyncEntity(pendingRun)
    }
}