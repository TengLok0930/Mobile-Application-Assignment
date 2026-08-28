package com.example.fundforgoals.feature.organisation.profile.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundforgoals.feature.member.profile.presentation.MemberContributionUi
import com.example.fundforgoals.supabase.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

<<<<<<< Updated upstream
class OrganisationProfileViewModel : ViewModel() {
=======
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
>>>>>>> Stashed changes

    private val userRepository = UserRepository()

    private val currentUser: String =
        checkNotNull(savedStateHandle["currentUser"])

    private val ongoingItems = listOf(
        OrganisationContributionUi(
            id = "1",
            projectTitle = "Clean Water Project",
            organisationName = "Helping Hands",
            amountText = "RM 50.00",
            isOngoing = true,
            hasECertificate = false
        ),
        OrganisationContributionUi(
            id = "2",
            projectTitle = "School Supplies Drive",
            organisationName = "Care For All",
            amountText = "RM 35.00",
            isOngoing = true,
            hasECertificate = false
        )
    )

    private val pastItems = listOf(
        OrganisationContributionUi(
            id = "3",
            projectTitle = "Food Relief Mission",
            organisationName = "Kindness Hub",
            amountText = "RM 100.00",
            isOngoing = false,
            hasECertificate = true
        ),
        OrganisationContributionUi(
            id = "4",
            projectTitle = "Flood Recovery Fund",
            organisationName = "Relief Network",
            amountText = "RM 75.00",
            isOngoing = false,
            hasECertificate = true
        )
    )

    private val _uiState = MutableStateFlow(
        OrganisationProfileUiState(
            currentUser = currentUser,
            ongoingContributions = ongoingItems,
            pastContributions = pastItems,
            isDarkMode = false,
            notificationsEnabled = true,
            isLoading = false
        )
    )
    val uiState: StateFlow<OrganisationProfileUiState> = _uiState.asStateFlow()

<<<<<<< Updated upstream
    fun onAction(action: OrganisationProfileAction) {
        when (action) {
            OrganisationProfileAction.OnAppearanceClick -> {
                _uiState.update { current ->
                    current.copy(
                        appearanceLabel = if (current.appearanceLabel == "Dark") "Light" else "Dark"
                    )
                }
            }

            OrganisationProfileAction.OnNotificationsClick -> {
                _uiState.update { current ->
                    current.copy(
                        notificationsLabel = if (current.notificationsLabel == "On") "Off" else "On"
                    )
                }
            }

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
                        organisationName = user.name,
                        organisationAvatar = user.avatarUrl,
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

    fun onAction(action: OrganisationProfileAction) {
        when (action) {
>>>>>>> Stashed changes
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