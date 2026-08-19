package com.example.fundforgoals.feature.auth.registration.signup_choice.presentation

import androidx.compose.runtime.Composable

@Composable
fun SignUpChoiceRoute(
    onBackClick: () -> Unit,
    onMemberClick: () -> Unit,
    onOrganisationClick: () -> Unit
) {
    SignUpChoiceScreen(
        uiState = SignUpChoiceUiState(),
        onAction = { action ->
            when (action) {
                SignUpChoiceAction.OnBackClick -> onBackClick()
                SignUpChoiceAction.OnMemberClick -> onMemberClick()
                SignUpChoiceAction.OnOrganisationClick -> onOrganisationClick()
            }
        }
    )
}