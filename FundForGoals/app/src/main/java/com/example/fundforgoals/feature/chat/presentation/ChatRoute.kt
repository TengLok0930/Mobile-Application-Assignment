package com.example.fundforgoals.feature.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ChatRoute(
    viewModel: ChatViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChatScreen(
        uiState = uiState,
        onAction = { action ->
            if (action == ChatAction.OnBackClick) {
                onBackClick()
            } else {
                viewModel.onAction(action)
            }
        }
    )
}