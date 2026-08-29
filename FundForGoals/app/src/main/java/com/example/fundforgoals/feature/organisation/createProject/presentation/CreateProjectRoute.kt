package com.example.fundforgoals.feature.organisation.createProject.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundforgoals.core.util.ContentType
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun CreateProjectRoute(
    currentUser: String,
    onBackClick: () -> Unit,
    onProjectCreated: () -> Unit = {},
    onMessagesClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    viewModel: CreateProjectViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()
    val isCompact = contentType == ContentType.LIST_ONLY

    LaunchedEffect(currentUser) {
        viewModel.setCurrentUser(currentUser)
    }

    CreateProjectScreen(
        uiState = uiState,
        isCompact = isCompact,
        onAction = { action ->
            when (action) {
                CreateProjectAction.OnBackClick -> onBackClick()

                CreateProjectAction.OnDialogOkClick -> {
                    viewModel.onAction(action)
                    onProjectCreated()
                }

                CreateProjectAction.OnMessagesClick -> onMessagesClick(currentUser)
                CreateProjectAction.OnHomeClick -> onHomeClick()
                CreateProjectAction.OnProfileClick -> onProfileClick()

                else -> viewModel.onAction(action)
            }
        }
    )
}