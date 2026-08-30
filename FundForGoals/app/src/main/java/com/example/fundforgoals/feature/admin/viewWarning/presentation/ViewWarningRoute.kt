package com.example.fundforgoals.feature.admin.viewWarning.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ViewWarningRoute(
    viewModel: ViewWarningViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ViewWarningScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                ViewWarningAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}