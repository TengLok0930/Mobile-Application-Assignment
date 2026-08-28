package com.example.fundforgoals.feature.auth.registration.organisation.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OrganisationRegRoute(
    viewModel: OrganisationRegViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OrganisationRegScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                OrganisationRegAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}