package com.example.fundforgoals.feature.member.profile.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MemberContributionUi(
    val id: String,
    val projectTitle: String,
    val organisationName: String,
    val amountText: String = "",
    val isOngoing: Boolean = false,
    val hasECertificate: Boolean = false
)

data class MemberProfileUiState(
    val memberName: String = "",
    val ongoingContributions: List<MemberContributionUi> = emptyList(),
    val pastContributions: List<MemberContributionUi> = emptyList(),
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class MemberProfileViewModel : ViewModel() {

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
            memberName = "Member Name",
            ongoingContributions = ongoingItems,
            pastContributions = pastItems,
            isDarkMode = false,
            notificationsEnabled = true,
            isLoading = false
        )
    )
    val uiState: StateFlow<MemberProfileUiState> = _uiState.asStateFlow()

    fun setDarkMode(isDarkTheme: Boolean) {
        _uiState.update { current ->
            current.copy(isDarkMode = isDarkTheme)
        }
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
                _uiState.update {
                    it.copy(
                        memberName = "Member Name",
                        ongoingContributions = ongoingItems,
                        pastContributions = pastItems,
                        errorMessage = null
                    )
                }
            }
        }
    }
}