package com.example.fundforgoals.feature.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fundforgoals.core.util.ContentType
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun ChatRoute(
    viewModel: ChatViewModel,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()
    val isCompact = contentType == ContentType.LIST_ONLY

    ChatScreen(
        uiState = uiState,
        isCompact = isCompact,
        onHomeClick = onHomeClick,
        onProfileClick = onProfileClick,
        onAction = { action -> viewModel.onAction(action) }
    )
}