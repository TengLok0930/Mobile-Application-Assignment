package com.example.fundforgoals.feature.auth.presentation.login

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
                _uiState.update { it.copy(username = action.value, errorMessage = null) }
            }

            is LoginAction.OnPasswordChanged -> {
                _uiState.update { it.copy(password = action.value, errorMessage = null) }
            }

            LoginAction.OnLoginClick -> {
                login()
            }

            LoginAction.OnForgotPasswordClick -> {

            }

            LoginAction.OnSignUpClick -> {

            }

            LoginAction.OnBackClick -> {

            }
        }
    }

    private fun login() {
        val currentState = _uiState.value

        if (!currentState.isLoginEnabled) {
            _uiState.update {
                it.copy(errorMessage = "Username and password cannot be empty!")
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        _uiState.update { it.copy(isLoading = false) }
    }
}