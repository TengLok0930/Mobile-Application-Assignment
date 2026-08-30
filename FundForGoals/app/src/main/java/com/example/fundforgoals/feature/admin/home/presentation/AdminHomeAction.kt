package com.example.fundforgoals.feature.admin.home.presentation

sealed interface AdminHomeAction {
    data class OnSearchQueryChanged(val value: String) : AdminHomeAction
    data class OnMonitorClick(val projectId: Int) : AdminHomeAction
    data object OnCancelProjectClick : AdminHomeAction
    data object OnClearSelection : AdminHomeAction

    data object OnRequestClick : AdminHomeAction
    data object OnHomeClick : AdminHomeAction
    data object OnProfileClick : AdminHomeAction
    data object Refresh : AdminHomeAction
}