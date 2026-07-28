package com.example.fundforgoals.feature.member.home.presentation

data class ProjectUi(
    val id: String,
    val title: String,
    val organisation: String,
    val description: String,
    val progress: Float = 0f,
    val contributionAmount: Int = 0
)

data class MemberHomeUiState(
    val searchQuery: String = "",
    val selectedFilter: String = "Newest",
    val projects: List<ProjectUi> = emptyList(),
    val selectedProjectId: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val selectedProject: ProjectUi?
        get() = projects.firstOrNull { it.id == selectedProjectId }
}