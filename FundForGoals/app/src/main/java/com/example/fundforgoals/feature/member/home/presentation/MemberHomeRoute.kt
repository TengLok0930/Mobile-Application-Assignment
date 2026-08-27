package com.example.fundforgoals.feature.member.home.presentation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fundforgoals.core.util.ContentType
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun MemberHomeRoute(
    viewModel: MemberHomeViewModel,
    onProjectSelected: (String) -> Unit,
    onMessagesClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()
    val isCompact = contentType == ContentType.LIST_ONLY

    MemberHomeScreen(
        uiState = uiState,
        isCompact = isCompact,
        onAction = { action ->
            when (action) {
                is MemberHomeAction.OnProjectClick -> {
                    if (isCompact) {
                        onProjectSelected(action.projectId)
                    } else {
                        viewModel.onAction(action)
                    }
                }

                MemberHomeAction.OnMessagesClick -> onMessagesClick()
                MemberHomeAction.OnProfileClick -> onProfileClick()
                is MemberHomeAction.OnSearchQueryChanged -> viewModel.onAction(action)
                MemberHomeAction.OnHomeClick -> viewModel.onAction(action)
                MemberHomeAction.Refresh -> viewModel.onAction(action)
            }
        }
    )
}