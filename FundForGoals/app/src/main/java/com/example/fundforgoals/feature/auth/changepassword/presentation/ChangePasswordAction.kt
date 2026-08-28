package com.example.fundforgoals.feature.auth.changepassword.presentation

sealed interface ChangePasswordAction {
    data object OnBackClick : ChangePasswordAction
    data class OnOldPasswordChange(val value: String) : ChangePasswordAction
    data class OnNewPasswordChange(val value: String) : ChangePasswordAction
    data class OnConfirmPasswordChange(val value: String) : ChangePasswordAction
    data object OnToggleOldPasswordVisibility : ChangePasswordAction
    data object OnToggleNewPasswordVisibility : ChangePasswordAction
    data object OnToggleConfirmPasswordVisibility : ChangePasswordAction
    data object OnSubmitClick : ChangePasswordAction
    data object OnDismissDialog : ChangePasswordAction
    data object OnDialogOkClick : ChangePasswordAction
}