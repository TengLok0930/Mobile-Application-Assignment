package com.example.fundforgoals.feature.organisation.pastprojects.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrganisationPastProjectsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        OrganisationPastProjectsUiState(
            isLoading = false,
            pastProjects = listOf(
                OrganisationPastProjectUi(
                    id = "1",
                    title = "Project 5",
                    contributionAmountText = "$200 contributed"
                ),
                OrganisationPastProjectUi(
                    id = "2",
                    title = "Project 4",
                    contributionAmountText = "$150 contributed"
                ),
                OrganisationPastProjectUi(
                    id = "3",
                    title = "Project 3",
                    contributionAmountText = "$125 contributed"
                )
            )
        )
    )
    val uiState: StateFlow<OrganisationPastProjectsUiState> = _uiState.asStateFlow()
}