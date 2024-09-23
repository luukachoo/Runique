package com.luukachoo.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.luukachoo.core.database.dao.RunPendingSyncDao
import com.luukachoo.core.database.mappers.toRunModel
import com.luukachoo.core.domain.run.RemoteRunDataSource

class CreateRunWorker(
    context: Context,
    private val params: WorkerParameters,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val pendingSyncDao: RunPendingSyncDao
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) Result.failure()
        val pendingRunId = params.inputData.getString(RUN_ID) ?: return Result.failure()
        val pendingRunEntity = pendingSyncDao.getRunPendingSyncEntity(pendingRunId) ?: return Result.failure()
        val run = pendingRunEntity.run.toRunModel()

        return when (val result =
            remoteRunDataSource.postRun(run, pendingRunEntity.mapPictureBytes)) {
            is com.luukachoo.core.domain.util.Result.Error -> result.error.toWorkerResult()
            is com.luukachoo.core.domain.util.Result.Success -> {
                pendingSyncDao.deleteRunPendingSyncEntity(pendingRunId)
                Result.success()
            }
        }
    }

    companion object {
        const val RUN_ID = "RUN_ID"
    }
}