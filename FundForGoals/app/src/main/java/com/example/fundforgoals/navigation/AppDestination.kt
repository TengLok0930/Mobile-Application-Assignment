package com.example.fundforgoals.navigation

sealed class AppDestination(val route: String) {
    data object Login : AppDestination("login")
    data object ForgotPassword : AppDestination("forgot_password")
    data object ChangePassword : AppDestination("change_password/{currentUser}") {
        fun createRoute(currentUser: String) = "change_password/$currentUser"
    }
    data object SignUpChoice : AppDestination("sign_up_choice")
    data object MemberSignUp : AppDestination("member_sign_up")
    data object OrganisationSignUp : AppDestination("organisation_sign_up")

    data object MemberHome : AppDestination("member_home/{currentUser}") {
        fun createRoute(currentUser: String) = "member_home/$currentUser"
    }

    data object MemberContribute : AppDestination("member_contribute/{currentUser}/{projectId}") {
        fun createRoute(currentUser: String, projectId: Int) = "member_contribute/$currentUser/$projectId"
    }

    data object MemberProfile : AppDestination("member_profile/{currentUser}") {
        fun createRoute(currentUser: String) = "member_profile/$currentUser"
    }
    data object MemberContributions : AppDestination("member_contributions")
    object OrganisationProfile : AppDestination("organisation_profile/{currentUser}")  {
        fun createRoute(currentUser: String) = "organisation_profile/$currentUser"
    }
    object OrganisationHome : AppDestination("organisation_home/{currentUser}") {
        fun createRoute(currentUser: String) = "organisation_home/$currentUser"
    }

    data object OrganisationCreateProject : AppDestination("organisation_create_project/{currentUser}") {
        fun createRoute(currentUser: String) = "organisation_create_project/$currentUser"
    }

    data object OrganisationViewProject : AppDestination("organisation_view_project/{currentUser}") {
        fun createRoute(currentUser: String) = "organisation_view_project/$currentUser"
    }
    data object OrganisationContribute : AppDestination("organisation_contribute/{currentUser}/{projectId}") {
        fun createRoute(currentUser: String, projectId: Int) = "organisation_contribute/$currentUser/$projectId"
    }
    object OrganisationPastProjects : AppDestination("organisation_past_projects")
    data object AdminHome : AppDestination("admin_home")
    data object AdminRequest : AppDestination("admin_request")
    data object AdminProfile : AppDestination("admin_profile")
    data object Chat : AppDestination("chat/{currentUser}") {
        fun createRoute(currentUser: String): String {
            return "chat/$currentUser"
        }
    }

    data object AdminChatroom : AppDestination("admin_chatroom/{currentUser}/{projectId}") {
        fun createRoute(currentUser: String, projectId: Int) = "admin_chatroom/$currentUser/$projectId"
    }

    data object MemberProjectDetail :
        AppDestination("member_project_detail/{projectId}") {

        fun createRoute(projectId: Int): String {
            return "member_project_detail/$projectId"
        }
    }

    data object OrganisationProjectDetail : AppDestination("organisation_project_detail/{projectId}") {
        fun createRoute(projectId: Int): String {
            return "organisation_project_detail/$projectId"
        }
    }

    data object OrganisationWarningList : AppDestination("organisation_warning_list/{projectId}") {
        fun createRoute(projectId: Int): String {
            return "organisation_warning_list/$projectId"
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