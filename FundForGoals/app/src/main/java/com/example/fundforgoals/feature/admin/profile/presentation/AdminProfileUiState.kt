package com.example.fundforgoals.feature.admin.profile.presentation

data class AdminProfileUiState(
    val isLoading: Boolean = false,
    val adminName: String = "Administrator",
    val appearanceLabel: String = "Dark",
    val notificationsLabel: String = "On",
    val errorMessage: String? = null
)