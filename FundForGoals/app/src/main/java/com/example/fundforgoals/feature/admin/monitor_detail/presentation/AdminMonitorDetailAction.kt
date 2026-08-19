package com.example.fundforgoals.feature.admin.monitor_detail.presentation

sealed interface AdminMonitorDetailAction {
    data object OnBackClick : AdminMonitorDetailAction
    data object OnCancelProjectClick : AdminMonitorDetailAction
    data object OnWarnProjectClick : AdminMonitorDetailAction
    data object OnViewChatroomClick : AdminMonitorDetailAction
}