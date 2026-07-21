package com.example.fundforgoals.feature.member.project_detail.presentation

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