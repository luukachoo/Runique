package com.luukachoo.run.location

import android.location.Location
import com.luukachoo.core.domain.location.LocationWithAltitude

fun Location.toLocationWithAltitude(): LocationWithAltitude = LocationWithAltitude(
    location = com.luukachoo.core.domain.location.Location(
        latitude = latitude, longitude = longitude
    ),
    altitude = altitude
)