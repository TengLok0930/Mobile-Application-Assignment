package com.example.fundforgoals.feature.member.profile.presentation

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

data class MemberContributionUi(
    val id: String,
    val projectTitle: String,
    val organisationName: String,
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
    private val memberContributionRepository = MemberContributionRepository()

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
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                userRepository.getUserByUsername(currentUser)
                    ?: error("User not found")
            }.onSuccess { user ->
                _uiState.update {
                    it.copy(
                        memberName = user.name,
                        memberAvatar = user.avatarUrl,
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
                memberContributionRepository.getContributionsForUser(userId)
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

    private fun List<MemberContributionData>.toUi(): List<MemberContributionUi> = map { data ->
        MemberContributionUi(
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