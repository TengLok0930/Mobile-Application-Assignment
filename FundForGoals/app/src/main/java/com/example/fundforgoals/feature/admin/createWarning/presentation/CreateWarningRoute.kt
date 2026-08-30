package com.example.fundforgoals.feature.admin.createWarning.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CreateWarningRoute(
    viewModel: CreateWarningViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.submitSuccess) {
        if (uiState.submitSuccess) {
            onBackClick()
        }
    }

    CreateWarningScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                CreateWarningAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}