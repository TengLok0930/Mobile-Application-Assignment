package com.example.fundforgoals.feature.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ChatRoute(
    viewModel: ChatScreenViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChatScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                ChatScreenAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}