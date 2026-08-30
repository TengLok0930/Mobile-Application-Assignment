package com.example.fundforgoals.feature.admin.viewWarning.presentation

sealed interface ViewWarningAction {
    data object OnBackClick : ViewWarningAction
    data object OnRefreshClick : ViewWarningAction
}