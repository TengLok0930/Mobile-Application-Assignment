package com.example.fundforgoals.supabase.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.ProjectRequest
import com.example.fundforgoals.supabase.repository.ProjectRequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProjectRequestUiState(
    val isLoading: Boolean = false,
    val projectRequests: List<ProjectRequest> = emptyList(),
    val error: String? = null
)

class ProjectRequestViewModel : ViewModel() {

    private val repository = ProjectRequestRepository()

    private val _uiState = MutableStateFlow(ProjectRequestUiState())
    val uiState: StateFlow<ProjectRequestUiState> = _uiState.asStateFlow()

    init {
        loadProjectRequests()
    }

    fun loadProjectRequests() {
        viewModelScope.launch {
            executeRequest {
                repository.getProjectRequests()
            }?.let { projectRequests ->
                _uiState.value = ProjectRequestUiState(
                    isLoading = false,
                    projectRequests = projectRequests
                )
            }
        }
    }

    fun addProjectRequest(projectRequest: ProjectRequest) {
        viewModelScope.launch {
            executeAction {
                repository.addProjectRequest(projectRequest)
            }
        }
    }

    fun modifyProjectRequest(projectRequest: ProjectRequest) {
        viewModelScope.launch {
            executeAction {
                repository.modifyProjectRequest(projectRequest)
            }
        }
    }

    fun deleteProjectRequest(id: Int) {
        viewModelScope.launch {
            executeAction {
                repository.deleteProjectRequest(id)
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

            val projectRequests = repository.getProjectRequests()

            _uiState.value = ProjectRequestUiState(
                isLoading = false,
                projectRequests = projectRequests
            )
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = exception.message ?: "Operation failed"
            )
        }
    }

    private suspend fun executeRequest(
        request: suspend () -> List<ProjectRequest>
    ): List<ProjectRequest>? {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        return try {
            request()
        } catch (exception: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = exception.message ?: "Unable to load project requests"
            )
            null
        }
    }
}