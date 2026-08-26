package com.example.fundforgoals.feature.auth.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginRoute(
    onBackClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful) {
            onLoginSuccess(uiState.userType)
            viewModel.onLoginNavigated()
        }
    }

    LoginScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                LoginAction.OnBackClick -> onBackClick()
                LoginAction.OnForgotPasswordClick -> onForgotPasswordClick()
                LoginAction.OnSignUpClick -> onSignUpClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}