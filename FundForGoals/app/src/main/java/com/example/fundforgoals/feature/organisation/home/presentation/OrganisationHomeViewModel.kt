package com.example.fundforgoals.feature.organisation.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.Project
import com.example.fundforgoals.supabase.repository.ProjectRepository
import com.example.fundforgoals.supabase.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrganisationHomeViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val currentUser: String =
        checkNotNull(savedStateHandle["currentUser"])

    private val projectRepository = ProjectRepository()
    private val userRepository = UserRepository()

    private var allProjects: List<Project> = emptyList()
    private var creatorNames: Map<Int, String> = emptyMap()

    private val _uiState = MutableStateFlow(
        OrganisationHomeUiState(currentUser = currentUser)
    )
    val uiState: StateFlow<OrganisationHomeUiState> = _uiState.asStateFlow()

    init {
        loadProjects()
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val user = userRepository.getUserByUsername(currentUser)

                allProjects = user?.id?.let { userId ->
                    projectRepository.getProjectsByUser(userId)
                } ?: emptyList()

                val creatorIds = allProjects
                    .map { it.createdBy }
                    .distinct()

                creatorNames = creatorIds
                    .mapNotNull { creatorId ->
                        val creator = userRepository.getUserById(creatorId)
                        creator?.name?.let { creatorName ->
                            creatorId to creatorName
                        }
                    }
                    .toMap()

                _uiState.update {
                    it.copy(
                        loginOrganisation = user?.name ?: currentUser,
                        projects = allProjects,
                        creatorNames = creatorNames,
                        selectedProjectId = allProjects.firstOrNull()?.id,
                        isLoading = false
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

            OrganisationHomeAction.OnViewProjectClick -> Unit
            OrganisationHomeAction.OnNewProjectClick -> Unit
            OrganisationHomeAction.OnMessagesClick -> Unit
            OrganisationHomeAction.OnHomeClick -> Unit
            OrganisationHomeAction.OnProfileClick -> Unit

            OrganisationHomeAction.Refresh -> loadProjects()
        }
    }
}