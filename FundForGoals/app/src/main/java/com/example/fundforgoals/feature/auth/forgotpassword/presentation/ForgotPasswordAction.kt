package com.example.fundforgoals.feature.auth.forgotpassword.presentation

sealed interface ForgotPasswordAction {
    data object OnBackClick : ForgotPasswordAction
    data class OnUsernameChange(val value: String) : ForgotPasswordAction
    data class OnNewPasswordChange(val value: String) : ForgotPasswordAction
    data class OnConfirmPasswordChange(val value: String) : ForgotPasswordAction
    data object OnToggleNewPasswordVisibility : ForgotPasswordAction
    data object OnToggleConfirmPasswordVisibility : ForgotPasswordAction
    data object OnSubmitClick : ForgotPasswordAction
    data object OnDismissDialog : ForgotPasswordAction
    data object OnDialogOkClick : ForgotPasswordAction
}