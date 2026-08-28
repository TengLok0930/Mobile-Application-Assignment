package com.example.fundforgoals.feature.auth.forgotpassword.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ForgotPasswordUiState(
    val username: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val showNewPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val isLoading: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val errorMessage: String? = null
) {
    val isSubmitEnabled: Boolean
        get() = username.isNotBlank() &&
                newPassword.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                !isLoading
}

class ForgotPasswordViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun onAction(action: ForgotPasswordAction) {
        when (action) {
            ForgotPasswordAction.OnBackClick -> Unit

            is ForgotPasswordAction.OnUsernameChange -> {
                _uiState.update {
                    it.copy(
                        username = action.value,
                        errorMessage = null,
                        showSuccessDialog = false
                    )
                }
            }

            is ForgotPasswordAction.OnNewPasswordChange -> {
                _uiState.update {
                    it.copy(
                        newPassword = action.value,
                        errorMessage = null,
                        showSuccessDialog = false
                    )
                }
            }

            is ForgotPasswordAction.OnConfirmPasswordChange -> {
                _uiState.update {
                    it.copy(
                        confirmPassword = action.value,
                        errorMessage = null,
                        showSuccessDialog = false
                    )
                }
            }

            ForgotPasswordAction.OnToggleNewPasswordVisibility -> {
                _uiState.update {
                    it.copy(showNewPassword = !it.showNewPassword)
                }
            }

            ForgotPasswordAction.OnToggleConfirmPasswordVisibility -> {
                _uiState.update {
                    it.copy(showConfirmPassword = !it.showConfirmPassword)
                }
            }

            ForgotPasswordAction.OnSubmitClick -> {
                submitPasswordChange()
            }

            ForgotPasswordAction.OnDismissDialog,
            ForgotPasswordAction.OnDialogOkClick -> {
                _uiState.update {
                    it.copy(showSuccessDialog = false)
                }
            }
        }
    }

    private fun submitPasswordChange() {
        val currentState = _uiState.value
        val username = currentState.username.trim()
        val newPassword = currentState.newPassword
        val confirmPassword = currentState.confirmPassword

        when {
            username.isBlank() -> {
                _uiState.update {
                    it.copy(errorMessage = "Username is required.")
                }
                return
            }

            newPassword.length < 6 -> {
                _uiState.update {
                    it.copy(errorMessage = "Password must be at least 6 characters long.")
                }
                return
            }

            newPassword != confirmPassword -> {
                _uiState.update {
                    it.copy(errorMessage = "Passwords do not match.")
                }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val user = userRepository.getUserByUsername(username)

                if (user == null || user.id == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "User not found."
                        )
                    }
                    return@launch
                }

                userRepository.updateUserPassword(
                    id = user.id,
                    newPassword = newPassword
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        showSuccessDialog = true,
                        errorMessage = null,
                        newPassword = "",
                        confirmPassword = ""
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to update password."
                    )
                }
            }
        }
    }
}