package com.example.fundforgoals.feature.organisation.viewProject

sealed interface ViewProjectAction {
    data class OnSearchQueryChanged(val value: String) : ViewProjectAction
    data class OnProjectClick(val projectId: Int) : ViewProjectAction
    data object OnMessagesClick : ViewProjectAction
    data object OnHomeClick : ViewProjectAction
    data object OnProfileClick : ViewProjectAction
    data object Refresh : ViewProjectAction
}