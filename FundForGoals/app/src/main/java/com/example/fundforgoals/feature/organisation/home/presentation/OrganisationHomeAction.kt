package com.example.fundforgoals.feature.organisation.home.presentation

sealed interface OrganisationHomeAction {
    data class OnSearchQueryChanged(val value: String) : OrganisationHomeAction
    data class OnProjectClick(val projectId: Int) : OrganisationHomeAction
    data object OnNewProjectClick : OrganisationHomeAction
    data object OnViewProjectClick : OrganisationHomeAction
    data object OnMessagesClick : OrganisationHomeAction
    data object OnHomeClick : OrganisationHomeAction
    data object OnProfileClick : OrganisationHomeAction
    data object Refresh : OrganisationHomeAction
}