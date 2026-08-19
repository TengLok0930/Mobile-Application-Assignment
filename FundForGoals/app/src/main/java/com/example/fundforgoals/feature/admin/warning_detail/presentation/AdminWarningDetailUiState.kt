package com.example.fundforgoals.feature.admin.warning_detail.presentation

data class AdminWarningDetailUi(
    val id: String,
    val title: String,
    val organisation: String,
    val incidentTitle: String,
    val warningDetails: String
)

data class AdminWarningDetailUiState(
    val isLoading: Boolean = false,
    val project: AdminWarningDetailUi? = null,
    val errorMessage: String? = null
)