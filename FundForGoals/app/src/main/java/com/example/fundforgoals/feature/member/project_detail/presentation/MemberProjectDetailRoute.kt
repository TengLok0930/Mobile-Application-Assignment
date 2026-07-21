package com.example.fundforgoals.feature.member.project_detail.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MemberProjectDetailRoute(
    projectId: String,
    onBackClick: () -> Unit,
    onContributeClick: () -> Unit,
    viewModel: MemberProjectDetailViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(projectId) {
        viewModel.loadProject(projectId)
    }

    MemberProjectDetailScreen(
        uiState = uiState.value,
        onAction = { action ->
            when (action) {
                MemberProjectDetailAction.OnBackClick -> onBackClick()
                MemberProjectDetailAction.OnContributeClick -> onContributeClick()
            }
        }
    )
}