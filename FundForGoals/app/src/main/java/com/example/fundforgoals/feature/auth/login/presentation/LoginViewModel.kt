package com.example.fundforgoals.feature.auth.login.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnUsernameChanged -> {
                _uiState.update {
                    it.copy(
                        username = action.value,
                        errorMessage = null,
                        isLoginSuccessful = false
                    )
                }
            }

            is LoginAction.OnPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = action.value,
                        errorMessage = null,
                        isLoginSuccessful = false
                    )
                }
            }

            LoginAction.OnLoginClick -> {
                login()
            }

            LoginAction.OnForgotPasswordClick -> Unit
            LoginAction.OnSignUpClick -> Unit
            LoginAction.OnBackClick -> Unit
        }
    }

    private fun login() {
        val currentState = _uiState.value

        if (!currentState.isLoginEnabled) {
            _uiState.update {
                it.copy(
                    errorMessage = "Username and password cannot be empty!",
                    isLoginSuccessful = false
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                isLoginSuccessful = false
            )
        }

        _uiState.update {
            it.copy(
                isLoading = false,
                isLoginSuccessful = true
            )
        }
    }

    fun onLoginNavigated() {
        _uiState.update {
            it.copy(isLoginSuccessful = false)
        }
    }
}