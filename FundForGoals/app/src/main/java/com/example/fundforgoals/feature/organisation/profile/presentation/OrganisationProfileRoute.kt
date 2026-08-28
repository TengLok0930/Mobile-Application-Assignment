package com.example.fundforgoals.feature.organisation.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun OrganisationProfileRoute(
    onLogoutClick: () -> Unit,
    onMessagesClick: () -> Unit,
    onHomeClick: () -> Unit,
    onViewContributionsClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onAppearanceClick: () -> Unit,
    isDarkTheme: Boolean,
    viewModel: OrganisationProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()

    LaunchedEffect(isDarkTheme) {
        viewModel.setDarkMode(isDarkTheme)
    }

    OrganisationProfileScreen(
        uiState = uiState,
        contentType = contentType,
        onAction = { action ->
            when (action) {
                OrganisationProfileAction.OnLogoutClick -> onLogoutClick()
                OrganisationProfileAction.OnMessagesClick -> onMessagesClick()
                OrganisationProfileAction.OnHomeClick -> onHomeClick()
                OrganisationProfileAction.OnViewContributionsClick -> onViewContributionsClick()
                OrganisationProfileAction.OnChangePasswordClick -> onChangePasswordClick()
                OrganisationProfileAction.OnToggleTheme -> onAppearanceClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}