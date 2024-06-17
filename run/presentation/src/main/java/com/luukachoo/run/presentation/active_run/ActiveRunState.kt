package com.luukachoo.run.presentation.active_run

import com.luukachoo.core.domain.location.Location
import com.luukachoo.run.domain.RunData
import kotlin.time.Duration

data class ActiveRunState(
    val runData: RunData = RunData(),
    val elapsedTime: Duration = Duration.ZERO,
    val shouldTrack: Boolean = false,
    val hasStartedRunning: Boolean = false,
    val currentLocation: Location? = null,
    val isRunFinished: Boolean = false,
    val isSavingRun: Boolean = false,
)
