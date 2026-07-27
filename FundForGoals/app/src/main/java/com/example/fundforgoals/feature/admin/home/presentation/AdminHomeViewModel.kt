package com.example.fundforgoals.feature.admin.home.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AdminHomeViewModel: ViewModel() {

    private val allProjects = listOf(
        AdminProjectUi(
            id = "1",
            title = "Project 1",
            organisation = "Organisation 1"
        ),
        AdminProjectUi(
            id = "2",
            title = "Project 2",
            organisation = "Organisation 1"
        ),
        AdminProjectUi(
            id = "3",
            title = "Project 3",
            organisation = "Organisation 2"
        ),
        AdminProjectUi(
            id = "4",
            title = "Project 4",
            organisation = "Organisation 2"
        ),
    )

    private val _uiState = MutableStateFlow(
        AdminHomeUiState(
            projects = allProjects
        )
    )
    
    val uiState: StateFlow<AdminHomeUiState> = _uiState.asStateFlow()

    fun onAction(action: AdminHomeAction) {
        when (action) {
            is AdminHomeAction.OnSearchQueryChanged -> {
                _uiState.update { currentState ->
                    val filteredProjects = allProjects.filter {
                        it.title.contains(action.value, ignoreCase = true) ||
                                it.organisation.contains(action.value, ignoreCase = true)
                    }

                    currentState.copy(
                        searchQuery = action.value,
                        projects = filteredProjects
                    )
                }
            }

            is AdminHomeAction.OnMonitorClick -> Unit
            AdminHomeAction.OnRequestClick -> Unit
            AdminHomeAction.OnHomeClick -> Unit
            AdminHomeAction.OnProfileClick -> Unit

            AdminHomeAction.Refresh -> {
                _uiState.update {
                    it.copy(projects = allProjects)
                }
            }
        }
    }
}