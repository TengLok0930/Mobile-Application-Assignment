package com.example.fundforgoals.feature.member.project_detail.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProjectDetailUi(
    val id: String,
    val title: String,
    val organisation: String,
    val description: String,
    val contributionAmount: String,
    val progress: Float
)

data class MemberProjectDetailUiState(
    val isLoading: Boolean = false,
    val project: ProjectDetailUi? = null,
    val errorMessage: String? = null
)

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