package com.example.fundforgoals.feature.admin.profile.presentation

sealed interface AdminProfileAction {
    data object OnAppearanceClick : AdminProfileAction
    data object OnNotificationsClick : AdminProfileAction
    data object OnLogoutClick : AdminProfileAction

    data object OnRequestsClick : AdminProfileAction
    data object OnHomeClick : AdminProfileAction
    data object OnProfileClick : AdminProfileAction
}