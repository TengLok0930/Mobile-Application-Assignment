package com.example.fundforgoals.feature.auth.presentation.register

data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegisterSuccessful: Boolean = false
) {
    val isRegisterEnabled: Boolean
        get() = fullName.isNotBlank() &&
            email.isNotBlank() &&
            username.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank()
}
