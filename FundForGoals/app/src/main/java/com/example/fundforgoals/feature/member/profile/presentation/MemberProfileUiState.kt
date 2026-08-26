package com.example.fundforgoals.feature.member.profile.presentation

data class MemberContributionUi(
    val id: String,
    val projectTitle: String,
    val organisationName: String,
    val amountText: String = "",
    val isOngoing: Boolean = false,
    val hasECertificate: Boolean = false
)

data class MemberProfileUiState(
    val memberName: String = "",
    val ongoingContributions: List<MemberContributionUi> = emptyList(),
    val pastContributions: List<MemberContributionUi> = emptyList(),
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)