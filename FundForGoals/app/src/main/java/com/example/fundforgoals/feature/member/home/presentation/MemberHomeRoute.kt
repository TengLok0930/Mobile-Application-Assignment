package com.example.fundforgoals.feature.member.home.presentation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fundforgoals.core.util.ContentType
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun MemberHomeRoute(
    viewModel: MemberHomeViewModel,
    onProjectSelected: (Int) -> Unit,
    onMessagesClick: (currentUserId: String) -> Unit,
    onProfileClick: () -> Unit,
    onContributeClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()
    val isCompact = contentType == ContentType.LIST_ONLY
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onAction(MemberHomeAction.Refresh)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    MemberHomeScreen(
        uiState = uiState,
        isCompact = isCompact,
        onContributeClick = onContributeClick,
        onAction = { action ->
            when (action) {
                is MemberHomeAction.OnProjectClick -> {
                    if (isCompact) {
                        onProjectSelected(action.projectId)
                    } else {
                        viewModel.onAction(action)
                    }
                }

                MemberHomeAction.OnMessagesClick -> onMessagesClick(uiState.currentUser)
                MemberHomeAction.OnProfileClick -> onProfileClick()
                is MemberHomeAction.OnSearchQueryChanged -> viewModel.onAction(action)
                MemberHomeAction.OnHomeClick -> viewModel.onAction(action)
                MemberHomeAction.Refresh -> viewModel.onAction(action)
            }
        }
    )
}