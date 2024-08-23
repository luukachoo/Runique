package com.luukachoo.run.presentation.run_overview.mappers

import com.luukachoo.core.domain.run.Run
import com.luukachoo.core.presentation.ui.formatted
import com.luukachoo.core.presentation.ui.toFormattedKm
import com.luukachoo.core.presentation.ui.toFormattedKmh
import com.luukachoo.core.presentation.ui.toFormattedMeters
import com.luukachoo.core.presentation.ui.toFormattedPace
import com.luukachoo.run.presentation.run_overview.model.RunUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUiModel(): RunUi {
    val dateTimeLInLocalTime = dataTimeUTC.withZoneSameInstant(ZoneId.systemDefault())
    val formattedDateTime = dateTimeLInLocalTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mma"))
    val distanceInKm = distanceMeters / 1000.0

    return RunUi(
        id = id!!,
        duration = duration.formatted(),
        dateTime = formattedDateTime,
        distance = distanceInKm.toFormattedKm(),
        averageSpeed = averageSpeedInKmh.toFormattedKmh(),
        maxSpeed = maxSpeedKmh.toFormattedKmh(),
        pace = duration.toFormattedPace(distanceInKm),
        totalElevation = totalElevationMeters.toFormattedMeters(),
        mapPictureUrl = mapPictureUrl
    )
}