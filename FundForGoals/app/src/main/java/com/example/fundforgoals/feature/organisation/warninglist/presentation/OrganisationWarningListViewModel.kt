package com.example.fundforgoals.feature.organisation.warninglist.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.Project
import com.example.fundforgoals.supabase.model.Warning
import com.example.fundforgoals.supabase.repository.ProjectRepository
import com.example.fundforgoals.supabase.repository.WarningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OrganisationWarningItemUi(
    val id: Int,
    val warningNumber: Int,
    val details: String,
    val createdAt: String
) {
    val title: String
        get() = "Warning #$warningNumber"
}

data class OrganisationWarningListUiState(
    val projectId: Int,
    val projectTitle: String = "",
    val warnings: List<OrganisationWarningItemUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class OrganisationWarningListViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val warningRepository = WarningRepository()
    private val projectRepository = ProjectRepository()

    private val projectId: Int = checkNotNull(savedStateHandle["projectId"])

    private val _uiState = MutableStateFlow(
        OrganisationWarningListUiState(
            projectId = projectId,
            isLoading = true
        )
    )
    val uiState: StateFlow<OrganisationWarningListUiState> = _uiState.asStateFlow()

    init {
        loadWarnings()
    }

    fun onAction(action: OrganisationWarningListAction) {
        when (action) {
            OrganisationWarningListAction.OnBackClick -> Unit
            OrganisationWarningListAction.OnRefreshClick -> loadWarnings()
            is OrganisationWarningListAction.OnWarningClick -> Unit
        }
    }

    private fun loadWarnings() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val project: Project = projectRepository.getProjectById(projectId)
                    ?: throw IllegalStateException("Project not found.")

                val warnings: List<Warning> = warningRepository
                    .getWarningsByProjectIds(listOf(projectId))
                    .sortedByDescending { it.createdAt }

                _uiState.update {
                    it.copy(
                        projectTitle = project.title,
                        warnings = warnings.mapIndexed { index, warning ->
                            OrganisationWarningItemUi(
                                id = warning.id ?: 0,
                                warningNumber = index + 1,
                                details = warning.details,
                                createdAt = warning.createdAt
                            )
                        },
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load warnings."
                    )
                }
            }
        }
    }
}