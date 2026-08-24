package com.example.fundforgoals.feature.admin.monitor_detail.presentation

data class AdminMonitorDetailUi(
    val id: String,
    val title: String,
    val organisation: String,
    val overview: String,
    val warningCount: Int,
    val incidentTitle: String,
    val warningDetails: String
)

data class AdminMonitorDetailUiState(
    val isLoading: Boolean = false,
    val project: AdminMonitorDetailUi? = null,
    val errorMessage: String? = null
)