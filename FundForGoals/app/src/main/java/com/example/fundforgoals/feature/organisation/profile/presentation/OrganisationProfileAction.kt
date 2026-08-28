package com.example.fundforgoals.feature.organisation.profile.presentation

sealed interface OrganisationProfileAction {
    data object OnBackClick : OrganisationProfileAction
    data object OnLogoutClick : OrganisationProfileAction

    data object OnMessagesClick : OrganisationProfileAction
    data object OnHomeClick : OrganisationProfileAction
    data object OnProfileClick : OrganisationProfileAction

    data object OnViewContributionsClick : OrganisationProfileAction
    data object OnChangePasswordClick : OrganisationProfileAction

    data object OnToggleTheme : OrganisationProfileAction
    data object OnToggleNotifications : OrganisationProfileAction

    data object Refresh : OrganisationProfileAction
}