package com.example.fundforgoals.feature.organisation.contribute.presentation

sealed interface OrganisationContributeAction {
    data class OnFundAmountChanged(val value: String) : OrganisationContributeAction
    data object OnSubmitClick : OrganisationContributeAction
    data object OnBackClick : OrganisationContributeAction
}