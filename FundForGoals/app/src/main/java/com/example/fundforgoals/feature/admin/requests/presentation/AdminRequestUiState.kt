package com.example.fundforgoals.feature.admin.requests.presentation

enum class AdminRequestType {
    ORGANISATION,
    USER,
    PROJECT
}

data class AdminRequestCategoryUi(
    val type: AdminRequestType,
    val title: String,
    val count: Int,
    val buttonText: String
)

data class AdminRequestItemUi(
    val id: String,
    val username: String,
    val subtitle: String,
    val aiSummary: String
)

data class AdminRequestUiState(
    val categories: List<AdminRequestCategoryUi> = emptyList(),
    val selectedType: AdminRequestType? = null,
    val requestItems: List<AdminRequestItemUi> = emptyList(),
    val selectedRequestId: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val selectedRequest: AdminRequestItemUi?
        get() = requestItems.find { it.id == selectedRequestId }
}