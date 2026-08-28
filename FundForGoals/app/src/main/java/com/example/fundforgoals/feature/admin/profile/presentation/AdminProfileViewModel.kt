package com.example.fundforgoals.feature.admin.profile.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AdminProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdminProfileUiState(
            isLoading = false,
            adminName = "Administrator",
            appearanceLabel = "Dark",
            notificationsLabel = "On"
        )
    )
    val uiState: StateFlow<AdminProfileUiState> = _uiState.asStateFlow()

    fun onAction(action: AdminProfileAction) {
        when (action) {
            AdminProfileAction.OnAppearanceClick -> {
                _uiState.update { current ->
                    current.copy(
                        appearanceLabel = if (current.appearanceLabel == "Dark") "Light" else "Dark"
                    )
                }
            }

            AdminProfileAction.OnNotificationsClick -> {
                _uiState.update { current ->
                    current.copy(
                        notificationsLabel = if (current.notificationsLabel == "On") "Off" else "On"
                    )
                }
            }

            AdminProfileAction.OnChangePasswordClick -> Unit
            AdminProfileAction.OnLogoutClick -> Unit
            AdminProfileAction.OnRequestsClick -> Unit
            AdminProfileAction.OnHomeClick -> Unit
            AdminProfileAction.OnProfileClick -> Unit
        }
    }
}