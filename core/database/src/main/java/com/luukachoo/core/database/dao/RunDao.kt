package com.luukachoo.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.luukachoo.core.database.entity.RunEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {
    @Upsert
    suspend fun upsertRun(runEntity: RunEntity)

    @Upsert
    suspend fun upsertRuns(runEntities: List<RunEntity>)

    @Query("SELECT * FROM runentity ORDER BY dataTimeUTC DESC")
    fun getRuns(): Flow<List<RunEntity>>

    @Query("DELETE FROM runentity WHERE id = :id")
    fun deleteRunById(id: String)

    @Query("DELETE FROM runentity")
    fun deleteAllRuns()
}