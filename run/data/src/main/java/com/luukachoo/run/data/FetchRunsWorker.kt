package com.luukachoo.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.luukachoo.core.domain.run.RunRepository
import com.luukachoo.core.domain.util.Result

class FetchRunsWorker(
    context: Context,
    params: WorkerParameters,
    private val runRepository: RunRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) Result.failure()
        return when (val result = runRepository.fetchRuns()) {
            is com.luukachoo.core.domain.util.Result.Error -> result.error.toWorkerResult()
            is com.luukachoo.core.domain.util.Result.Success -> Result.success()
        }
    }
}