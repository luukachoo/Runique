package com.luukachoo.run.presentation.run_overview

sealed interface RunOverviewAction {
    data object OnStartClick : RunOverviewAction
    data object OnLogOutClick : RunOverviewAction
    data object OnAnalyticsClick : RunOverviewAction
}