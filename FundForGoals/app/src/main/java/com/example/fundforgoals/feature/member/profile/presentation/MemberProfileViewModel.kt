package com.example.fundforgoals.feature.member.profile.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.supabase.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

<<<<<<< Updated upstream
class MemberProfileViewModel : ViewModel() {
=======
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

    private val currentUser: String =
        checkNotNull(savedStateHandle["currentUser"])
>>>>>>> Stashed changes

    private val ongoingItems = listOf(
        MemberContributionUi(
            id = "1",
            projectTitle = "Clean Water Project",
            organisationName = "Helping Hands",
            amountText = "RM 50.00",
            isOngoing = true,
            hasECertificate = false
        ),
        MemberContributionUi(
            id = "2",
            projectTitle = "School Supplies Drive",
            organisationName = "Care For All",
            amountText = "RM 35.00",
            isOngoing = true,
            hasECertificate = false
        )
    )

    private val pastItems = listOf(
        MemberContributionUi(
            id = "3",
            projectTitle = "Food Relief Mission",
            organisationName = "Kindness Hub",
            amountText = "RM 100.00",
            isOngoing = false,
            hasECertificate = true
        ),
        MemberContributionUi(
            id = "4",
            projectTitle = "Flood Recovery Fund",
            organisationName = "Relief Network",
            amountText = "RM 75.00",
            isOngoing = false,
            hasECertificate = true
        )
    )

    private val _uiState = MutableStateFlow(
        MemberProfileUiState(
            currentUser = currentUser,
            ongoingContributions = ongoingItems,
            pastContributions = pastItems,
            isDarkMode = false,
            notificationsEnabled = true,
            isLoading = false
        )
    )
    val uiState: StateFlow<MemberProfileUiState> = _uiState.asStateFlow()

<<<<<<< Updated upstream
=======
    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

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

    fun setDarkMode(isDarkTheme: Boolean) {
        _uiState.update { current ->
            current.copy(isDarkMode = isDarkTheme)
        }
    }

>>>>>>> Stashed changes
    fun onAction(action: MemberProfileAction) {
        when (action) {
            MemberProfileAction.OnBackClick -> Unit
            MemberProfileAction.OnLogoutClick -> Unit
            MemberProfileAction.OnMessagesClick -> Unit
            MemberProfileAction.OnHomeClick -> Unit
            MemberProfileAction.OnProfileClick -> Unit
            MemberProfileAction.OnViewContributionsClick -> Unit
            MemberProfileAction.OnChangePasswordClick -> Unit
<<<<<<< Updated upstream

            MemberProfileAction.OnToggleTheme -> {
                _uiState.update { current ->
                    current.copy(isDarkMode = !current.isDarkMode)
                }
            }
=======
            MemberProfileAction.OnToggleTheme -> Unit
>>>>>>> Stashed changes

            MemberProfileAction.OnToggleNotifications -> {
                _uiState.update { current ->
                    current.copy(notificationsEnabled = !current.notificationsEnabled)
                }
            }

            MemberProfileAction.Refresh -> {
                loadProfile()
                _uiState.update {
                    it.copy(
                        ongoingContributions = ongoingItems,
                        pastContributions = pastItems
                    )
                }
            }
        }
    }
}