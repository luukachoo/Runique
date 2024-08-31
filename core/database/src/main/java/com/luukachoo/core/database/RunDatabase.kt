package com.luukachoo.core.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import com.luukachoo.core.database.dao.RunDao
import com.luukachoo.core.database.entity.RunEntity

@Database(
    entities = [RunEntity::class],
    version = 1
)
abstract class RunDatabase : RoomDatabase() {

    abstract val runDao: RunDao
}