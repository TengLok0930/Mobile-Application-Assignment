package com.example.fundforgoals.feature.organisation.profile.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class OrganisationPastProjectUi(
    val id: String,
    val title: String,
    val contributionAmountText: String = ""
)

data class OrganisationProfileUiState(
    val isLoading: Boolean = false,
    val organisationName: String = "Organisation 1",
    val appearanceLabel: String = "Dark",
    val notificationsLabel: String = "On",
    val pastProjects: List<OrganisationPastProjectUi> = emptyList(),
    val totalContributionsText: String = "$0",
    val errorMessage: String? = null
)

class OrganisationProfileViewModel : ViewModel() {

    private val samplePastProjects = listOf(
        OrganisationPastProjectUi(
            id = "1",
            title = "Project 5",
            contributionAmountText = "$200 contributed"
        ),
        OrganisationPastProjectUi(
            id = "2",
            title = "Project 4",
            contributionAmountText = "$150 contributed"
        )
    )

    private val _uiState = MutableStateFlow(
        OrganisationProfileUiState(
            organisationName = "Organisation 1",
            appearanceLabel = "Dark",
            notificationsLabel = "On",
            pastProjects = samplePastProjects,
            totalContributionsText = "$350"
        )
    )
    val uiState: StateFlow<OrganisationProfileUiState> = _uiState.asStateFlow()

    fun setAppearanceLabel(isDarkTheme: Boolean) {
        _uiState.update { current ->
            current.copy(
                appearanceLabel = if (isDarkTheme) "Dark" else "Light"
            )
        }
    }

    fun onAction(action: OrganisationProfileAction) {
        when (action) {
            OrganisationProfileAction.OnAppearanceClick -> Unit

            OrganisationProfileAction.OnNotificationsClick -> {
                _uiState.update { current ->
                    current.copy(
                        notificationsLabel = if (current.notificationsLabel == "On") "Off" else "On"
                    )
                }
            }

            OrganisationProfileAction.OnBackClick -> Unit
            OrganisationProfileAction.OnLogoutClick -> Unit
            OrganisationProfileAction.OnMessagesClick -> Unit
            OrganisationProfileAction.OnHomeClick -> Unit
            OrganisationProfileAction.OnProfileClick -> Unit
            OrganisationProfileAction.OnViewPastProjectsClick -> Unit
            OrganisationProfileAction.OnViewContributionsClick -> Unit
            OrganisationProfileAction.OnChangePasswordClick -> Unit
        }
    }
}