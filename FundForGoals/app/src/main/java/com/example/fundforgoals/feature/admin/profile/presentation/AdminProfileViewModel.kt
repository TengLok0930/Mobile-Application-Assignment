package com.example.fundforgoals.feature.admin.profile.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AdminProfileUiState(
    val isLoading: Boolean = false,
    val adminName: String = "Administrator",
    val appearanceLabel: String = "Dark",
    val notificationsLabel: String = "On",
    val errorMessage: String? = null
)

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

    fun setAppearanceLabel(isDarkTheme: Boolean) {
        _uiState.update { current ->
            current.copy(
                appearanceLabel = if (isDarkTheme) "Dark" else "Light"
            )
        }
    }

    fun onAction(action: AdminProfileAction) {
        when (action) {
            AdminProfileAction.OnAppearanceClick -> Unit
            AdminProfileAction.OnNotificationsClick -> {
                _uiState.update { current ->
                    current.copy(
                        notificationsLabel = if (current.notificationsLabel == "On") "Off" else "On"
                    )
                }
            }
            AdminProfileAction.OnLogoutClick -> Unit
            AdminProfileAction.OnRequestsClick -> Unit
            AdminProfileAction.OnHomeClick -> Unit
            AdminProfileAction.OnProfileClick -> Unit
        }
    }
}