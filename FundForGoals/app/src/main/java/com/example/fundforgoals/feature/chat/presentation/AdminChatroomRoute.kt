package com.example.fundforgoals.feature.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fundforgoals.core.util.ContentType
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun AdminChatroomRoute(
    viewModel: ChatViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()
    val isCompact = contentType == ContentType.LIST_ONLY

    ChatScreen(
        uiState = uiState,
        onAction = { action -> viewModel.onAction(action) },
        onHomeClick = {},
        onProfileClick = {},
        onBackNavigate = onBackClick,
        isCompact = isCompact
    )
}