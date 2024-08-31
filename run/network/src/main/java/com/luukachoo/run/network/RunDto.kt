package com.luukachoo.run.network

import kotlinx.serialization.Serializable

@Serializable
data class RunDto(
    val id: String,
    val dataTimeUtc: String,
    val durationMillis: Long,
    val distanceMeters: Int,
    val lat: Double,
    val long: Double,
    val avgSpeedKmh: Double,
    val maxSpeedKmh: Double,
    val totalElevationMeters: Int,
    val mapPictureUrl: String?
)