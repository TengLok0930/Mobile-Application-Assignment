package com.example.fundforgoals.feature.member.contributions.presentation

sealed interface MemberContributionsAction {
    data object OnBackClick : MemberContributionsAction
    data object OnMessagesClick : MemberContributionsAction
    data object OnHomeClick : MemberContributionsAction
    data object OnProfileClick : MemberContributionsAction
    data class OnContributionClick(val contributionId: String) : MemberContributionsAction
    data class OnECertClick(val contributionId: String) : MemberContributionsAction
}