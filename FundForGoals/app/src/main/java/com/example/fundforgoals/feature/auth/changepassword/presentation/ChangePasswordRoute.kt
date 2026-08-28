package com.example.fundforgoals.feature.auth.changepassword.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChangePasswordRoute(
    username: String,
    onBackClick: () -> Unit,
    onPasswordChanged: () -> Unit = {},
    viewModel: ChangePasswordViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(username) {
        viewModel.setLoggedInUsername(username)
    }

    ChangePasswordScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                ChangePasswordAction.OnBackClick -> onBackClick()
                ChangePasswordAction.OnDialogOkClick -> {
                    viewModel.onAction(action)
                    onPasswordChanged()
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}