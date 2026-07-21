package com.example.fundforgoals.feature.member.project_detail.presentation

sealed interface MemberProjectDetailAction {
    data object OnBackClick : MemberProjectDetailAction
    data object OnContributeClick : MemberProjectDetailAction
}