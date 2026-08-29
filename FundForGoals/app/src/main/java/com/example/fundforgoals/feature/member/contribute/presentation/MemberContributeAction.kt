package com.example.fundforgoals.feature.member.contribute.presentation

sealed interface MemberContributeAction {
    data class OnFundAmountChanged(val value: String) : MemberContributeAction
    data object OnSubmitClick : MemberContributeAction
    data object OnBackClick : MemberContributeAction
}