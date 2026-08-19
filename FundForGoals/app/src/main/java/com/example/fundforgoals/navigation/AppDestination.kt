package com.example.fundforgoals.navigation

sealed class AppDestination(val route: String) {
    data object Login : AppDestination("login")
    data object SignUpChoice : AppDestination("sign_up_choice")
    data object OrganisationSignUp : AppDestination("organisation_sign_up")

    data object MemberHome : AppDestination("member_home")
    data object OrganisationHome : AppDestination("organisation_home")
    data object AdminHome : AppDestination("admin_home")
    data object AdminRequest : AppDestination("admin_request")
    data object AdminProfile : AppDestination("admin_profile")
    data object Chat : AppDestination("chat")

    data object MemberProjectDetail : AppDestination("member_project_detail/{projectId}") {
        fun createRoute(projectId: String): String {
            return "member_project_detail/$projectId"
        }
    }

    data object OrganisationProjectDetail : AppDestination("organisation_project_detail/{projectId}") {
        fun createRoute(projectId: String): String {
            return "organisation_project_detail/$projectId"
        }
    }

    data object AdminMonitorDetail : AppDestination("admin_monitor_detail/{projectId}") {
        fun createRoute(projectId: String): String {
            return "admin_monitor_detail/$projectId"
        }
    }

    data object AdminWarningDetail : AppDestination("admin_warning_detail/{projectId}") {
        fun createRoute(projectId: String): String {
            return "admin_warning_detail/$projectId"
        }
    }
}