package com.example.fundforgoals.feature.admin.monitor_detail.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AdminMonitorDetailRoute(
    projectId: String,
    onBackClick: () -> Unit,
    onCancelProjectClick: () -> Unit,
    onWarnProjectClick: () -> Unit,
    onViewChatroomClick: () -> Unit,
    viewModel: AdminMonitorDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(projectId) {
        viewModel.loadProject(projectId)
    }

    AdminMonitorDetailScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                AdminMonitorDetailAction.OnBackClick -> onBackClick()
                AdminMonitorDetailAction.OnCancelProjectClick -> onCancelProjectClick()
                AdminMonitorDetailAction.OnWarnProjectClick -> onWarnProjectClick()
                AdminMonitorDetailAction.OnViewChatroomClick -> onViewChatroomClick()
            }
        }
    )
}