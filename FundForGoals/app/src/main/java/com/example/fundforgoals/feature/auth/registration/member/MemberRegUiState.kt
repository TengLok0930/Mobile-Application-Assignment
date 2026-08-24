package com.example.fundforgoals.feature.auth.registration.member

data class MemberRegUiState(
    val username: String = "",
    val socialUrl: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegisterSuccessful: Boolean = false
) {
    val isRegisterEnabled: Boolean
        get() = username.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank()
}