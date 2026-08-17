package com.example.fundforgoals.feature.auth.presentation.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnFullNameChanged -> {
                _uiState.update {
                    it.copy(
                        fullName = action.value,
                        errorMessage = null,
                        isRegisterSuccessful = false
                    )
                }
            }

            is RegisterAction.OnEmailChanged -> {
                _uiState.update {
                    it.copy(
                        email = action.value,
                        errorMessage = null,
                        isRegisterSuccessful = false
                    )
                }
            }

            is RegisterAction.OnUsernameChanged -> {
                _uiState.update {
                    it.copy(
                        username = action.value,
                        errorMessage = null,
                        isRegisterSuccessful = false
                    )
                }
            }

            is RegisterAction.OnPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = action.value,
                        errorMessage = null,
                        isRegisterSuccessful = false
                    )
                }
            }

            is RegisterAction.OnConfirmPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        confirmPassword = action.value,
                        errorMessage = null,
                        isRegisterSuccessful = false
                    )
                }
            }

            RegisterAction.OnTogglePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            RegisterAction.OnToggleConfirmPasswordVisibility -> {
                _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }

            RegisterAction.OnRegisterClick -> {
                register()
            }

            RegisterAction.OnLoginClick -> Unit
            RegisterAction.OnBackClick -> Unit
        }
    }

    private fun register() {
        val currentState = _uiState.value

        val validationError = validate(currentState)
        if (validationError != null) {
            _uiState.update {
                it.copy(
                    errorMessage = validationError,
                    isRegisterSuccessful = false
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                isRegisterSuccessful = false
            )
        }

        // Simulated registration (no backend wired up yet).
        _uiState.update {
            it.copy(
                isLoading = false,
                isRegisterSuccessful = true
            )
        }
    }

    private fun validate(state: RegisterUiState): String? {
        return when {
            !state.isRegisterEnabled -> "All fields are required!"
            !EMAIL_REGEX.matches(state.email.trim()) -> "Please enter a valid email address!"
            state.password.length < 6 -> "Password must be at least 6 characters!"
            state.password != state.confirmPassword -> "Passwords do not match!"
            else -> null
        }
    }

    fun onRegisterNavigated() {
        _uiState.update { it.copy(isRegisterSuccessful = false) }
    }
}
