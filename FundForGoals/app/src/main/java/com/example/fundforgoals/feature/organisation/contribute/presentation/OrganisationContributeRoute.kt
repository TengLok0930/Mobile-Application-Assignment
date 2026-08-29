package com.example.fundforgoals.feature.organisation.contribute.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OrganisationContributeRoute(
    viewModel: OrganisationContributeViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.submitSuccess) {
        if (uiState.submitSuccess) {
            onBackClick()
        }
    }

    OrganisationContributeScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                OrganisationContributeAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}