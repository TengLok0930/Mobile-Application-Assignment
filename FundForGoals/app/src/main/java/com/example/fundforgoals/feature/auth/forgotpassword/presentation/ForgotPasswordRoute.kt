package com.example.fundforgoals.feature.auth.forgotpassword.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ForgotPasswordRoute(
    onBackClick: () -> Unit,
    onRequestSubmitted: () -> Unit = {},
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ForgotPasswordScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                ForgotPasswordAction.OnBackClick -> onBackClick()
                ForgotPasswordAction.OnDialogOkClick -> {
                    viewModel.onAction(action)
                    onRequestSubmitted()
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}