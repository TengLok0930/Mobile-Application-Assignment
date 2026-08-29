package com.example.fundforgoals.feature.member.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.Project
import com.example.fundforgoals.supabase.repository.ContributorRepository
import com.example.fundforgoals.supabase.repository.ProjectRepository
import com.example.fundforgoals.supabase.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MemberHomeUiState(
    val currentUser: String,
    val searchQuery: String = "",
    val selectedFilter: String = "Newest",
    val projects: List<Project> = emptyList(),
    val creatorNames: Map<Int, String> = emptyMap(),
    val projectFunds: Map<Int, Double> = emptyMap(),
    val selectedProjectId: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val selectedProject: Project?
        get() = projects.firstOrNull { it.id == selectedProjectId }
}

class MemberHomeViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentUser: String =
        checkNotNull(savedStateHandle["currentUser"])

    private val projectRepository = ProjectRepository()
    private val userRepository = UserRepository()
    private val contributorRepository = ContributorRepository()

    private var allProjects: List<Project> = emptyList()
    private var creatorNames: Map<Int, String> = emptyMap()
    private var projectFunds: Map<Int, Double> = emptyMap()

    private val _uiState = MutableStateFlow(
        MemberHomeUiState(currentUser = currentUser)
    )
    val uiState: StateFlow<MemberHomeUiState> = _uiState.asStateFlow()

    init {
        loadProjects()
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val currentUserId = userRepository.getUserByUsername(currentUser)?.id

                val contributedProjectIds = currentUserId?.let { userId ->
                    contributorRepository.getContributorsByUserId(userId).map { it.project }
                } ?: emptyList()

                allProjects = projectRepository.getContributableProjects(contributedProjectIds)

                val creatorIds = allProjects.map { it.createdBy }.distinct()
                creatorNames = creatorIds
                    .mapNotNull { creatorId ->
                        userRepository.getUserById(creatorId)?.name?.let { creatorId to it }
                    }
                    .toMap()

                val projectIds = allProjects.mapNotNull { it.id }
                projectFunds = contributorRepository.getTotalFundsByProjectIds(projectIds)

                _uiState.update {
                    it.copy(
                        projects = allProjects,
                        creatorNames = creatorNames,
                        projectFunds = projectFunds,
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

    fun onAction(action: MemberHomeAction) {
        when (action) {
            is MemberHomeAction.OnSearchQueryChanged -> {
                searchProjects(action.value)
            }

            is MemberHomeAction.OnProjectClick -> {
                _uiState.update {
                    it.copy(
                        selectedProjectId = action.projectId
                    )
                }
            }

            MemberHomeAction.OnMessagesClick -> Unit

            MemberHomeAction.OnHomeClick -> Unit

            MemberHomeAction.OnProfileClick -> Unit

            MemberHomeAction.Refresh -> {
                loadProjects()
            }
        }
    }

    private fun searchProjects(query: String) {
        val filteredProjects = if (query.isBlank()) {
            allProjects
        } else {
            allProjects.filter { project ->
                val creatorName = creatorNames[project.createdBy].orEmpty()

                project.title.contains(
                    other = query,
                    ignoreCase = true
                ) || creatorName.contains(
                    other = query,
                    ignoreCase = true
                )
            }
        }

        _uiState.update { currentState ->
            val selectedStillExists = filteredProjects.any {
                it.id == currentState.selectedProjectId
            }

            currentState.copy(
                searchQuery = query,
                projects = filteredProjects,
                selectedProjectId = when {
                    filteredProjects.isEmpty() -> null
                    selectedStillExists -> currentState.selectedProjectId
                    else -> filteredProjects.firstOrNull()?.id
                }
            )
        }
    }
}