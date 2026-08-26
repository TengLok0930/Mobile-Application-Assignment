package com.example.fundforgoals.feature.organisation.profile.presentation

sealed interface OrganisationProfileAction {
    data object OnBackClick : OrganisationProfileAction
    data object OnLogoutClick : OrganisationProfileAction

    data object OnMessagesClick : OrganisationProfileAction
    data object OnHomeClick : OrganisationProfileAction
    data object OnProfileClick : OrganisationProfileAction

    data object OnViewPastProjectsClick : OrganisationProfileAction
    data object OnViewContributionsClick : OrganisationProfileAction

    data object OnAppearanceClick : OrganisationProfileAction
    data object OnNotificationsClick : OrganisationProfileAction
    data object OnChangePasswordClick : OrganisationProfileAction
}