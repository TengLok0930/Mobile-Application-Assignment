package com.example.fundforgoals.feature.organisation.home.presentation

import com.example.fundforgoals.supabase.model.Project

data class OrganisationHomeUiState(
    val currentUser: String,
    val loginOrganisation: String = "",
    val searchQuery: String = "",
    val selectedFilter: String = "Newest",
    val projects: List<Project> = emptyList(),
    val creatorNames: Map<Int, String> = emptyMap(),
    val selectedProjectId: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val selectedProject: Project?
        get() = projects.firstOrNull { it.id == selectedProjectId }
}