package com.example.fundforgoals.feature.member.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fundforgoals.core.util.rememberContentType

@Composable
fun MemberProfileRoute(
    viewModel: MemberProfileViewModel,
    onLogoutClick: () -> Unit,
    onMessagesClick: (currentUser: String) -> Unit,
    onHomeClick: () -> Unit,
    onViewContributionsClick: () -> Unit,
    onAppearanceClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    isDarkTheme: Boolean
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentType = rememberContentType()

    LaunchedEffect(isDarkTheme) {
        viewModel.setDarkMode(isDarkTheme)
    }

    MemberProfileScreen(
        uiState = uiState,
        contentType = contentType,
        onAction = { action ->
            when (action) {
                MemberProfileAction.OnLogoutClick -> onLogoutClick()
                MemberProfileAction.OnMessagesClick -> { /* keep your existing logic */ }
                MemberProfileAction.OnHomeClick -> onHomeClick()
                MemberProfileAction.OnViewContributionsClick -> onViewContributionsClick()
                MemberProfileAction.OnChangePasswordClick -> onChangePasswordClick()
                MemberProfileAction.OnToggleTheme -> onAppearanceClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}