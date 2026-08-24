package com.example.fundforgoals.feature.member.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun MemberProfileRoute(
    viewModel: MemberProfileViewModel,
    onLogoutClick: () -> Unit,
    onMessagesClick: () -> Unit,
    onHomeClick: () -> Unit,
    onViewContributionsClick: () -> Unit,
    onChangePasswordClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()

    MemberProfileScreen(
        uiState = uiState,
        contentType = contentType,
        onAction = { action ->
            when (action) {
                MemberProfileAction.OnLogoutClick -> onLogoutClick()
                MemberProfileAction.OnMessagesClick -> onMessagesClick()
                MemberProfileAction.OnHomeClick -> onHomeClick()
                MemberProfileAction.OnViewContributionsClick -> onViewContributionsClick()
                MemberProfileAction.OnChangePasswordClick -> onChangePasswordClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}