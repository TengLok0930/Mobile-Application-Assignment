package com.example.fundforgoals.feature.auth.registration.organisation.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OrganisationRegViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OrganisationRegUiState())
    val uiState: StateFlow<OrganisationRegUiState> = _uiState.asStateFlow()

    fun onAction(action: OrganisationRegAction) {
        when (action) {
            is OrganisationRegAction.OnCompanyNameChanged -> {
                _uiState.update { it.copy(companyName = action.value) }
            }

            is OrganisationRegAction.OnPasswordChanged -> {
                _uiState.update { it.copy(password = action.value) }
            }

            is OrganisationRegAction.OnConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = action.value) }
            }

            is OrganisationRegAction.OnProfileFileSelected -> {
                _uiState.update { it.copy(profileFileName = action.filename) }
            }

            OrganisationRegAction.OnRegisterClick -> {
                val state = _uiState.value

                when {
                    state.companyName.isBlank() -> {
                        _uiState.update { it.copy(errorMessage = "Company name is required") }
                    }

                    state.password.isBlank() || state.confirmPassword.isBlank() -> {
                        _uiState.update { it.copy(errorMessage = "Password fields cannot be empty") }
                    }

                    state.password != state.confirmPassword -> {
                        _uiState.update { it.copy(errorMessage = "Passwords do not match") }
                    }

                    state.profileFileName.isNullOrBlank() -> {
                        _uiState.update { it.copy(errorMessage = "Please upload a company profile") }
                    }

                    else -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                    }
                }
            }

            OrganisationRegAction.OnBackClick -> Unit
        }
    }
}