package com.example.fundforgoals.feature.organisation.profile.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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