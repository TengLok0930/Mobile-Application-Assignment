package com.example.fundforgoals.feature.auth.registration.organisation.presentation

data class OrganisationRegUiState(
    val companyName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val profileFileName: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)