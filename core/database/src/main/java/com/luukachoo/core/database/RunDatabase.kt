package com.luukachoo.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luukachoo.core.database.dao.RunDao
import com.luukachoo.core.database.dao.RunPendingSyncDao
import com.luukachoo.core.database.entity.DeletedRunSyncEntity
import com.luukachoo.core.database.entity.RunEntity
import com.luukachoo.core.database.entity.RunPendingSyncEntity

@Database(
    entities = [
        RunEntity::class,
        RunPendingSyncEntity::class,
        DeletedRunSyncEntity::class
    ],
    version = 1
)
abstract class RunDatabase : RoomDatabase() {

    abstract val runDao: RunDao
    abstract val runPendingSyncDao: RunPendingSyncDao
}