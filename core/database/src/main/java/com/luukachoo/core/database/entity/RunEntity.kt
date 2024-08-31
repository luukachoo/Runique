package com.luukachoo.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bson.types.ObjectId

@Entity
data class RunEntity(
    val durationMillis: Long,
    val dataTimeUTC: String,
    val distanceMeters: Int,
    val latitude: Double,
    val longitude: Double,
    val maxSpeedKmh: Double,
    val averageSpeedKmh: Double,
    val totalElevationMeters: Int,
    val mapPictureUrl: String?,
    @PrimaryKey(autoGenerate = false)
    val id: String = ObjectId().toHexString()
)
