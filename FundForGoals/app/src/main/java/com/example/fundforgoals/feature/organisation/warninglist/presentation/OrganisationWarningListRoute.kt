package com.example.fundforgoals.feature.organisation.warninglist.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OrganisationWarningListRoute(
    viewModel: OrganisationWarningListViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OrganisationWarningListScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                OrganisationWarningListAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}