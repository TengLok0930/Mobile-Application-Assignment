package com.example.fundforgoals.feature.organisation.createProject.presentation

sealed interface CreateProjectAction {
    data object OnBackClick : CreateProjectAction
    data class OnTitleChange(val value: String) : CreateProjectAction
    data class OnDescriptionChange(val value: String) : CreateProjectAction
    data class OnGoalChange(val value: String) : CreateProjectAction
    data object OnSubmitClick : CreateProjectAction
    data object OnDismissDialog : CreateProjectAction
    data object OnDialogOkClick : CreateProjectAction
    data object OnMessagesClick : CreateProjectAction
    data object OnHomeClick : CreateProjectAction
    data object OnProfileClick : CreateProjectAction
}