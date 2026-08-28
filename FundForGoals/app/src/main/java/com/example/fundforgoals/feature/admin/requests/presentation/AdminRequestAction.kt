package com.example.fundforgoals.feature.admin.requests.presentation

sealed interface AdminRequestAction {
    data class OnCategoryClick(val type: AdminRequestType) : AdminRequestAction
    data class OnRequestClick(val requestId: String): AdminRequestAction

    data object OnBackFromCategoryClick: AdminRequestAction
    data object OnBackFromDetailClick: AdminRequestAction

    data object OnAcceptRequestClick: AdminRequestAction
    data object OnRejectRequestClick: AdminRequestAction

    data object OnRequestsClick: AdminRequestAction
    data object OnHomeClick: AdminRequestAction
    data object OnProfileClick: AdminRequestAction
}