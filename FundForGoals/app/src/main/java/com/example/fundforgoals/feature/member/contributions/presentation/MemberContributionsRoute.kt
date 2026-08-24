package com.example.fundforgoals.feature.member.contributions.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MemberContributionsRoute(
    onBackClick: () -> Unit,
    onMessagesClick: () -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onContributionClick: (String) -> Unit,
    onECertClick: (String) -> Unit,
    viewModel: MemberContributionsViewModel = viewModel(),
    isCompact: Boolean = true
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MemberContributionsScreen(
        uiState = uiState,
        isCompact = isCompact,
        onAction = { action ->
            when (action) {
                MemberContributionsAction.OnBackClick -> onBackClick()
                MemberContributionsAction.OnMessagesClick -> onMessagesClick()
                MemberContributionsAction.OnHomeClick -> onHomeClick()
                MemberContributionsAction.OnProfileClick -> onProfileClick()
                is MemberContributionsAction.OnContributionClick -> {
                    onContributionClick(action.contributionId)
                }
                is MemberContributionsAction.OnECertClick -> {
                    onECertClick(action.contributionId)
                }
            }
        }
    )
}