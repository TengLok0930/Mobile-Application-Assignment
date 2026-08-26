package com.example.fundforgoals.feature.organisation.pastprojects.presentation

sealed interface OrganisationPastProjectsAction {
    data object OnBackClick : OrganisationPastProjectsAction

    data object OnMessagesClick : OrganisationPastProjectsAction
    data object OnHomeClick : OrganisationPastProjectsAction
    data object OnProfileClick : OrganisationPastProjectsAction
}