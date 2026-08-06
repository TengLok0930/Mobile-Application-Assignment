package com.example.fundforgoals.feature.auth.presentation.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
) {
    val isLoginEnabled: Boolean
        get() = username.isNotBlank() && password.isNotBlank()
}