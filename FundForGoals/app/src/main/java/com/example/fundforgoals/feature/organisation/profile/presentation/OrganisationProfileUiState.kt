package com.example.fundforgoals.feature.organisation.profile.presentation

data class OrganisationPastProjectUi(
    val id: String,
    val title: String,
    val contributionAmountText: String = ""
)

data class OrganisationProfileUiState(
    val isLoading: Boolean = false,
    val organisationName: String = "Organisation 1",
    val appearanceLabel: String = "Dark",
    val notificationsLabel: String = "On",
    val pastProjects: List<OrganisationPastProjectUi> = emptyList(),
    val totalContributionsText: String = "$0",
    val errorMessage: String? = null
)