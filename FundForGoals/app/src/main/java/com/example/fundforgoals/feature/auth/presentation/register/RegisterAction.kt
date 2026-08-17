package com.example.fundforgoals.feature.auth.presentation.register

sealed interface RegisterAction {
    data class OnFullNameChanged(val value: String) : RegisterAction
    data class OnEmailChanged(val value: String) : RegisterAction
    data class OnUsernameChanged(val value: String) : RegisterAction
    data class OnPasswordChanged(val value: String) : RegisterAction
    data class OnConfirmPasswordChanged(val value: String) : RegisterAction
    data object OnTogglePasswordVisibility : RegisterAction
    data object OnToggleConfirmPasswordVisibility : RegisterAction
    data object OnRegisterClick : RegisterAction
    data object OnLoginClick : RegisterAction
    data object OnBackClick : RegisterAction
}
