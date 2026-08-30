package com.example.fundforgoals.feature.organisation.warninglist.presentation

sealed interface OrganisationWarningListAction {
    data object OnBackClick : OrganisationWarningListAction
    data object OnRefreshClick : OrganisationWarningListAction
    data class OnWarningClick(val warningId: Int) : OrganisationWarningListAction
}