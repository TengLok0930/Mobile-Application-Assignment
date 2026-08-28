package com.example.fundforgoals.feature.auth.registration.member

sealed interface MemberRegAction {
    data class OnUsernameChanged(val value: String) : MemberRegAction
    data class OnSocialUrlChanged(val value: String) : MemberRegAction
    data class OnPasswordChanged(val value: String) : MemberRegAction
    data class OnConfirmPasswordChanged(val value: String) : MemberRegAction
    data object OnTogglePasswordVisibility : MemberRegAction
    data object OnToggleConfirmPasswordVisibility : MemberRegAction
    data object OnRegisterClick : MemberRegAction
    data object OnLoginClick : MemberRegAction
    data object OnBackClick : MemberRegAction
}