package com.example.fundforgoals.feature.auth.registration.member

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fundforgoals.feature.auth.registration.member.presentation.MemberRegAction

@Composable
fun MemberRegRoute(
    viewModel: MemberRegViewModel,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterSuccess: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isRegisterSuccessful) {
        if (uiState.isRegisterSuccessful) {
            onRegisterSuccess(uiState.username)
            viewModel.onRegisterNavigated()
        }
    }

    MemberRegScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                MemberRegAction.OnBackClick -> onBackClick()
                MemberRegAction.OnLoginClick -> onLoginClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}