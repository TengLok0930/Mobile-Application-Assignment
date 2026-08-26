package com.example.fundforgoals.supabase.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.Project
import com.example.fundforgoals.supabase.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class ProjectUiState(
    val isLoading: Boolean = false,
    val projects: List<Project> = emptyList(),
    val error: String? = null
)

class ProjectViewModel : ViewModel() {
    private val repository = ProjectRepository()

    private val _uiState = MutableStateFlow(ProjectUiState())
    val uiState: StateFlow<ProjectUiState> = _uiState.asStateFlow()

    init {
        loadProjects()
    }

    fun loadProjects() {
        viewModelScope.launch {
            executeRequest {
                repository.getProjects()
            }?.let { projects ->
                _uiState.value = ProjectUiState(
                    isLoading = false,
                    projects = projects
                )
            }
        }
    }

    fun addProject(project: Project) {
        viewModelScope.launch {
            executeAction {
                repository.addProject(project)
            }
        }
    }

    fun modifyProject(project: Project) {
        viewModelScope.launch {
            executeAction {
                repository.modifyProject(project)
            }
        }
    }

    fun deleteProject(id: Int) {
        viewModelScope.launch {
            executeAction {
                repository.deleteProject(id)
            }
        }
    }

    private suspend fun executeAction(
        action: suspend () -> Unit
    ) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        try {
            action()

            val projects = repository.getProjects()

            _uiState.value = ProjectUiState(
                isLoading = false,
                projects = projects
            )
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = exception.message ?: "Operation failed"
            )
        }
    }

    private suspend fun executeRequest(
        request: suspend () -> List<Project>
    ): List<Project>? {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        return try {
            request()
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = exception.message ?: "Unable to load projects"
            )
            null
        }
    }
}