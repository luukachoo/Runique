package com.luukachoo.core.data.run

import com.luukachoo.core.domain.run.LocalRunDataSource
import com.luukachoo.core.domain.run.RemoteRunDataSource
import com.luukachoo.core.domain.run.Run
import com.luukachoo.core.domain.run.RunId
import com.luukachoo.core.domain.run.RunRepository
import com.luukachoo.core.domain.util.DataError
import com.luukachoo.core.domain.util.EmptyResult
import com.luukachoo.core.domain.util.Result
import com.luukachoo.core.domain.util.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class OfflineFirstRunRepository(
    private val localRunDataSource: LocalRunDataSource,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val applicationScope: CoroutineScope
) : RunRepository {
    override fun getRuns(): Flow<List<Run>> = localRunDataSource.getRuns()

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteRunDataSource.getRuns()) {
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRuns(result.data).asEmptyDataResult()
                }.await()
            }

            is Result.Error -> result.asEmptyDataResult()
        }
    }

    override suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyResult<DataError> {
        val localResult = localRunDataSource.upsertRun(run)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }

        val runWithId = run.copy(id = localResult.data)
        return when (
            val remoteResult = remoteRunDataSource.postRun(runWithId, mapPicture)
        ) {
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRun(remoteResult.data).asEmptyDataResult()
                }.await()
            }

            is Result.Error -> Result.Success(Unit)
        }
    }

    override suspend fun deleteRun(id: RunId) {
        localRunDataSource.deleteRunById(id = id)
        val remoteResult = applicationScope.async {
            remoteRunDataSource.deleteRun(id)
        }.await()
    }
}