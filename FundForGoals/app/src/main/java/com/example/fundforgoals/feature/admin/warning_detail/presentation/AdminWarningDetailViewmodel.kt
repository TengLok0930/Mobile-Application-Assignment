package com.example.fundforgoals.feature.admin.warning_detail.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminWarningDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AdminWarningDetailUiState(isLoading = true))
    val uiState: StateFlow<AdminWarningDetailUiState> = _uiState.asStateFlow()

    fun loadProject(projectId: String) {
        _uiState.value = AdminWarningDetailUiState(
            isLoading = false,
            project = AdminWarningDetailUi(
                id = projectId,
                title = "Project $projectId",
                organisation = "Organisation $projectId",
                incidentTitle = "What happened to Project $projectId",
                warningDetails = "A warning was raised because the project timeline slipped, recent updates were incomplete, and the organisation did not respond clearly to the latest moderation concerns."
            )
        )
    }
}