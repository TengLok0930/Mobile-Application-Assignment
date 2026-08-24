package com.example.fundforgoals.feature.admin.warning_detail.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AdminWarningDetailRoute(
    projectId: String,
    onBackClick: () -> Unit,
    onWarnOrganisationClick: () -> Unit,
    viewModel: AdminWarningDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(projectId) {
        viewModel.loadProject(projectId)
    }

    AdminWarningDetailScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                AdminWarningDetailAction.OnBackClick -> onBackClick()
                AdminWarningDetailAction.OnWarnOrganisationClick -> onWarnOrganisationClick()
            }
        }
    )
}