package com.example.fundforgoals.feature.admin.monitor_detail.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AdminMonitorDetailUi(
    val id: String,
    val title: String,
    val organisation: String,
    val overview: String,
    val warningCount: Int,
    val incidentTitle: String,
    val warningDetails: String
)

data class AdminMonitorDetailUiState(
    val isLoading: Boolean = false,
    val project: AdminMonitorDetailUi? = null,
    val errorMessage: String? = null
)
class AdminMonitorDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AdminMonitorDetailUiState(isLoading = true))
    val uiState: StateFlow<AdminMonitorDetailUiState> = _uiState.asStateFlow()

    fun loadProject(projectId: String) {
        _uiState.value = AdminMonitorDetailUiState(
            isLoading = false,
            project = AdminMonitorDetailUi(
                id = projectId,
                title = "Project $projectId",
                organisation = "Organisation $projectId",
                overview = "This project requires admin monitoring due to recent activity and warning reports. Review the details and decide whether admin intervention is needed.",
                warningCount = 1,
                incidentTitle = "What happened to Project $projectId",
                warningDetails = "A warning was raised because the project timeline slipped and recent updates from the organisation were incomplete."
            )
        )
    }
}