package com.example.fundforgoals.feature.organisation.contribute.presentation

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

data class OrganisationContributeUiState(
    val currentUser: String,
    val selectedProjectId: Int,
    val project: Project? = null,
    val creatorName: String = "",
    val currentFund: Double = 0.0,
    val fundAmountInput: String = "",
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val submitSuccess: Boolean = false
)

class OrganisationContributeViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val projectRepository = ProjectRepository()
    private val userRepository = UserRepository()
    private val contributorRepository = ContributorRepository()

    private val currentUser: String =
        checkNotNull(savedStateHandle["currentUser"])

    private val selectedProjectId: Int =
        checkNotNull(savedStateHandle["projectId"])

    private val _uiState = MutableStateFlow(
        OrganisationContributeUiState(
            currentUser = currentUser,
            selectedProjectId = selectedProjectId
        )
    )
    val uiState: StateFlow<OrganisationContributeUiState> = _uiState.asStateFlow()

    init {
        loadProject()
    }

    private fun loadProject() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                val project = projectRepository.getProjectById(selectedProjectId)
                    ?: error("Project not found")

                val creatorName = userRepository.getUserById(project.createdBy)?.name.orEmpty()

                val currentFund = contributorRepository
                    .getTotalFundsByProjectIds(listOf(selectedProjectId))[selectedProjectId]
                    ?: 0.0

                Triple(project, creatorName, currentFund)
            }.onSuccess { (project, creatorName, currentFund) ->
                _uiState.update {
                    it.copy(
                        project = project,
                        creatorName = creatorName,
                        currentFund = currentFund,
                        isLoading = false
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to load project"
                    )
                }
            }
        }
    }

    fun onAction(action: OrganisationContributeAction) {
        when (action) {
            is OrganisationContributeAction.OnFundAmountChanged -> {
                _uiState.update {
                    it.copy(fundAmountInput = action.value, errorMessage = null)
                }
            }

            OrganisationContributeAction.OnSubmitClick -> submitContribution()

            OrganisationContributeAction.OnBackClick -> Unit
        }
    }

    private fun submitContribution() {
        val state = _uiState.value
        val project = state.project ?: return

        val amount = state.fundAmountInput.toDoubleOrNull()

        if (amount == null || amount <= 0.0) {
            _uiState.update {
                it.copy(
                    errorMessage = if (amount == null) {
                        "Enter a valid amount"
                    } else {
                        "Amount must be more than RM 0.00"
                    }
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

            runCatching {
                val liveFund = contributorRepository
                    .getTotalFundsByProjectIds(listOf(project.id ?: error("Project has no id")))[project.id]
                    ?: 0.0

                val newTotal = liveFund + amount

                if (newTotal > project.fundGoal) {
                    error("Amount exceeds the remaining goal of RM %.2f".format(project.fundGoal - liveFund))
                }

                val user = userRepository.getUserByUsername(currentUser)
                    ?: error("User not found")
                val userId = user.id ?: error("User has no id")
                val projectId = project.id ?: error("Project has no id")

                contributorRepository.addContributor(
                    userId = userId,
                    projectId = projectId,
                    fundAmount = amount
                )

                val updatedProject = if (newTotal >= project.fundGoal) {
                    val updated = project.copy(status = "Past", hasCert = true)
                    projectRepository.modifyProject(updated)
                    updated
                } else {
                    project
                }

                updatedProject to newTotal
            }.onSuccess { (updatedProject, newTotal) ->
                _uiState.update {
                    it.copy(
                        project = updatedProject,
                        currentFund = newTotal,
                        fundAmountInput = "",
                        isSubmitting = false,
                        submitSuccess = true
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = exception.message ?: "Failed to submit contribution"
                    )
                }
            }
        }
    }
}