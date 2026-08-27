package com.example.fundforgoals.feature.organisation.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fundforgoals.core.util.ContentType
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun OrganisationHomeRoute(
    viewModel: OrganisationHomeViewModel,
    onProjectSelected: (Int) -> Unit,
    onViewProjectClick: () -> Unit,
    onMessagesClick: (currentUser: String) -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()
    val isCompact = contentType == ContentType.LIST_ONLY

    OrganisationHomeScreen(
        uiState = uiState,
        isCompact = isCompact,
        onAction = { action ->
            when (action) {
                is OrganisationHomeAction.OnProjectClick -> {
                    if (isCompact) {
                        onProjectSelected(action.projectId)
                    } else {
                        viewModel.onAction(action)
                    }
                }

                OrganisationHomeAction.OnViewProjectClick -> onViewProjectClick()
                OrganisationHomeAction.OnNewProjectClick -> viewModel.onAction(action)
                OrganisationHomeAction.OnMessagesClick -> onMessagesClick(uiState.currentUser)
                OrganisationHomeAction.OnProfileClick -> onProfileClick()
                is OrganisationHomeAction.OnSearchQueryChanged -> viewModel.onAction(action)
                OrganisationHomeAction.OnHomeClick -> viewModel.onAction(action)
                OrganisationHomeAction.Refresh -> viewModel.onAction(action)
            }
        }
    )
}