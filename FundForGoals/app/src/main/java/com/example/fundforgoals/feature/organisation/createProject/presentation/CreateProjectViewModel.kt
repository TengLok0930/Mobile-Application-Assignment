package com.example.fundforgoals.feature.organisation.createProject.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.Project
import com.example.fundforgoals.supabase.model.ProjectRequest
import com.example.fundforgoals.supabase.repository.ProjectRepository
import com.example.fundforgoals.supabase.repository.ProjectRequestRepository
import com.example.fundforgoals.supabase.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateProjectUiState(
    val currentUser: String = "",
    val title: String = "",
    val description: String = "",
    val goal: String = "",
    val isLoading: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val errorMessage: String? = null
) {
    val isSubmitEnabled: Boolean
        get() = currentUser.isNotBlank() &&
                title.isNotBlank() &&
                description.isNotBlank() &&
                goal.isNotBlank() &&
                !isLoading
}

class CreateProjectViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val projectRepository = ProjectRepository()
    private val projectRequestRepository = ProjectRequestRepository()
    private val _uiState = MutableStateFlow(CreateProjectUiState())
    val uiState: StateFlow<CreateProjectUiState> = _uiState.asStateFlow()

    fun setCurrentUser(currentUser: String) {
        if (_uiState.value.currentUser == currentUser) return
        _uiState.update { it.copy(currentUser = currentUser) }
    }

    fun onAction(action: CreateProjectAction) {
        when (action) {
            CreateProjectAction.OnBackClick,
            CreateProjectAction.OnMessagesClick,
            CreateProjectAction.OnHomeClick,
            CreateProjectAction.OnProfileClick -> Unit

            is CreateProjectAction.OnTitleChange -> {
                _uiState.update {
                    it.copy(
                        title = action.value,
                        errorMessage = null,
                        showSuccessDialog = false
                    )
                }
            }

            is CreateProjectAction.OnDescriptionChange -> {
                _uiState.update {
                    it.copy(
                        description = action.value,
                        errorMessage = null,
                        showSuccessDialog = false
                    )
                }
            }

            is CreateProjectAction.OnGoalChange -> {
                val filtered = action.value.filter { char -> char.isDigit() }
                _uiState.update {
                    it.copy(
                        goal = filtered,
                        errorMessage = null,
                        showSuccessDialog = false
                    )
                }
            }

            CreateProjectAction.OnSubmitClick -> {
                createProject()
            }

            CreateProjectAction.OnDismissDialog,
            CreateProjectAction.OnDialogOkClick -> {
                _uiState.update {
                    it.copy(showSuccessDialog = false)
                }
            }
        }
    }

    private fun createProject() {
        val state = _uiState.value
        val goalAmount = state.goal.toDoubleOrNull()

        when {
            state.currentUser.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Current user not found.") }
                return
            }

            state.title.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Title is required.") }
                return
            }

            state.description.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Description is required.") }
                return
            }

            goalAmount == null || goalAmount <= 0.0 -> {
                _uiState.update { it.copy(errorMessage = "Goal must be a valid amount.") }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val user = userRepository.getUserByUsername(state.currentUser)
                    ?: throw IllegalStateException("User not found.")

                val createdProject = projectRepository.addProject(
                    Project(
                        id = null,
                        createdAt = "",
                        title = state.title.trim(),
                        desc = state.description.trim(),
                        createdBy = user.id ?: throw IllegalStateException("User ID not found."),
                        fundGoal = goalAmount,
                        avatarUrl = "https://gravatar.com/avatar/98a43d9c65d2d88226e4173c6a35c859?s=400&d=identicon&r=x",
                        status = "Pending",
                        hasCert = false
                    )
                )

                val createdProjectId = createdProject.id
                    ?: throw IllegalStateException("Created project ID not found.")

                val requestDetails = """
                    Project creation request for ${createdProject.title}
                    
                    Description:
                    ${state.description.trim()}
                """.trimIndent()

                projectRequestRepository.addProjectRequest(
                    ProjectRequest(
                        id = null,
                        createdAt = "",
                        details = requestDetails,
                        aiOverview = null,
                        status = "pending",
                        projectId = createdProjectId,
                        project = null
                    )
                )

                _uiState.update {
                    it.copy(
                        title = "",
                        description = "",
                        goal = "",
                        isLoading = false,
                        showSuccessDialog = true,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to create project."
                    )
                }
            }
        }
    }
}