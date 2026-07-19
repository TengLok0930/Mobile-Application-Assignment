package com.example.fundforgoals.feature.member.home.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MemberHomeViewModel : ViewModel() {

    private val allProjects = listOf(
        ProjectUi(
            id = "1",
            title = "Project 1",
            organisation = "Organisation 1",
            description = "Description 1"
        ),
        ProjectUi(
            id = "2",
            title = "Project 2",
            organisation = "Organisation 2",
            description = "Description 2"
        )
    )

    private val _uiState = MutableStateFlow(
        MemberHomeUiState(
            projects = allProjects
        )
    )
    val uiState: StateFlow<MemberHomeUiState> = _uiState.asStateFlow()

    fun onAction(action: MemberHomeAction) {
        when (action) {
            is MemberHomeAction.OnSearchQueryChanged -> {
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

            is MemberHomeAction.OnProjectClick -> {
            }

            MemberHomeAction.OnMessagesClick -> {
            }

            MemberHomeAction.OnHomeClick -> {
            }

            MemberHomeAction.OnProfileClick -> {
            }

            MemberHomeAction.Refresh -> {
                _uiState.update {
                    it.copy(projects = allProjects)
                }
            }
        }
    }
}