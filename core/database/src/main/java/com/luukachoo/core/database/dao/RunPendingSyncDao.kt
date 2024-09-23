package com.luukachoo.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.luukachoo.core.database.entity.DeletedRunSyncEntity
import com.luukachoo.core.database.entity.RunPendingSyncEntity

@Dao
interface RunPendingSyncDao {

    // Created Runs

    @Query("SELECT * FROM RUNPENDINGSYNCENTITY WHERE userId=:userId")
    suspend fun getAllRunPendingSyncEntities(userId: String): List<RunPendingSyncEntity>

    @Query("SELECT * FROM RUNPENDINGSYNCENTITY WHERE runId=:runId")
    suspend fun getRunPendingSyncEntity(runId: String): RunPendingSyncEntity?

    @Upsert
    suspend fun upsertRunPendingSyncEntity(entity: RunPendingSyncEntity)

    @Query("DELETE FROM RUNPENDINGSYNCENTITY WHERE runId=:runId")
    suspend fun deleteRunPendingSyncEntity(runId: String)

    // Deleted Runs

    @Query("SELECT * FROM DELETEDRUNSYNCENTITY WHERE userId=:userId")
    suspend fun getAllDeletedRunSyncEntities(userId: String): List<DeletedRunSyncEntity>

    @Upsert
    suspend fun upsertDeletedRunSyncEntity(entity: DeletedRunSyncEntity)

    @Query("DELETE FROM DELETEDRUNSYNCENTITY WHERE runId=:runId")
    suspend fun deleteDeletedRunSyncEntity(runId: String)
}