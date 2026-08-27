package com.example.fundforgoals.feature.organisation.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun OrganisationProfileRoute(
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onMessagesClick: (currentUser: String) -> Unit,
    onHomeClick: () -> Unit,
    onViewPastProjectsClick: () -> Unit,
    onViewContributionsClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    viewModel: OrganisationProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()

    OrganisationProfileScreen(
        uiState = uiState,
        contentType = contentType,
        onAction = { action ->
            when (action) {
                OrganisationProfileAction.OnBackClick -> onBackClick()
                OrganisationProfileAction.OnLogoutClick -> onLogoutClick()
                OrganisationProfileAction.OnMessagesClick -> onMessagesClick(uiState.organisationName)
                OrganisationProfileAction.OnHomeClick -> onHomeClick()
                OrganisationProfileAction.OnViewPastProjectsClick -> onViewPastProjectsClick()
                OrganisationProfileAction.OnViewContributionsClick -> onViewContributionsClick()
                OrganisationProfileAction.OnChangePasswordClick -> onChangePasswordClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}