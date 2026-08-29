package com.example.fundforgoals.feature.member.contribute.presentation

import com.example.fundforgoals.supabase.model.Project

data class MemberContributeUiState(
    val currentUser: String,
    val selectedProjectId: Int,
    val projects: List<Project> = emptyList(),
    val creatorNames: Map<Int, String> = emptyMap(),
    val fundAmount: Double
)

