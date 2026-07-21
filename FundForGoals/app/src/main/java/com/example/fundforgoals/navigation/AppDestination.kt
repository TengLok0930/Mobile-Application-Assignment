package com.example.fundforgoals.navigation

sealed class AppDestination(val route: String) {
    data object Login : AppDestination("login")
    data object MemberHome : AppDestination("member_home")
    data object Chat : AppDestination("chat")
    data object MemberProjectDetail : AppDestination("member_project_detail/{projectId}") {
        fun createRoute(projectId: String): String {
            return "member_project_detail/$projectId"
        }
    }
}