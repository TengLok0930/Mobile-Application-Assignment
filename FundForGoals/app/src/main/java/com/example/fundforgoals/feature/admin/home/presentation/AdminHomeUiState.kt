package com.example.fundforgoals.feature.admin.home.presentation

data class AdminProjectUi(
    val id: String,
    val title: String,
    val organisation: String
)

data class AdminHomeUiState(
    val searchQuery: String = "",
    val projects: List<AdminProjectUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)