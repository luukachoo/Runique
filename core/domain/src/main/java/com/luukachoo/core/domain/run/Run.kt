package com.luukachoo.core.domain.run

import com.luukachoo.core.domain.location.Location
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit

data class Run(
    val id: String?, // This will be null, if we'll start a new run.
    val duration: Duration,
    val dataTimeUTC: ZonedDateTime,
    val distanceMeters: Int,
    val location: Location,
    val maxSpeedKmh: Double,
    val totalElevationMeters: Int,
    val mapPictureUrl: String?
) {
    val averageSpeedInKmh: Double
        get() = (distanceMeters / 1000.0) / duration.toDouble(DurationUnit.HOURS)
}
