package com.example.fundforgoals.feature.admin.home.presentation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fundforgoals.core.util.ContentType
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun AdminHomeRoute(
    viewModel: AdminHomeViewModel,
    onRequestClick: () -> Unit,
    onMonitorProjectClick: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()
    val isCompact = contentType == ContentType.LIST_ONLY

    AdminHomeScreen(
        uiState = uiState,
        isCompact = isCompact,
        onAction = { action ->
            when (action) {
                is AdminHomeAction.OnSearchQueryChanged -> viewModel.onAction(action)
                is AdminHomeAction.OnMonitorClick -> onMonitorProjectClick(action.projectId)

                AdminHomeAction.OnRequestClick -> onRequestClick()
                AdminHomeAction.OnHomeClick -> viewModel.onAction(action)
                AdminHomeAction.OnProfileClick -> onProfileClick()
                AdminHomeAction.Refresh -> viewModel.onAction(action)
            }
        }
    )
}