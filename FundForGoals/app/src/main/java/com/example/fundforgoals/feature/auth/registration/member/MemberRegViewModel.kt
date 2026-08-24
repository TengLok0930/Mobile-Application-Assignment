package com.example.fundforgoals.feature.auth.registration.member

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MemberRegViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MemberRegUiState())
    val uiState: StateFlow<MemberRegUiState> = _uiState.asStateFlow()

    fun onAction(action: MemberRegAction) {
        when (action) {
            is MemberRegAction.OnUsernameChanged -> {
                _uiState.update {
                    it.copy(
                        username = action.value,
                        errorMessage = null,
                        isRegisterSuccessful = false
                    )
                }
            }

            is MemberRegAction.OnPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = action.value,
                        errorMessage = null,
                        isRegisterSuccessful = false
                    )
                }
            }

            is MemberRegAction.OnSocialUrlChanged -> {
                _uiState.update {
                    it.copy(
                        socialUrl = action.value,
                        errorMessage = null,
                        isRegisterSuccessful = false
                    )
                }
            }

            is MemberRegAction.OnConfirmPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        confirmPassword = action.value,
                        errorMessage = null,
                        isRegisterSuccessful = false
                    )
                }
            }

            MemberRegAction.OnTogglePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            MemberRegAction.OnToggleConfirmPasswordVisibility -> {
                _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }

            MemberRegAction.OnRegisterClick -> {
                register()
            }

            MemberRegAction.OnLoginClick -> Unit
            MemberRegAction.OnBackClick -> Unit
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

    private fun validate(state: MemberRegUiState): String? {
        return when {
            !state.isRegisterEnabled -> "All fields are required!"
            state.password.length < 6 -> "Password must be at least 6 characters!"
            state.password != state.confirmPassword -> "Passwords do not match!"
            else -> null
        }
    }

    fun onRegisterNavigated() {
        _uiState.update { it.copy(isRegisterSuccessful = false) }
    }
}