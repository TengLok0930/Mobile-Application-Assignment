package com.example.fundforgoals.feature.auth.registration.signup_choice.presentation

sealed interface SignUpChoiceAction {
    data object OnBackClick : SignUpChoiceAction
    data object OnMemberClick : SignUpChoiceAction
    data object OnOrganisationClick : SignUpChoiceAction
}