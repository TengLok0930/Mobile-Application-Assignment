package com.example.fundforgoals.feature.admin.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fundforgoals.core.util.ContentType
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun AdminHomeRoute(
    viewModel: AdminHomeViewModel,
    onRequestClick: () -> Unit,
    onProfileClick: () -> Unit,
    onMonitorClick: (projectId: Int) -> Unit,
    onWarnProjectClick: (projectId: Int) -> Unit,
    onViewChatroomClick: (projectId: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()
    val isCompact = contentType == ContentType.LIST_ONLY

    AdminHomeScreen(
        uiState = uiState,
        isCompact = isCompact,
        onWarnProjectClick = onWarnProjectClick,
        onViewChatroomClick = onViewChatroomClick,
        onAction = { action ->
            when (action) {
                AdminHomeAction.OnRequestClick -> onRequestClick()
                AdminHomeAction.OnProfileClick -> onProfileClick()

                is AdminHomeAction.OnMonitorClick -> {
                    if (isCompact) {
                        onMonitorClick(action.projectId)
                    } else {
                        viewModel.onAction(action)
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}