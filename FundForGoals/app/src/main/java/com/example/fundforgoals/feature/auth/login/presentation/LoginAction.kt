package com.example.fundforgoals.feature.auth.login.presentation

sealed interface LoginAction {
    data class OnUsernameChanged(val value: String) : LoginAction
    data class OnPasswordChanged(val value: String) : LoginAction
    data object OnLoginClick : LoginAction
    data object OnForgotPasswordClick : LoginAction
    data object OnSignUpClick : LoginAction
    data object OnBackClick : LoginAction
}