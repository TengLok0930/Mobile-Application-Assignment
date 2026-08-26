package com.example.fundforgoals.feature.member.profile.presentation

sealed interface MemberProfileAction {
    data object OnBackClick : MemberProfileAction
    data object OnLogoutClick : MemberProfileAction

    data object OnMessagesClick : MemberProfileAction
    data object OnHomeClick : MemberProfileAction
    data object OnProfileClick : MemberProfileAction

    data object OnViewContributionsClick : MemberProfileAction
    data object OnChangePasswordClick : MemberProfileAction

    data object OnToggleTheme : MemberProfileAction
    data object OnToggleNotifications : MemberProfileAction

    data object Refresh : MemberProfileAction
}