package com.example.fundforgoals.feature.admin.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.repository.ProjectRepository
import com.example.fundforgoals.supabase.repository.UserRepository
import com.example.fundforgoals.supabase.repository.WarningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminProjectUi(
    val id: Int,
    val title: String,
    val organisation: String,
    val overview: String,
    val warningCount: Int,
    val warningDetails: String,
    val avatarUrl: String
)

data class AdminHomeUiState(
    val searchQuery: String = "",
    val projects: List<AdminProjectUi> = emptyList(),
    val selectedProjectId: Int? = null,
    val isLoading: Boolean = false,
    val isCancelling: Boolean = false,
    val errorMessage: String? = null
) {
    val selectedProject: AdminProjectUi?
        get() = projects.firstOrNull { it.id == selectedProjectId }
}

class AdminHomeViewModel : ViewModel() {

    private val projectRepository = ProjectRepository()
    private val userRepository = UserRepository()
    private val warningRepository = WarningRepository()

    private var allProjectUis: List<AdminProjectUi> = emptyList()

    private val _uiState = MutableStateFlow(AdminHomeUiState())
    val uiState: StateFlow<AdminHomeUiState> = _uiState.asStateFlow()

    init {
        loadProjects()
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val projects = projectRepository.getOngoingProjects()

                val creatorIds = projects.map { it.createdBy }.distinct()
                val creatorNames = creatorIds
                    .mapNotNull { creatorId ->
                        userRepository.getUserById(creatorId)?.name?.let { creatorId to it }
                    }
                    .toMap()

                val projectIds = projects.mapNotNull { it.id }
                val warningsByProject = warningRepository
                    .getWarningsByProjectIds(projectIds)
                    .groupBy { it.projectId }

                allProjectUis = projects.mapNotNull { project ->
                    project.id?.let { id ->
                        val projectWarnings = warningsByProject[id].orEmpty()
                        AdminProjectUi(
                            id = id,
                            title = project.title,
                            organisation = creatorNames[project.createdBy].orEmpty(),
                            overview = project.desc,
                            warningCount = projectWarnings.size,
                            warningDetails = projectWarnings.maxByOrNull { it.createdAt }?.details
                                ?: "No warning details are currently available for this project.",
                            avatarUrl = project.avatarUrl
                        )
                    }
                }

                _uiState.update { current ->
                    val stillExists = allProjectUis.any { it.id == current.selectedProjectId }
                    current.copy(
                        projects = allProjectUis,
                        selectedProjectId = if (stillExists) current.selectedProjectId else null,
                        isLoading = false
                    )
                }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = exception.message ?: "Failed to load projects")
                }
            }
        }
    }

    fun onAction(action: AdminHomeAction) {
        when (action) {
            is AdminHomeAction.OnSearchQueryChanged -> onSearchQueryChanged(action.value)

            is AdminHomeAction.OnMonitorClick -> {
                _uiState.update { it.copy(selectedProjectId = action.projectId) }
            }

            AdminHomeAction.OnCancelProjectClick -> cancelSelectedProject()
            AdminHomeAction.OnClearSelection -> {
                _uiState.update { it.copy(selectedProjectId = null) }
            }
            AdminHomeAction.OnRequestClick -> Unit
            AdminHomeAction.OnHomeClick -> Unit
            AdminHomeAction.OnProfileClick -> Unit
            AdminHomeAction.Refresh -> loadProjects()
        }
    }

    private fun cancelSelectedProject() {
        val projectId = _uiState.value.selectedProjectId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isCancelling = true, errorMessage = null) }

            try {
                val project = projectRepository.getProjectById(projectId)
                    ?: throw IllegalStateException("Project not found")

                projectRepository.modifyProject(project.copy(status = "Cancelled"))
                _uiState.update { it.copy(isCancelling = false) }
                loadProjects()
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(isCancelling = false, errorMessage = exception.message ?: "Failed to cancel project")
                }
            }
        }
    }

    private fun onSearchQueryChanged(query: String) {
        val filtered = if (query.isBlank()) {
            allProjectUis
        } else {
            allProjectUis.filter { project ->
                project.title.contains(query, ignoreCase = true) ||
                        project.organisation.contains(query, ignoreCase = true)
            }
        }

        _uiState.update { current ->
            val stillExists = filtered.any { it.id == current.selectedProjectId }
            current.copy(
                searchQuery = query,
                projects = filtered,
                selectedProjectId = if (stillExists) current.selectedProjectId else null
            )
        }
    }
}