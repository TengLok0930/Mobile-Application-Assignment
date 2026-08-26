package com.example.fundforgoals.feature.member.contributions.presentation

data class MemberContributionUi(
    val id: String,
    val projectTitle: String,
    val organisationName: String,
    val amountText: String,
    val isOngoing: Boolean,
    val hasECertificate: Boolean
)

data class MemberContributionsUiState(
    val isLoading: Boolean = false,
    val memberName: String = "",
    val ongoingContributions: List<MemberContributionUi> = emptyList(),
    val pastContributions: List<MemberContributionUi> = emptyList(),
    val errorMessage: String? = null
)