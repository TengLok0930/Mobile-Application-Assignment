package com.example.fundforgoals.feature.member.home.presentation

sealed interface MemberHomeAction {
    data class OnSearchQueryChanged(val value: String) : MemberHomeAction
    data class OnProjectClick(val projectId: Int) : MemberHomeAction
    data object OnMessagesClick : MemberHomeAction
    data object OnHomeClick : MemberHomeAction
    data object OnProfileClick : MemberHomeAction
    data object Refresh : MemberHomeAction
}