package com.luukachoo.core.database

import android.database.sqlite.SQLiteFullException
import com.luukachoo.core.database.dao.RunDao
import com.luukachoo.core.database.mappers.toRunEntity
import com.luukachoo.core.database.mappers.toRunModel
import com.luukachoo.core.domain.run.LocalRunDataSource
import com.luukachoo.core.domain.run.Run
import com.luukachoo.core.domain.run.RunId
import com.luukachoo.core.domain.util.DataError
import com.luukachoo.core.domain.util.Result
import com.luukachoo.core.database.util.runCachingError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalRunDataSource(
    private val runDao: RunDao
) : LocalRunDataSource {
    override fun getRuns(): Flow<List<Run>> {
        return runDao.getRuns()
            .map { entities ->
                entities.map { it.toRunModel() }
            }
    }

    override suspend fun upsertRun(run: Run): Result<RunId, DataError.Local> {
        return try {
            val entity = run.toRunEntity()
            runDao.upsertRun(entity)
            Result.Success(entity.id)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertRuns(runs: List<Run>): Result<List<RunId>, DataError.Local> {
        return runCachingError {
            runDao.upsertRuns(runs.map { it.toRunEntity() })
            runs.map { it.id!! }
        }
    }

    override suspend fun deleteRunById(id: String) = runDao.deleteRunById(id)


    override suspend fun deleteAllRuns() = runDao.deleteAllRuns()

}