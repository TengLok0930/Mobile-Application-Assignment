package com.example.fundforgoals.feature.admin.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun AdminProfileRoute(
    onRequestsClick: () -> Unit,
    onHomeClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onAppearanceClick: () -> Unit,
    isDarkTheme: Boolean,
    viewModel: AdminProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()

    LaunchedEffect(isDarkTheme) {
        viewModel.setAppearanceLabel(isDarkTheme)
    }

    AdminProfileScreen(
        uiState = uiState,
        contentType = contentType,
        onAction = { action ->
            when (action) {
                AdminProfileAction.OnRequestsClick -> onRequestsClick()
                AdminProfileAction.OnHomeClick -> onHomeClick()
                AdminProfileAction.OnLogoutClick -> onLogoutClick()
                AdminProfileAction.OnAppearanceClick -> onAppearanceClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}