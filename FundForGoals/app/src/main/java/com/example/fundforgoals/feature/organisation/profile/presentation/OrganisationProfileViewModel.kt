package com.example.fundforgoals.feature.organisation.profile.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.repository.MemberContributionData
import com.example.fundforgoals.supabase.repository.MemberContributionRepository
import com.example.fundforgoals.supabase.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OrganisationContributionUi(
    val id: String,
    val projectTitle: String,
    val organisationName: String,
    val amountText: String = "",
    val isOngoing: Boolean = false,
    val hasECertificate: Boolean = false
)

data class OrganisationProfileUiState(
    val currentUser: String,
    val organisationAvatar: String = "",
    val organisationName: String = "",
    val ongoingContributions: List<OrganisationContributionUi> = emptyList(),
    val pastContributions: List<OrganisationContributionUi> = emptyList(),
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class OrganisationProfileViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userRepository = UserRepository()
    private val contributionRepository = MemberContributionRepository()

    private val currentUser: String =
        checkNotNull(savedStateHandle["currentUser"])

    private val _uiState = MutableStateFlow(
        OrganisationProfileUiState(currentUser = currentUser)
    )
    val uiState: StateFlow<OrganisationProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                userRepository.getUserByUsername(currentUser)
                    ?: error("User not found")
            }.onSuccess { user ->
                _uiState.update {
                    it.copy(
                        organisationName = user.name,
                        organisationAvatar = user.avatarUrl,
                        isLoading = false
                    )
                }
                loadContributions(userId = user.id ?: return@onSuccess)
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to load profile"
                    )
                }
            }
        }
    }

    private fun loadContributions(userId: Int) {
        viewModelScope.launch {
            runCatching {
                contributionRepository.getContributionsForUser(userId)
            }.onSuccess { contributions ->
                val (ongoing, past) = contributions.partition { it.isOngoing }
                _uiState.update {
                    it.copy(
                        ongoingContributions = ongoing.toUi(),
                        pastContributions = past.toUi()
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(errorMessage = exception.message ?: "Failed to load contributions")
                }
            }
        }
    }

    private fun List<MemberContributionData>.toUi(): List<OrganisationContributionUi> = map { data ->
        OrganisationContributionUi(
            id = data.contributorId.toString(),
            projectTitle = data.projectTitle,
            organisationName = data.organisationName,
            amountText = "RM %.2f".format(data.fundAmount),
            isOngoing = data.isOngoing,
            hasECertificate = data.hasECertificate
        )
    }

    fun setDarkMode(isDarkTheme: Boolean) {
        _uiState.update { it.copy(isDarkMode = isDarkTheme) }
    }

    fun onAction(action: OrganisationProfileAction) {
        when (action) {
            OrganisationProfileAction.OnBackClick -> Unit
            OrganisationProfileAction.OnLogoutClick -> Unit
            OrganisationProfileAction.OnMessagesClick -> Unit
            OrganisationProfileAction.OnHomeClick -> Unit
            OrganisationProfileAction.OnProfileClick -> Unit
            OrganisationProfileAction.OnViewContributionsClick -> Unit
            OrganisationProfileAction.OnChangePasswordClick -> Unit
            OrganisationProfileAction.OnToggleTheme -> Unit

            OrganisationProfileAction.OnToggleNotifications -> {
                _uiState.update { current ->
                    current.copy(notificationsEnabled = !current.notificationsEnabled)
                }
            }

            OrganisationProfileAction.Refresh -> {
                loadProfile()
            }
        }
    }
}