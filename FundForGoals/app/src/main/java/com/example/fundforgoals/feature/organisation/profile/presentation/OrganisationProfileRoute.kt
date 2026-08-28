package com.example.fundforgoals.feature.organisation.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    onAppearanceClick: () -> Unit,
    isDarkTheme: Boolean,
    viewModel: OrganisationProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()

    LaunchedEffect(isDarkTheme) {
        viewModel.setAppearanceLabel(isDarkTheme)
    }

    OrganisationProfileScreen(
        uiState = uiState,
        contentType = contentType,
        onAction = { action ->
            when (action) {
                OrganisationProfileAction.OnBackClick -> onBackClick()
                OrganisationProfileAction.OnLogoutClick -> onLogoutClick()
                OrganisationProfileAction.OnMessagesClick -> { /* existing logic */ }
                OrganisationProfileAction.OnHomeClick -> onHomeClick()
                OrganisationProfileAction.OnViewPastProjectsClick -> onViewPastProjectsClick()
                OrganisationProfileAction.OnViewContributionsClick -> onViewContributionsClick()
                OrganisationProfileAction.OnChangePasswordClick -> onChangePasswordClick()
                OrganisationProfileAction.OnAppearanceClick -> onAppearanceClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}