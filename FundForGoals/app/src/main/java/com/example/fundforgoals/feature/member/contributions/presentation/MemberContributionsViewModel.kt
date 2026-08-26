package com.example.fundforgoals.feature.member.contributions.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MemberContributionsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        MemberContributionsUiState(
            isLoading = false,
            memberName = "Member 1",
            ongoingContributions = listOf(
                MemberContributionUi(
                    id = "ongoing_1",
                    projectTitle = "Project 1",
                    organisationName = "Organisation 1",
                    amountText = "$50",
                    isOngoing = true,
                    hasECertificate = false
                ),
                MemberContributionUi(
                    id = "ongoing_2",
                    projectTitle = "Project 2",
                    organisationName = "Organisation 1",
                    amountText = "$35",
                    isOngoing = true,
                    hasECertificate = false
                )
            ),
            pastContributions = listOf(
                MemberContributionUi(
                    id = "past_1",
                    projectTitle = "Project 5",
                    organisationName = "Organisation 3",
                    amountText = "$20",
                    isOngoing = false,
                    hasECertificate = true
                )
            )
        )
    )
    val uiState: StateFlow<MemberContributionsUiState> = _uiState.asStateFlow()
}