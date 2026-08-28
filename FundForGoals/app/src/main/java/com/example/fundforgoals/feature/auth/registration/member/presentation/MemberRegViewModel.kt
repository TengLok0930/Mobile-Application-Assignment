package com.example.fundforgoals.feature.auth.registration.member

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.feature.auth.registration.member.presentation.MemberRegAction
import com.example.fundforgoals.supabase.model.User
import com.example.fundforgoals.supabase.model.UserRequest
import com.example.fundforgoals.supabase.repository.UserRepository
import com.example.fundforgoals.supabase.repository.UserRequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
                socialUrl.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank()
}

class MemberRegViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val userRequestRepository = UserRequestRepository()

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
        if (_uiState.value.isLoading) return

        val currentState = _uiState.value
        val normalizedState = currentState.copy(
            username = currentState.username.trim(),
            socialUrl = currentState.socialUrl.trim()
        )

        val validationError = validate(normalizedState)

        if (validationError != null) {
            _uiState.update {
                it.copy(
                    errorMessage = validationError,
                    isRegisterSuccessful = false
                )
            }
            return
        }

        val username = normalizedState.username
        val socialUrl = normalizedState.socialUrl

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    isRegisterSuccessful = false
                )
            }

            try {
                val existingUser = userRepository.getUserByUsername(username)
                if (existingUser != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Username already exists.",
                            isRegisterSuccessful = false
                        )
                    }
                    return@launch
                }

                val createdUser = userRepository.addUser(
                    User(
                        name = username,
                        password = currentState.password,
                        socialLink = socialUrl,
                        avatarUrl = "",
                        userType = "MEMBER",
                        isApproved = false
                    )
                )

                val requestDetails = """
                Username: ${createdUser.name}
                Social URL: ${createdUser.socialLink}
            """.trimIndent()

                userRequestRepository.addUserRequest(
                    UserRequest(
                        id = null,
                        createdAt = "",
                        userId = createdUser.id
                            ?: throw IllegalStateException("Created user ID is missing."),
                        requestType = "MEMBER_REGISTRATION",
                        details = requestDetails,
                        aiOverview = null,
                        status = "pending",
                        user = null
                    )
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRegisterSuccessful = true,
                        errorMessage = null,
                        username = "",
                        socialUrl = "",
                        password = "",
                        confirmPassword = ""
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to submit registration request.",
                        isRegisterSuccessful = false
                    )
                }
            }
        }
    }

    private fun validate(state: MemberRegUiState): String? {
        return when {
            !state.isRegisterEnabled -> "All fields are required."
            !state.socialUrl.startsWith("http://") && !state.socialUrl.startsWith("https://") ->
                "Please enter a valid social URL."
            state.password.length < 6 -> "Password must be at least 6 characters."
            state.password != state.confirmPassword -> "Passwords do not match."
            else -> null
        }
    }

    fun onRegisterNavigated() {
        _uiState.update { it.copy(isRegisterSuccessful = false) }
    }
}