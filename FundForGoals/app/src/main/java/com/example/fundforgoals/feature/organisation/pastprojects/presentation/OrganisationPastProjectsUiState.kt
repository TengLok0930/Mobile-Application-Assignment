package com.example.fundforgoals.feature.organisation.pastprojects.presentation

data class OrganisationPastProjectUi(
    val id: String,
    val title: String,
    val contributionAmountText: String = ""
)

data class OrganisationPastProjectsUiState(
    val isLoading: Boolean = false,
    val pastProjects: List<OrganisationPastProjectUi> = emptyList(),
    val errorMessage: String? = null
)