package com.example.fundforgoals.feature.admin.home.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

class AdminHomeViewModel : ViewModel() {

    private val allProjects = listOf(
        AdminProjectUi(
            id = "1",
            title = "Project 1",
            organisation = "Organisation 1",
            overview = "This project requires admin monitoring due to recent activity and warning reports.",
            warningCount = 1,
            incidentTitle = "What happened to Project 1",
            warningDetails = "A warning was raised because the project timeline slipped and recent updates from the organisation were incomplete."
        ),
        AdminProjectUi(
            id = "2",
            title = "Project 2",
            organisation = "Organisation 1",
            overview = "This project has multiple warning signals and may need direct admin intervention.",
            warningCount = 2,
            incidentTitle = "What happened to Project 2",
            warningDetails = "Two reports flagged this project for delayed milestones and inconsistent communication."
        ),
        AdminProjectUi(
            id = "3",
            title = "Project 3",
            organisation = "Organisation 2",
            overview = "This project is stable but still available for admin monitoring.",
            warningCount = 0,
            incidentTitle = "What happened to Project 3",
            warningDetails = "No warning details are currently available for this project."
        )
    )

    private val _uiState = MutableStateFlow(
        AdminHomeUiState(
            projects = allProjects,
            isLoading = false
        )
    )
    val uiState: StateFlow<AdminHomeUiState> = _uiState.asStateFlow()

    fun onAction(action: AdminHomeAction) {
        when (action) {
            is AdminHomeAction.OnSearchQueryChanged -> onSearchQueryChanged(action.value)
            is AdminHomeAction.OnMonitorClick -> onMonitorClick(action.projectId)

            AdminHomeAction.OnWarnProjectClick -> {
                _uiState.update { it.copy(activeDetailPane = AdminDetailPane.WARNING) }
            }

            AdminHomeAction.OnBackClick -> {
                _uiState.update { current ->
                    if (current.activeDetailPane == AdminDetailPane.WARNING) {
                        current.copy(activeDetailPane = AdminDetailPane.MONITOR)
                    } else {
                        current.copy(selectedProject = null)
                    }
                }
            }

            AdminHomeAction.OnWarnOrganisationClick -> Unit
            AdminHomeAction.OnCancelProjectClick -> Unit
            AdminHomeAction.OnViewChatroomClick -> Unit
            AdminHomeAction.OnRequestClick -> Unit
            AdminHomeAction.OnHomeClick -> Unit
            AdminHomeAction.OnProfileClick -> Unit

            AdminHomeAction.Refresh -> {
                _uiState.update {
                    it.copy(
                        searchQuery = "",
                        projects = allProjects,
                        selectedProject = null,
                        activeDetailPane = AdminDetailPane.MONITOR,
                        errorMessage = null
                    )
                }
            }
        }
    }

    private fun onMonitorClick(projectId: String) {
        _uiState.update { current ->
            val selected = allProjects.find { it.id == projectId }
            current.copy(
                selectedProject = selected,
                activeDetailPane = AdminDetailPane.MONITOR
            )
        }
    }

    private fun onSearchQueryChanged(query: String) {
        val filteredProjects = if (query.isBlank()) {
            allProjects
        } else {
            allProjects.filter { project ->
                project.title.contains(query, ignoreCase = true) ||
                        project.organisation.contains(query, ignoreCase = true)
            }
        }

        _uiState.update { current ->
            val selectedStillExists = current.selectedProject?.id?.let { id ->
                filteredProjects.find { it.id == id }
            }

            current.copy(
                searchQuery = query,
                projects = filteredProjects,
                selectedProject = selectedStillExists,
                errorMessage = null,
                activeDetailPane = if (selectedStillExists == null) {
                    AdminDetailPane.MONITOR
                } else {
                    current.activeDetailPane
                }
            )
        }
    }
}