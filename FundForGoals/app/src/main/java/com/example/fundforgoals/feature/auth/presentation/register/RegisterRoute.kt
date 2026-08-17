package com.example.fundforgoals.feature.auth.presentation.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RegisterRoute(
    viewModel: RegisterViewModel,
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

    RegisterScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                RegisterAction.OnBackClick -> onBackClick()
                RegisterAction.OnLoginClick -> onLoginClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}
