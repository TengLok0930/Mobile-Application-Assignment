package com.example.fundforgoals.feature.auth.changepassword.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.User
import com.example.fundforgoals.supabase.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChangePasswordUiState(
    val username: String = "",
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val showOldPassword: Boolean = false,
    val showNewPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val isLoading: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val errorMessage: String? = null
) {
    val isSubmitEnabled: Boolean
        get() = username.isNotBlank() &&
                oldPassword.isNotBlank() &&
                newPassword.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                !isLoading
}

class ChangePasswordViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private var currentUser: User? = null

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun setLoggedInUsername(username: String) {
        if (_uiState.value.username == username) return

        _uiState.update {
            it.copy(username = username)
        }

        viewModelScope.launch {
            try {
                currentUser = userRepository.getUserByUsername(username)
            } catch (_: Exception) {
                currentUser = null
            }
        }
    }

    fun onAction(action: ChangePasswordAction) {
        when (action) {
            ChangePasswordAction.OnBackClick -> Unit

            is ChangePasswordAction.OnOldPasswordChange -> {
                _uiState.update {
                    it.copy(
                        oldPassword = action.value,
                        errorMessage = null,
                        showSuccessDialog = false
                    )
                }
            }

            is ChangePasswordAction.OnNewPasswordChange -> {
                _uiState.update {
                    it.copy(
                        newPassword = action.value,
                        errorMessage = null,
                        showSuccessDialog = false
                    )
                }
            }

            is ChangePasswordAction.OnConfirmPasswordChange -> {
                _uiState.update {
                    it.copy(
                        confirmPassword = action.value,
                        errorMessage = null,
                        showSuccessDialog = false
                    )
                }
            }

            ChangePasswordAction.OnToggleOldPasswordVisibility -> {
                _uiState.update {
                    it.copy(showOldPassword = !it.showOldPassword)
                }
            }

            ChangePasswordAction.OnToggleNewPasswordVisibility -> {
                _uiState.update {
                    it.copy(showNewPassword = !it.showNewPassword)
                }
            }

            ChangePasswordAction.OnToggleConfirmPasswordVisibility -> {
                _uiState.update {
                    it.copy(showConfirmPassword = !it.showConfirmPassword)
                }
            }

            ChangePasswordAction.OnSubmitClick -> {
                submitPasswordChange()
            }

            ChangePasswordAction.OnDismissDialog,
            ChangePasswordAction.OnDialogOkClick -> {
                _uiState.update {
                    it.copy(showSuccessDialog = false)
                }
            }
        }
    }

    private fun submitPasswordChange() {
        val state = _uiState.value
        val user = currentUser

        when {
            state.username.isBlank() -> {
                _uiState.update {
                    it.copy(errorMessage = "User not found.")
                }
                return
            }

            user == null || user.id == null -> {
                _uiState.update {
                    it.copy(errorMessage = "User not found.")
                }
                return
            }

            state.oldPassword.isBlank() -> {
                _uiState.update {
                    it.copy(errorMessage = "Old password is required.")
                }
                return
            }

            state.oldPassword != user.password -> {
                _uiState.update {
                    it.copy(errorMessage = "Old password is incorrect.")
                }
                return
            }

            state.newPassword.length < 6 -> {
                _uiState.update {
                    it.copy(errorMessage = "New password must be at least 6 characters long.")
                }
                return
            }

            state.newPassword != state.confirmPassword -> {
                _uiState.update {
                    it.copy(errorMessage = "Passwords do not match.")
                }
                return
            }

            state.newPassword == state.oldPassword -> {
                _uiState.update {
                    it.copy(errorMessage = "New password must be different from old password.")
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
                val updatedUser = userRepository.updateUserPassword(
                    id = user.id,
                    newPassword = state.newPassword
                )

                currentUser = updatedUser

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        oldPassword = "",
                        newPassword = "",
                        confirmPassword = "",
                        showSuccessDialog = true,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to change password."
                    )
                }
            }
        }
    }
}