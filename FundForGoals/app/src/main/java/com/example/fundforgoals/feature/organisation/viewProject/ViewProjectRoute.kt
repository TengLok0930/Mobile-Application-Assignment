package com.example.fundforgoals.feature.organisation.viewProject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fundforgoals.core.util.ContentType
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun ViewProjectRoute(
    viewModel: ViewProjectViewModel,
    onProjectSelected: (Int) -> Unit,
    onHomeClick: () -> Unit,
    onMessagesClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()
    val isCompact = contentType == ContentType.LIST_ONLY

    ViewProjectScreen(
        uiState = uiState,
        isCompact = isCompact,
        onAction = { action ->
            when (action) {
                is ViewProjectAction.OnProjectClick -> {
                    if (isCompact) {
                        onProjectSelected(action.projectId)
                    } else {
                        viewModel.onAction(action)
                    }
                }

                ViewProjectAction.OnMessagesClick -> onMessagesClick()
                ViewProjectAction.OnProfileClick -> onProfileClick()
                ViewProjectAction.OnHomeClick -> onHomeClick()
                is ViewProjectAction.OnSearchQueryChanged -> viewModel.onAction(action)
                ViewProjectAction.Refresh -> viewModel.onAction(action)
            }
        }
    )
}