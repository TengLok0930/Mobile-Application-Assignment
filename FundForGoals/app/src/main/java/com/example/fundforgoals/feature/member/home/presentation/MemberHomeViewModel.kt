package com.example.fundforgoals.feature.member.home.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MemberHomeViewModel : ViewModel() {

    private val allProjects = listOf(
        ProjectUi(
            id = "1",
            title = "Project 1",
            organisation = "Organisation 1",
            description = "Description 1",
            progress = 0.45f,
            contributionAmount = 500
        ),
        ProjectUi(
            id = "2",
            title = "Project 2",
            organisation = "Organisation 2",
            description = "Description 2",
            progress = 0.70f,
            contributionAmount = 850
        ),
        ProjectUi(
            id = "3",
            title = "Project 3",
            organisation = "Organisation 3",
            description = "Description 3",
            progress = 0.30f,
            contributionAmount = 300
        )
    )

    private val _uiState = MutableStateFlow(
        MemberHomeUiState(
            projects = allProjects,
            selectedProjectId = allProjects.firstOrNull()?.id
        )
    )
    val uiState: StateFlow<MemberHomeUiState> = _uiState.asStateFlow()

    fun onAction(action: MemberHomeAction) {
        when (action) {
            is MemberHomeAction.OnSearchQueryChanged -> {
                _uiState.update { currentState ->
                    val filteredProjects = allProjects.filter {
                        it.title.contains(action.value, ignoreCase = true) ||
                                it.organisation.contains(action.value, ignoreCase = true)
                    }

                    val selectedStillExists = filteredProjects.any {
                        it.id == currentState.selectedProjectId
                    }

                    currentState.copy(
                        searchQuery = action.value,
                        projects = filteredProjects,
                        selectedProjectId = when {
                            filteredProjects.isEmpty() -> null
                            selectedStillExists -> currentState.selectedProjectId
                            else -> filteredProjects.first().id
                        }
                    )
                }
            }

            is MemberHomeAction.OnProjectClick -> {
                _uiState.update {
                    it.copy(selectedProjectId = action.projectId)
                }
            }

            MemberHomeAction.OnMessagesClick -> Unit
            MemberHomeAction.OnHomeClick -> Unit
            MemberHomeAction.OnProfileClick -> Unit

            MemberHomeAction.Refresh -> {
                _uiState.update {
                    it.copy(
                        projects = allProjects,
                        selectedProjectId = allProjects.firstOrNull()?.id
                    )
                }
            }
        }
    }
}