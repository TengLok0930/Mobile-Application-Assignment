package com.example.fundforgoals.feature.admin.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun AdminHomeRoute(
    viewModel: AdminHomeViewModel,
    onRequestClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()

    AdminHomeScreen(
        uiState = uiState,
        contentType = contentType,
        onAction = { action ->
            when (action) {
                AdminHomeAction.OnRequestClick -> onRequestClick()
                AdminHomeAction.OnProfileClick -> onProfileClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}