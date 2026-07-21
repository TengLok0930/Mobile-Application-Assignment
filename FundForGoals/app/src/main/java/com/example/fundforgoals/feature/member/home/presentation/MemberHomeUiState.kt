package com.example.fundforgoals.feature.member.home.presentation

data class ProjectUi(
    val id: String,
    val title: String,
    val organisation: String,
    val description: String
)

data class MemberHomeUiState(
    val searchQuery: String = "",
    val selectedFilter: String = "Newest",
    val projects: List<ProjectUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)