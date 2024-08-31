package com.luukachoo.run.network.mappers

import com.luukachoo.core.domain.location.Location
import com.luukachoo.core.domain.run.Run
import com.luukachoo.run.network.RunDto
import com.luukachoo.run.network.util.CreateRunRequest
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunDto.toRunModel() = Run(
    id = id,
    dataTimeUTC = Instant.parse(dataTimeUtc).atZone(ZoneId.of("UTC")),
    duration = durationMillis.milliseconds,
    distanceMeters = distanceMeters,
    location = Location(
        latitude = lat,
        longitude = long
    ),
    maxSpeedKmh = maxSpeedKmh,
    totalElevationMeters = totalElevationMeters,
    mapPictureUrl = mapPictureUrl
)

fun Run.toCreateRunRequest() = CreateRunRequest(
    id = id!!,
    durationMillis = duration.inWholeMilliseconds,
    distanceMeters = distanceMeters,
    lat = location.latitude,
    long = location.longitude,
    avgSpeedKmh = averageSpeedInKmh,
    maxSpeedKmh = maxSpeedKmh,
    totalElevationMeters = totalElevationMeters,
    epochMillis = dataTimeUTC.toEpochSecond() * 1000L
)