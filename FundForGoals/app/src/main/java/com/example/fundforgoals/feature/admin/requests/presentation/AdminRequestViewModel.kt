package com.example.fundforgoals.feature.admin.requests.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AdminRequestViewModel: ViewModel() {

    private val organisationRequests = listOf(
        AdminRequestItemUi(
            id = "org_1",
            username = "OrgUser1",
            subtitle = "New organisation creation",
            aiSummary = "This organisation request asks for approval to create a new organisation profile in the system."
        )
    )

    private val userRequests = listOf(
        AdminRequestItemUi(
            id = "user_1",
            username = "Username 1",
            subtitle = "Change name",
            aiSummary = "This user submitted a request to change the display name associated with the account."
        ),
        AdminRequestItemUi(
            id = "user_2",
            username = "Username 2",
            subtitle = "Creation",
            aiSummary = "This user creation request requires admin review before the account becomes active."
        )
    )

    private val projectRequests = emptyList<AdminRequestItemUi>()

    private val categories = listOf(
        AdminRequestCategoryUi(
            type = AdminRequestType.ORGANISATION,
            title = if (organisationRequests.isEmpty()) "No requests..." else "Incoming",
            count = organisationRequests.size,
            buttonText = "View organisation requests"
        ),
        AdminRequestCategoryUi(
            type = AdminRequestType.USER,
            title = if (userRequests.isEmpty()) "No requests..." else "Incoming",
            count = userRequests.size,
            buttonText = "View user requests"
        ),
        AdminRequestCategoryUi(
            type = AdminRequestType.PROJECT,
            title = if (projectRequests.isEmpty()) "No requests..." else "Incoming",
            count = projectRequests.size,
            buttonText = "View project requests"
        )
    )
    
    private val _uiState = MutableStateFlow(
        AdminRequestUiState(
            categories = categories,
            selectedType = null,
            requestItems = emptyList(),
            selectedRequestId = null
        )
    )
    val uiState: StateFlow<AdminRequestUiState> = _uiState.asStateFlow()

    fun onAction(action: AdminRequestAction) {
        when (action) {
            is AdminRequestAction.OnCategoryClick -> {
                val items = when (action.type) {
                    AdminRequestType.USER -> userRequests
                    AdminRequestType.ORGANISATION -> organisationRequests
                    AdminRequestType.PROJECT -> projectRequests
                }

                _uiState.update {
                    it.copy(
                        selectedType = action.type,
                        requestItems = items,
                        selectedRequestId = null
                    )
                }
            }

            is AdminRequestAction.OnRequestClick -> {
                _uiState.update {
                    it.copy(selectedRequestId = action.requestId)
                }
            }

            AdminRequestAction.OnBackFromCategoryClick -> {
                _uiState.update {
                    it.copy(
                        selectedType = null,
                        requestItems = emptyList(),
                        selectedRequestId = null
                    )
                }
            }

            AdminRequestAction.OnBackFromDetailClick -> {
                _uiState.update {
                    it.copy(selectedRequestId = null)
                }
            }

            AdminRequestAction.OnAcceptRequestClick -> {
                val currentSelectedId = _uiState.value.selectedRequestId
                if (currentSelectedId == null) return

                val updatedItems = _uiState.value.requestItems.filterNot { it.id == currentSelectedId }

                _uiState.update { currentState ->
                    val selectedType = currentState.selectedType
                    val updatedCategories = currentState.categories.map { category ->
                        if (category.type == selectedType) {
                            val newCount = updatedItems.size
                            category.copy(
                                count = newCount,
                                title = if (newCount == 0) "No requests..." else "Incoming"
                            )
                        } else {
                            category
                        }
                    }

                    currentState.copy(
                        categories = updatedCategories,
                        requestItems = updatedItems,
                        selectedRequestId = null
                    )
                }
            }

            AdminRequestAction.OnRejectRequestClick -> {
                val currentSelectedId = _uiState.value.selectedRequestId
                if (currentSelectedId == null) return

                val updatedItems = _uiState.value.requestItems.filterNot { it.id == currentSelectedId }

                _uiState.update { currentState ->
                    val selectedType = currentState.selectedType
                    val updatedCategories = currentState.categories.map { category ->
                        if (category.type == selectedType) {
                            val newCount = updatedItems.size
                            category.copy(
                                count = newCount,
                                title = if (newCount == 0) "No requests..." else "Incoming"
                            )
                        } else {
                            category
                        }
                    }

                    currentState.copy(
                        categories = updatedCategories,
                        requestItems = updatedItems,
                        selectedRequestId = null
                    )
                }
            }

            AdminRequestAction.OnRequestsClick -> Unit
            AdminRequestAction.OnHomeClick -> Unit
            AdminRequestAction.OnProfileClick -> Unit
        }
    }
}