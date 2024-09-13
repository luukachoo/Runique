package com.luukachoo.core.database.mappers

import com.luukachoo.core.database.entity.RunEntity
import com.luukachoo.core.domain.location.Location
import com.luukachoo.core.domain.run.Run
import org.bson.types.ObjectId
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunEntity.toRunModel() = Run(
    id = id,
    duration = durationMillis.milliseconds,
    dateTimeUTC = Instant.parse(dataTimeUTC).atZone(ZoneId.of("UTC")),
    distanceMeters = distanceMeters,
    location = Location(
        latitude = latitude,
        longitude = longitude
    ),
    maxSpeedKmh = maxSpeedKmh,
    totalElevationMeters = totalElevationMeters,
    mapPictureUrl = mapPictureUrl
)

fun Run.toRunEntity() = RunEntity(
    id = id ?: ObjectId().toHexString(),
    durationMillis = duration.inWholeMilliseconds,
    dataTimeUTC = dateTimeUTC.toInstant().toString(),
    maxSpeedKmh = maxSpeedKmh,
    averageSpeedKmh = averageSpeedInKmh,
    distanceMeters = distanceMeters,
    latitude = location.latitude,
    longitude = location.longitude,
    totalElevationMeters = totalElevationMeters,
    mapPictureUrl = mapPictureUrl
)