package com.luukachoo.run.presentation.active_run

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class ActiveRunViewModel : ViewModel() {

    var state by mutableStateOf(ActiveRunState())
        private set

    private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _hasNotificationPermission = MutableStateFlow(false)

    fun onAction(activeRunAction: ActiveRunAction) {
        when(activeRunAction) {
            ActiveRunAction.OnFinishRunClick -> {}
            ActiveRunAction.OnResumeRunClick -> {}
            ActiveRunAction.OnToggleRunClick -> {}
            is ActiveRunAction.SubmitLocationPermissionInfo -> {
                _hasNotificationPermission.value = activeRunAction.acceptedLocationPermission
                state = state.copy(showLocationRationale = activeRunAction.showLocationRationale)
            }
            is ActiveRunAction.SubmitNotificationPermissionInfo -> {
                state = state.copy(showNotificationRationale = activeRunAction.showNotificationRationale)
            }

            ActiveRunAction.DismissRationaleDialog -> {
                state = state.copy(showNotificationRationale = false, showLocationRationale = false)
            }
            else -> Unit
        }
    }

}