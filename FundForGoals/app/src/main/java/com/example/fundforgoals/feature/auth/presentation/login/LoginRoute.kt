package com.example.fundforgoals.feature.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    onBackClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                LoginAction.OnBackClick -> onBackClick()
                LoginAction.OnForgotPasswordClick -> onForgotPasswordClick()
                LoginAction.OnSignUpClick -> onSignUpClick()
                LoginAction.OnLoginClick -> {
                    viewModel.onAction(action)
                    onLoginSuccess()
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}