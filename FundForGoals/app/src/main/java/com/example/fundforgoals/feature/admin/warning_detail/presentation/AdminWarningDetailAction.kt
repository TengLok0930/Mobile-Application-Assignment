package com.example.fundforgoals.feature.admin.warning_detail.presentation

sealed interface AdminWarningDetailAction {
    data object OnBackClick : AdminWarningDetailAction
    data object OnWarnOrganisationClick : AdminWarningDetailAction
}