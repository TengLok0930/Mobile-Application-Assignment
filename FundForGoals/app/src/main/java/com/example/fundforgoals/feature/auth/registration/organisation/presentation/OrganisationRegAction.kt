package com.example.fundforgoals.feature.auth.registration.organisation.presentation

sealed interface OrganisationRegAction {
    data class OnCompanyNameChanged(val value: String) : OrganisationRegAction
    data class OnPasswordChanged(val value: String) : OrganisationRegAction
    data class OnConfirmPasswordChanged(val value: String) : OrganisationRegAction
    data class OnProfileFileSelected(val filename: String) : OrganisationRegAction
    data object OnRegisterClick : OrganisationRegAction
    data object OnBackClick : OrganisationRegAction
}