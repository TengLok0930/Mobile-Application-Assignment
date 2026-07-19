package com.example.fundforgoals.feature.member.project_detail.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MemberProjectDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MemberProjectDetailUiState(isLoading = true))
    val uiState: StateFlow<MemberProjectDetailUiState> = _uiState.asStateFlow()

    fun loadProject(projectId: String) {
        _uiState.value = MemberProjectDetailUiState(
            isLoading = false,
            project = ProjectDetailUi(
                id = projectId,
                title = "Project $projectId",
                organisation = "Organisation $projectId",
                description = "This project supports fundraising goals and community impact.",
                contributionAmount = "1250",
                progress = 0.74f
            )
        )
    }
}