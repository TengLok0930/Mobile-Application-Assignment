package com.example.fundforgoals.feature.auth.registration.organisation.presentation

sealed interface OrganisationRegAction {
    data class OnCompanyNameChanged(val value: String) : OrganisationRegAction
    data class OnCompanyProfileUrlChanged(val value: String) : OrganisationRegAction
    data class OnPasswordChanged(val value: String) : OrganisationRegAction
    data class OnConfirmPasswordChanged(val value: String) : OrganisationRegAction
    data object OnTogglePasswordVisibility : OrganisationRegAction
    data object OnToggleConfirmPasswordVisibility : OrganisationRegAction
    data object OnRegisterClick : OrganisationRegAction
    data object OnLoginClick : OrganisationRegAction
    data object OnBackClick : OrganisationRegAction
}