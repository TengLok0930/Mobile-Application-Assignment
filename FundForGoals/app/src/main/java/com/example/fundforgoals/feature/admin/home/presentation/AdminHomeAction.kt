package com.example.fundforgoals.feature.admin.home.presentation

sealed interface AdminHomeAction {
    data class OnSearchQueryChanged(val value: String) : AdminHomeAction
    data class OnMonitorClick(val projectId: String) : AdminHomeAction

    data object OnBackClick : AdminHomeAction
    data object OnWarnProjectClick : AdminHomeAction
    data object OnWarnOrganisationClick : AdminHomeAction
    data object OnCancelProjectClick : AdminHomeAction
    data object OnViewChatroomClick : AdminHomeAction

    data object OnRequestClick : AdminHomeAction
    data object OnHomeClick : AdminHomeAction
    data object OnProfileClick : AdminHomeAction
    data object Refresh : AdminHomeAction
}