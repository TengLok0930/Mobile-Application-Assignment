package com.example.fundforgoals.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fundforgoals.feature.admin.home.presentation.AdminHomeRoute
import com.example.fundforgoals.feature.admin.home.presentation.AdminHomeViewModel
import com.example.fundforgoals.feature.admin.monitor_detail.presentation.AdminMonitorDetailRoute
import com.example.fundforgoals.feature.admin.monitor_detail.presentation.AdminMonitorDetailViewModel
import com.example.fundforgoals.feature.admin.profile.presentation.AdminProfileRoute
import com.example.fundforgoals.feature.admin.requests.presentation.AdminRequestRoute
import com.example.fundforgoals.feature.admin.requests.presentation.AdminRequestViewModel
import com.example.fundforgoals.feature.admin.warning_detail.presentation.AdminWarningDetailRoute
import com.example.fundforgoals.feature.admin.warning_detail.presentation.AdminWarningDetailViewModel
import com.example.fundforgoals.feature.auth.login.presentation.LoginRoute
import com.example.fundforgoals.feature.auth.login.presentation.LoginViewModel
import com.example.fundforgoals.feature.auth.registration.member.MemberRegRoute
import com.example.fundforgoals.feature.auth.registration.member.MemberRegViewModel
import com.example.fundforgoals.feature.auth.registration.organisation.presentation.OrganisationRegRoute
import com.example.fundforgoals.feature.auth.registration.organisation.presentation.OrganisationRegViewModel
import com.example.fundforgoals.feature.auth.registration.signup_choice.presentation.SignUpChoiceRoute
import com.example.fundforgoals.feature.chat.presentation.ChatRoute
import com.example.fundforgoals.feature.chat.presentation.ChatViewModel
import com.example.fundforgoals.feature.member.contributions.presentation.MemberContributionsRoute
import com.example.fundforgoals.feature.member.home.presentation.MemberHomeRoute
import com.example.fundforgoals.feature.member.home.presentation.MemberHomeViewModel
import com.example.fundforgoals.feature.member.profile.presentation.MemberProfileRoute
import com.example.fundforgoals.feature.member.profile.presentation.MemberProfileViewModel
import com.example.fundforgoals.feature.member.project_detail.presentation.MemberProjectDetailRoute
import com.example.fundforgoals.feature.organisation.home.presentation.OrganisationHomeRoute
import com.example.fundforgoals.feature.organisation.home.presentation.OrganisationHomeViewModel
import com.example.fundforgoals.feature.organisation.pastprojects.presentation.OrganisationPastProjectsRoute
import com.example.fundforgoals.feature.organisation.profile.presentation.OrganisationProfileRoute
import com.example.fundforgoals.feature.organisation.viewProject.ViewProjectRoute
import com.example.fundforgoals.feature.organisation.viewProject.ViewProjectViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Login.route,
        modifier = modifier
    ) {
        composable(route = AppDestination.Login.route) {
            val loginViewModel: LoginViewModel = viewModel()

            LoginRoute(
                viewModel = loginViewModel,
                onForgotPasswordClick = {
                    // Navigate to forgot password screen
                },
                onSignUpClick = {
                    navController.navigate(AppDestination.SignUpChoice.route) {
                        launchSingleTop = true
                    }
                },
                onLoginSuccess = { username, userType ->
                    val destination = when {
                        userType.trim().equals("admin", ignoreCase = true) ->
                            AppDestination.AdminHome.route
                        userType.trim().equals("organisation", ignoreCase = true) ->
                            AppDestination.OrganisationHome.createRoute(username)
                        userType.trim().equals("member", ignoreCase = true) ->
                            AppDestination.MemberHome.createRoute(username)
                        else -> ""
                    }

                    if (destination != "") {
                        navController.navigate(destination) {
                            popUpTo(AppDestination.Login.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(route = AppDestination.SignUpChoice.route) {
            SignUpChoiceRoute(
                onBackClick = {
                    navController.popBackStack()
                },
                onMemberClick = {
                    navController.navigate(AppDestination.MemberSignUp.route) {
                        launchSingleTop = true
                    }
                },
                onOrganisationClick = {
                    navController.navigate(AppDestination.OrganisationSignUp.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = AppDestination.MemberSignUp.route) {
            MemberRegRoute(
                viewModel = viewModel<MemberRegViewModel>(),
                onBackClick = {
                    navController.popBackStack()
                },
                onLoginClick = {
                    navController.navigate(AppDestination.Login.route) {
                        launchSingleTop = true
                    }
                },
                onRegisterSuccess = {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(AppDestination.Login.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = AppDestination.OrganisationSignUp.route) {
            OrganisationRegRoute(
                viewModel = viewModel<OrganisationRegViewModel>(),
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = AppDestination.MemberHome.route,
            arguments = listOf(
                navArgument("currentUser") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val currentUser = backStackEntry.arguments?.getString("currentUser") ?: return@composable

            MemberHomeRoute(
                viewModel = viewModel<MemberHomeViewModel>(),
                onProjectSelected = { projectId ->
                    navController.navigate(
                        AppDestination.MemberProjectDetail.createRoute(projectId)
                    )
                },
                onMessagesClick = { currentUser ->
                    navController.navigate(AppDestination.Chat.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigate(AppDestination.MemberProfile.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = AppDestination.MemberProfile.route) {
            MemberProfileRoute(
                viewModel = viewModel<MemberProfileViewModel>(),
                onLogoutClick = {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(AppDestination.Login.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onMessagesClick = { currentUser ->
                    navController.navigate(AppDestination.Chat.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
                },
                onHomeClick = {
                    navController.navigate(AppDestination.MemberHome.route) {
                        popUpTo(AppDestination.MemberHome.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onViewContributionsClick = {
                    navController.navigate(AppDestination.MemberContributions.route) {
                        launchSingleTop = true
                    }
                },
                onChangePasswordClick = {
                    // Add change password destination later
                }
            )
        }

        composable(route = AppDestination.MemberContributions.route) {
            MemberContributionsRoute(
                onBackClick = {
                    navController.popBackStack()
                },
                onMessagesClick = { currentUser ->
                    navController.navigate(AppDestination.Chat.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
                },
                onHomeClick = {
                    navController.navigate(AppDestination.MemberHome.route) {
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigate(AppDestination.MemberProfile.route) {
                        launchSingleTop = true
                    }
                },
                onContributionClick = { contributionId ->
                },
                onECertClick = { contributionId ->
                }
            )
        }

        composable(route = AppDestination.OrganisationHome.route,
            arguments = listOf(
                navArgument("currentUser") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val currentUser = backStackEntry.arguments?.getString("currentUser") ?: return@composable
            OrganisationHomeRoute(
                viewModel = viewModel<OrganisationHomeViewModel>(),
                onProjectSelected = { projectId ->
                    navController.navigate(
                        AppDestination.OrganisationProjectDetail.createRoute(projectId)
                    )
                },
                onViewProjectClick = {
                    navController.navigate(
                        AppDestination.OrganisationViewProject.createRoute(currentUser)
                    ) {
                        launchSingleTop = true
                    }
                },
                onMessagesClick = { currentUser ->
                    navController.navigate(AppDestination.Chat.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigate(AppDestination.OrganisationProfile.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = AppDestination.OrganisationViewProject.route,
            arguments = listOf(
                navArgument("currentUser") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val currentUser = backStackEntry.arguments?.getString("currentUser") ?: return@composable

            ViewProjectRoute(
                viewModel = viewModel<ViewProjectViewModel>(),
                onProjectSelected = { projectId ->
                    navController.navigate(
                        AppDestination.OrganisationProjectDetail.createRoute(projectId)
                    )
                },
                onHomeClick = {
                    navController.navigate(
                        AppDestination.OrganisationHome.createRoute(currentUser)
                    ) {
                        popUpTo(AppDestination.OrganisationHome.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onMessagesClick = { currentUser ->
                    navController.navigate(AppDestination.Chat.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigate(AppDestination.OrganisationProfile.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = AppDestination.OrganisationProfile.route) {
            OrganisationProfileRoute(
                onBackClick = {
                    navController.popBackStack()
                },
                onLogoutClick = {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(AppDestination.Login.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onMessagesClick = { currentUser ->
                    navController.navigate(AppDestination.Chat.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
                },
                onHomeClick = {
                    navController.navigate(AppDestination.OrganisationHome.route) {
                        popUpTo(AppDestination.OrganisationHome.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onViewPastProjectsClick = {
                    navController.navigate(AppDestination.OrganisationPastProjects.route) {
                        launchSingleTop = true
                    }
                },
                onViewContributionsClick = {
                    navController.navigate(AppDestination.MemberContributions.route) {
                        launchSingleTop = true
                    }
                },
                onChangePasswordClick = {
                    // Add change password screen later
                }
            )
        }

        composable(route = AppDestination.OrganisationPastProjects.route) {
            OrganisationPastProjectsRoute(
                onBackClick = {
                    navController.popBackStack()
                },
                onMessagesClick = { currentUser ->
                    navController.navigate(AppDestination.Chat.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
                },
                onHomeClick = {
                    navController.navigate(AppDestination.OrganisationHome.route) {
                        popUpTo(AppDestination.OrganisationHome.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigate(AppDestination.OrganisationProfile.route) {
                        popUpTo(AppDestination.OrganisationProfile.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = AppDestination.AdminHome.route) {
            AdminHomeRoute(
                viewModel = viewModel<AdminHomeViewModel>(),
                onRequestClick = {
                    navController.navigate(AppDestination.AdminRequest.route) {
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigate(AppDestination.AdminProfile.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = AppDestination.AdminMonitorDetail.route,
            arguments = listOf(
                navArgument("projectId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId").orEmpty()

            AdminMonitorDetailRoute(
                projectId = projectId,
                onBackClick = {
                    navController.popBackStack()
                },
                onCancelProjectClick = {
                },
                onWarnProjectClick = {
                    navController.navigate(
                        AppDestination.AdminWarningDetail.createRoute(projectId)
                    ) {
                        launchSingleTop = true
                    }
                },
                onViewChatroomClick = {
                    navController.navigate(AppDestination.Chat.route) {
                        launchSingleTop = true
                    }
                },
                viewModel = viewModel<AdminMonitorDetailViewModel>()
            )
        }

        composable(
            route = AppDestination.AdminWarningDetail.route,
            arguments = listOf(
                navArgument("projectId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId").orEmpty()

            AdminWarningDetailRoute(
                projectId = projectId,
                onBackClick = {
                    navController.popBackStack()
                },
                onWarnOrganisationClick = {
                },
                viewModel = viewModel<AdminWarningDetailViewModel>()
            )
        }

        composable(route = AppDestination.AdminRequest.route) {
            AdminRequestRoute(
                viewModel = viewModel<AdminRequestViewModel>(),
                onHomeClick = {
                    navController.navigate(AppDestination.AdminHome.route) {
                        popUpTo(AppDestination.AdminHome.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigate(AppDestination.AdminProfile.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = AppDestination.AdminProfile.route) {
            AdminProfileRoute(
                onLogoutClick = {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(AppDestination.Login.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onHomeClick = {
                    navController.navigate(AppDestination.AdminHome.route) {
                        popUpTo(AppDestination.AdminHome.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onRequestsClick = {
                    navController.navigate(AppDestination.AdminRequest.route) {
                        popUpTo(AppDestination.AdminRequest.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onChangePasswordClick = {
                    // Add change password navigation later
                }
            )
        }

        composable(
            route = AppDestination.Chat.route,
            arguments = listOf(
                navArgument("currentUser") {
                    type = NavType.StringType
                }
            )
        ) {
            ChatRoute(
                viewModel = viewModel<ChatViewModel>(),
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = AppDestination.MemberProjectDetail.route,
            arguments = listOf(
                navArgument("projectId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId").orEmpty()

            MemberProjectDetailRoute(
                projectId = projectId,
                onBackClick = {
                    navController.popBackStack()
                },
                onContributeClick = {
                }
            )
        }
    }
}