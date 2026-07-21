package com.example.fundforgoals.feature.member.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MemberHomeRoute(
    viewModel: MemberHomeViewModel,
    onProjectSelected: (String) -> Unit,
    onMessagesClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MemberHomeScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                is MemberHomeAction.OnProjectClick -> onProjectSelected(action.projectId)
                MemberHomeAction.OnMessagesClick -> onMessagesClick()
                MemberHomeAction.OnProfileClick -> onProfileClick()
                is MemberHomeAction.OnSearchQueryChanged -> viewModel.onAction(action)
                MemberHomeAction.OnHomeClick -> viewModel.onAction(action)
                MemberHomeAction.Refresh -> viewModel.onAction(action)
            }
        }
    )
}