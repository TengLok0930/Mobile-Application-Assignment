package com.example.fundforgoals.feature.auth.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.repository.UserRepository
import com.example.fundforgoals.supabase.viewModel.UserUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val userRepository = UserRepository()

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

        if (currentState.username == "admin") {
            if (currentState.password == "admin123") {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoginSuccessful = true,
                        errorMessage = null,
                        userType = "admin"
                    )
                }
                return
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Incorrect password",
                        isLoginSuccessful = false
                    )
                }
            }
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "User not found",
                    isLoginSuccessful = false
                )
            }
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    isLoginSuccessful = false
                )
            }

            try {
                val user = userRepository.getUserByUsername(
                    currentState.username.trim()
                )

                when {
                    user == null -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "User not found",
                                isLoginSuccessful = false
                            )
                        }
                    }

                    user.password != currentState.password -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Incorrect password",
                                isLoginSuccessful = false
                            )
                        }
                    }

                    else -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoginSuccessful = true,
                                errorMessage = null,
                                userType = user.userType
                            )
                        }
                    }
                }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Unable to login",
                        isLoginSuccessful = false
                    )
                }
            }
        }
    }

    fun onLoginNavigated() {
        _uiState.update {
            it.copy(isLoginSuccessful = false)
        }
    }
}