package com.example.fundforgoals.feature.admin.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AdminProfileRoute(
    onRequestsClick: () -> Unit,
    onHomeClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: AdminProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdminProfileScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                AdminProfileAction.OnRequestsClick -> onRequestsClick()
                AdminProfileAction.OnHomeClick -> onHomeClick()
                AdminProfileAction.OnChangePasswordClick -> onChangePasswordClick()
                AdminProfileAction.OnLogoutClick -> onLogoutClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}