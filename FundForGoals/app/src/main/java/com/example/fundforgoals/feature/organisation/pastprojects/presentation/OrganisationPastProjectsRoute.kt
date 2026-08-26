package com.example.fundforgoals.feature.organisation.pastprojects.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun OrganisationPastProjectsRoute(
    onBackClick: () -> Unit,
    onMessagesClick: () -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    viewModel: OrganisationPastProjectsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()

    OrganisationPastProjectsScreen(
        uiState = uiState,
        contentType = contentType,
        onAction = { action ->
            when (action) {
                OrganisationPastProjectsAction.OnBackClick -> onBackClick()
                OrganisationPastProjectsAction.OnMessagesClick -> onMessagesClick()
                OrganisationPastProjectsAction.OnHomeClick -> onHomeClick()
                OrganisationPastProjectsAction.OnProfileClick -> onProfileClick()
            }
        }
    )
}