package com.example.fundforgoals.feature.member.profile.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.model.Contributor
import com.example.fundforgoals.supabase.model.Project
import com.example.fundforgoals.supabase.model.User
import com.example.fundforgoals.supabase.repository.ContributorRepository
import com.example.fundforgoals.supabase.repository.ProjectRepository
import com.example.fundforgoals.supabase.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

data class MemberContributionUi(
    val id: String,
    val projectId: Int,
    val projectTitle: String,
    val organisationName: String,
    val amount: Double,
    val amountText: String = "",
    val isOngoing: Boolean = false,
    val hasECertificate: Boolean = false
)

data class MemberProfileUiState(
    val currentUser: String,
    val memberAvatar: String = "",
    val memberName: String = "",
    val ongoingContributions: List<MemberContributionUi> = emptyList(),
    val pastContributions: List<MemberContributionUi> = emptyList(),
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class MemberProfileViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userRepository = UserRepository()
    private val contributorRepository = ContributorRepository()
    private val projectRepository = ProjectRepository()

    private val currentUser: String =
        checkNotNull(savedStateHandle["currentUser"])

    private val _uiState = MutableStateFlow(
        MemberProfileUiState(currentUser = currentUser)
    )
    val uiState: StateFlow<MemberProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val user = userRepository.getUserByUsername(currentUser)
                    ?: throw IllegalStateException("User not found.")

                val userId = user.id
                    ?: throw IllegalStateException("User ID not found.")

                val contributors = contributorRepository.getContributorsByUserId(userId)
                val contributionItems = buildContributionUi(contributors)

                val (ongoing, past) = contributionItems.partition { it.isOngoing }

                _uiState.update {
                    it.copy(
                        memberName = user.name,
                        memberAvatar = user.avatarUrl,
                        ongoingContributions = ongoing,
                        pastContributions = past,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to load profile"
                    )
                }
            }
        }
    }

    private suspend fun buildContributionUi(
        contributors: List<Contributor>
    ): List<MemberContributionUi> {
        if (contributors.isEmpty()) return emptyList()

        val projectIds = contributors.map { it.project }.distinct()
        val projects = projectRepository.getProjectsByIds(projectIds)
        val projectMap: Map<Int, Project> = projects.mapNotNull { project ->
            project.id?.let { it to project }
        }.toMap()

        val creatorIds = projects.map { it.createdBy }.distinct()
        val creators = userRepository.getUsersByIds(creatorIds)
        val creatorMap: Map<Int, User> = creators.mapNotNull { creator ->
            creator.id?.let { it to creator }
        }.toMap()

        return contributors.mapNotNull { contributor ->
            val project = projectMap[contributor.project] ?: return@mapNotNull null
            val organisationName = creatorMap[project.createdBy]?.name ?: "Unknown organisation"
            val projectStatus = project.status.orEmpty()

            MemberContributionUi(
                id = (contributor.id ?: 0).toString(),
                projectId = contributor.project,
                projectTitle = project.title,
                organisationName = organisationName,
                amount = contributor.fundAmount,
                amountText = "RM %,.2f".format(Locale.US, contributor.fundAmount),
                isOngoing = projectStatus == "Ongoing",
                hasECertificate = project.hasCert && projectStatus != "Ongoing"
            )
        }
    }

    fun setDarkMode(isDarkTheme: Boolean) {
        _uiState.update { it.copy(isDarkMode = isDarkTheme) }
    }

    fun onAction(action: MemberProfileAction) {
        when (action) {
            MemberProfileAction.OnBackClick -> Unit
            MemberProfileAction.OnLogoutClick -> Unit
            MemberProfileAction.OnMessagesClick -> Unit
            MemberProfileAction.OnHomeClick -> Unit
            MemberProfileAction.OnProfileClick -> Unit
            MemberProfileAction.OnViewContributionsClick -> Unit
            MemberProfileAction.OnChangePasswordClick -> Unit
            MemberProfileAction.OnToggleTheme -> Unit

            MemberProfileAction.OnToggleNotifications -> {
                _uiState.update { current ->
                    current.copy(notificationsEnabled = !current.notificationsEnabled)
                }
            }

            MemberProfileAction.Refresh -> {
                loadProfile()
            }
        }
    }
}