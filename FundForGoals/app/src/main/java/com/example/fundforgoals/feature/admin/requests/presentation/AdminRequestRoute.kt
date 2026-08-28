package com.example.fundforgoals.feature.admin.requests.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fundforgoals.core.util.ContentType
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun AdminRequestRoute(
    viewModel: AdminRequestViewModel,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()
    val isCompact = contentType == ContentType.LIST_ONLY

    AdminRequestScreen(
        uiState = uiState,
        isCompact = isCompact,
        onAction = { action ->
            when (action) {
                AdminRequestAction.OnHomeClick -> onHomeClick()
                AdminRequestAction.OnProfileClick -> onProfileClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}