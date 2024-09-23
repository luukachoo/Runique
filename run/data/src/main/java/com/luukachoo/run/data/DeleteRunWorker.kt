package com.luukachoo.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.luukachoo.core.database.dao.RunPendingSyncDao
import com.luukachoo.core.domain.run.RemoteRunDataSource
import com.luukachoo.core.domain.util.Result

class DeleteRunWorker(
    context: Context,
    private val params: WorkerParameters,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val pendingSyncDao: RunPendingSyncDao
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) Result.failure()
        val runId = params.inputData.getString(RUN_ID) ?: return Result.failure()

        return when(val result = remoteRunDataSource.deleteRun(runId)) {
            is com.luukachoo.core.domain.util.Result.Error -> result.error.toWorkerResult()
            is com.luukachoo.core.domain.util.Result.Success -> {
                pendingSyncDao.deleteRunPendingSyncEntity(runId)
                Result.success()
            }
        }
    }

    companion object {
        const val RUN_ID = "RUN_ID"
    }
}