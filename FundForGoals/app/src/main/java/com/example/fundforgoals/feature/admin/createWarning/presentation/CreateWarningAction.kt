package com.example.fundforgoals.feature.admin.createWarning.presentation

sealed interface CreateWarningAction {
    data class OnDetailsChanged(val value: String) : CreateWarningAction
    data object OnSubmitClick : CreateWarningAction
    data object OnBackClick : CreateWarningAction
}