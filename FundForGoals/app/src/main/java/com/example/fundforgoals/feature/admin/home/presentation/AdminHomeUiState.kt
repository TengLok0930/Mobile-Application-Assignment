package com.example.fundforgoals.feature.admin.home.presentation

enum class AdminDetailPane {
    MONITOR,
    WARNING
}

data class AdminProjectUi(
    val id: String,
    val title: String,
    val organisation: String,
    val overview: String = "",
    val warningCount: Int = 0,
    val incidentTitle: String = "",
    val warningDetails: String = ""
)

data class AdminHomeUiState(
    val searchQuery: String = "",
    val projects: List<AdminProjectUi> = emptyList(),
    val selectedProject: AdminProjectUi? = null,
    val activeDetailPane: AdminDetailPane = AdminDetailPane.MONITOR,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)