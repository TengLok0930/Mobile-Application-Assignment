package com.example.fundforgoals.feature.auth.registration.organisation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.User
import com.example.fundforgoals.supabase.model.UserRequest
import com.example.fundforgoals.supabase.repository.UserRepository
import com.example.fundforgoals.supabase.repository.UserRequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OrganisationRegUiState(
    val companyName: String = "",
    val companyProfileUrl: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegisterSuccessful: Boolean = false
) {
    val isRegisterEnabled: Boolean
        get() = companyName.isNotBlank() &&
                companyProfileUrl.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank()
}

class OrganisationRegViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val userRequestRepository = UserRequestRepository()

    private val _uiState = MutableStateFlow(OrganisationRegUiState())
    val uiState: StateFlow<OrganisationRegUiState> = _uiState.asStateFlow()

    fun onAction(action: OrganisationRegAction) {
        when (action) {
            is OrganisationRegAction.OnCompanyNameChanged -> {
                _uiState.update {
                    it.copy(
                        companyName = action.value,
                        errorMessage = null,
                        isRegisterSuccessful = false
                    )
                }
            }

            is OrganisationRegAction.OnCompanyProfileUrlChanged -> {
                _uiState.update {
                    it.copy(
                        companyProfileUrl = action.value,
                        errorMessage = null,
                        isRegisterSuccessful = false
                    )
                }
            }

            is OrganisationRegAction.OnPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = action.value,
                        errorMessage = null,
                        isRegisterSuccessful = false
                    )
                }
            }

            is OrganisationRegAction.OnConfirmPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        confirmPassword = action.value,
                        errorMessage = null,
                        isRegisterSuccessful = false
                    )
                }
            }

            OrganisationRegAction.OnTogglePasswordVisibility -> {
                _uiState.update {
                    it.copy(isPasswordVisible = !it.isPasswordVisible)
                }
            }

            OrganisationRegAction.OnToggleConfirmPasswordVisibility -> {
                _uiState.update {
                    it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
                }
            }

            OrganisationRegAction.OnRegisterClick -> {
                register()
            }

            OrganisationRegAction.OnLoginClick -> Unit
            OrganisationRegAction.OnBackClick -> Unit
        }
    }

    private fun register() {
        if (_uiState.value.isLoading) return

        val currentState = _uiState.value
        val normalizedState = currentState.copy(
            companyName = currentState.companyName.trim(),
            companyProfileUrl = currentState.companyProfileUrl.trim()
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

        val companyName = normalizedState.companyName
        val companyProfileUrl = normalizedState.companyProfileUrl

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    isRegisterSuccessful = false
                )
            }

            try {
                val existingUser = userRepository.getUserByUsername(companyName)
                if (existingUser != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Organisation name already exists.",
                            isRegisterSuccessful = false
                        )
                    }
                    return@launch
                }

                val createdUser = userRepository.addUser(
                    User(
                        name = companyName,
                        password = normalizedState.password,
                        socialLink = companyProfileUrl,
                        avatarUrl = "https://gravatar.com/avatar/7462f2bae9324ee53865e09f9286e94e?s=400&d=identicon&r=x",
                        userType = "ORGANISATION",
                        isApproved = false
                    )
                )

                val requestDetails = """
                    Organisation Name: ${createdUser.name}
                    Company Profile URL: ${createdUser.socialLink}
                """.trimIndent()

                userRequestRepository.addUserRequest(
                    UserRequest(
                        id = null,
                        createdAt = "",
                        userId = createdUser.id
                            ?: throw IllegalStateException("Created organisation ID is missing."),
                        requestType = "ORGANISATION_REGISTRATION",
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
                        companyName = "",
                        companyProfileUrl = "",
                        password = "",
                        confirmPassword = ""
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to submit organisation registration request.",
                        isRegisterSuccessful = false
                    )
                }
            }
        }
    }

    private fun validate(state: OrganisationRegUiState): String? {
        return when {
            !state.isRegisterEnabled -> "All fields are required."
            !state.companyProfileUrl.startsWith("http://") &&
                    !state.companyProfileUrl.startsWith("https://") ->
                "Please enter a valid company profile URL."
            state.password.length < 6 -> "Password must be at least 6 characters."
            state.password != state.confirmPassword -> "Passwords do not match."
            else -> null
        }
    }

    fun onRegisterNavigated() {
        _uiState.update {
            it.copy(isRegisterSuccessful = false)
        }
    }
}