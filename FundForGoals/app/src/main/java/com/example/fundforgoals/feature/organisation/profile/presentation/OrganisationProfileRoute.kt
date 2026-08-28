package com.example.fundforgoals.feature.organisation.profile.presentation

import androidx.compose.runtime.Composable
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
<<<<<<< Updated upstream
    viewModel: OrganisationProfileViewModel = viewModel()
=======
    onAppearanceClick: () -> Unit,
    isDarkTheme: Boolean,
    viewModel: OrganisationProfileViewModel
>>>>>>> Stashed changes
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()

<<<<<<< Updated upstream
=======
    LaunchedEffect(isDarkTheme) {
        viewModel.setDarkMode(isDarkTheme)
    }

>>>>>>> Stashed changes
    OrganisationProfileScreen(
        uiState = uiState,
        contentType = contentType,
        onAction = { action ->
            when (action) {
                OrganisationProfileAction.OnLogoutClick -> onLogoutClick()
<<<<<<< Updated upstream
                OrganisationProfileAction.OnMessagesClick -> onMessagesClick(uiState.organisationName)
=======
                OrganisationProfileAction.OnMessagesClick -> onMessagesClick()
>>>>>>> Stashed changes
                OrganisationProfileAction.OnHomeClick -> onHomeClick()
                OrganisationProfileAction.OnViewContributionsClick -> onViewContributionsClick()
                OrganisationProfileAction.OnChangePasswordClick -> onChangePasswordClick()
<<<<<<< Updated upstream
=======
                OrganisationProfileAction.OnToggleTheme -> onAppearanceClick()
>>>>>>> Stashed changes
                else -> viewModel.onAction(action)
            }
        }
    )
}