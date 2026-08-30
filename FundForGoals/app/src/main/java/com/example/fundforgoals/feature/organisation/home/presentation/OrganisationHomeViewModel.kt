package com.example.fundforgoals.feature.organisation.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.Project
import com.example.fundforgoals.supabase.repository.ContributorRepository
import com.example.fundforgoals.supabase.repository.ProjectRepository
import com.example.fundforgoals.supabase.repository.ProjectRequestRepository
import com.example.fundforgoals.supabase.repository.UserRepository
import com.example.fundforgoals.supabase.repository.WarningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OrganisationHomeUiState(
    val currentUser: String,
    val loginOrganisation: String = "",
    val searchQuery: String = "",
    val selectedFilter: String = "Newest",
    val projects: List<Project> = emptyList(),
    val creatorNames: Map<Int, String> = emptyMap(),
    val projectFunds: Map<Int, Double> = emptyMap(),
    val projectWarningCounts: Map<Int, Int> = emptyMap(),
    val projectAiOverviews: Map<Int, String> = emptyMap(),
    val selectedProjectId: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val selectedProject: Project?
        get() = projects.firstOrNull { it.id == selectedProjectId }
}

class OrganisationHomeViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentUser: String =
        checkNotNull(savedStateHandle["currentUser"])

    private val projectRepository = ProjectRepository()
    private val projectRequestRepository = ProjectRequestRepository()
    private val userRepository = UserRepository()
    private val contributorRepository = ContributorRepository()
    private val warningRepository = WarningRepository()

    private var allProjects: List<Project> = emptyList()
    private var creatorNames: Map<Int, String> = emptyMap()
    private var projectFunds: Map<Int, Double> = emptyMap()
    private var projectWarningCounts: Map<Int, Int> = emptyMap()
    private var projectAiOverviews: Map<Int, String> = emptyMap()

    private val _uiState = MutableStateFlow(
        OrganisationHomeUiState(currentUser = currentUser)
    )
    val uiState: StateFlow<OrganisationHomeUiState> = _uiState.asStateFlow()

    init {
        loadProjects()
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val user = userRepository.getUserByUsername(currentUser)

                allProjects = user?.id?.let { userId ->
                    projectRepository.getProjectsByOwnUserOngoing(userId)
                } ?: emptyList()

                val creatorIds = allProjects.map { it.createdBy }.distinct()
                creatorNames = creatorIds
                    .mapNotNull { creatorId ->
                        userRepository.getUserById(creatorId)?.name?.let { creatorId to it }
                    }
                    .toMap()

                val projectIds = allProjects.mapNotNull { it.id }

                projectAiOverviews = projectIds.associateWith { projectId ->
                    projectRequestRepository.getProjectRequestByProjectId(projectId)
                        ?.aiOverview
                        ?.takeIf { it.isNotBlank() }
                        ?: "No AI overview available."
                }

                projectFunds = contributorRepository.getTotalFundsByProjectIds(projectIds)

                val warnings = warningRepository.getWarningsByProjectIds(projectIds)
                projectWarningCounts = warnings
                    .groupBy { it.projectId }
                    .mapValues { (_, warningsForProject) -> warningsForProject.size }

                _uiState.update {
                    it.copy(
                        loginOrganisation = user?.name ?: currentUser,
                        projects = allProjects,
                        creatorNames = creatorNames,
                        projectFunds = projectFunds,
                        projectWarningCounts = projectWarningCounts,
                        selectedProjectId = allProjects.firstOrNull()?.id,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to load projects"
                    )
                }
            }
        }
    }

    fun onAction(action: OrganisationHomeAction) {
        when (action) {
            is OrganisationHomeAction.OnSearchQueryChanged -> {
                _uiState.update { currentState ->
                    val filteredProjects = allProjects.filter { project ->
                        val creatorName = creatorNames[project.createdBy] ?: ""
                        project.title.contains(action.value, ignoreCase = true) ||
                                creatorName.contains(action.value, ignoreCase = true)
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
                            else -> filteredProjects.firstOrNull()?.id
                        }
                    )
                }
            }

            is OrganisationHomeAction.OnProjectClick -> {
                _uiState.update {
                    it.copy(selectedProjectId = action.projectId)
                }
            }

            is OrganisationHomeAction.OnViewWarningsClick -> Unit
            OrganisationHomeAction.OnViewProjectClick -> Unit
            OrganisationHomeAction.OnNewProjectClick -> Unit
            OrganisationHomeAction.OnMessagesClick -> Unit
            OrganisationHomeAction.OnHomeClick -> Unit
            OrganisationHomeAction.OnProfileClick -> Unit
            OrganisationHomeAction.Refresh -> loadProjects()
        }
    }
}