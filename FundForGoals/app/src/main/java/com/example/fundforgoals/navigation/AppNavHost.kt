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
import com.example.fundforgoals.feature.admin.profile.presentation.AdminProfileViewModel
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
import com.example.fundforgoals.feature.member.home.presentation.MemberHomeRoute
import com.example.fundforgoals.feature.member.home.presentation.MemberHomeViewModel
import com.example.fundforgoals.feature.member.project_detail.presentation.MemberProjectDetailRoute
import com.example.fundforgoals.feature.organisation.home.presentation.OrganisationHomeRoute
import com.example.fundforgoals.feature.organisation.home.presentation.OrganisationHomeViewModel

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
                onBackClick = {
                    navController.popBackStack()
                },
                onForgotPasswordClick = {
                    // Navigate to forgot password screen
                },
                onSignUpClick = {
                    navController.navigate(AppDestination.SignUpChoice.route) {
                        launchSingleTop = true
                    }
                },
                onLoginSuccess = { userType ->
                    val destination = when {
                        userType.trim().equals("admin", ignoreCase = true) -> {
                            AppDestination.AdminHome.route
                        }
<<<<<<< Updated upstream

                        username.trim().equals("org", ignoreCase = true) -> {
                            AppDestination.OrganisationHome.route
                        }

                        else -> {
=======
                        userType.trim().equals("organisation", ignoreCase = true) -> {
                            AppDestination.OrganisationHome.route
                        }
                        userType.trim().equals("member", ignoreCase = true) -> {
>>>>>>> Stashed changes
                            AppDestination.MemberHome.route
                        }
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

        composable(route = AppDestination.MemberHome.route) {
            MemberHomeRoute(
                viewModel = viewModel<MemberHomeViewModel>(),
                onProjectSelected = { projectId ->
                    navController.navigate(
                        AppDestination.MemberProjectDetail.createRoute(projectId)
                    )
                },
                onMessagesClick = {
                    navController.navigate(AppDestination.Chat.route)
                },
                onProfileClick = {
                }
            )
        }

        composable(route = AppDestination.OrganisationHome.route) {
            OrganisationHomeRoute(
                viewModel = viewModel<OrganisationHomeViewModel>(),
                onProjectSelected = { projectId ->
                    navController.navigate(
                        AppDestination.OrganisationProjectDetail.createRoute(projectId)
                    )
                },
                onMessagesClick = {
                    navController.navigate(AppDestination.Chat.route)
                },
                onProfileClick = {
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
                    navController.navigate(AppDestination.Chat.route)
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
                viewModel = viewModel<AdminProfileViewModel>(),
                onRequestsClick = {
                    navController.navigate(AppDestination.AdminRequest.route) {
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
                onChangePasswordClick = {
                },
                onLogoutClick = {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(AppDestination.Login.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = AppDestination.Chat.route) {
            ChatRoute(
                viewModel = viewModel<ChatViewModel>(),
                onBackClick = {
                    navController.popBackStack()
                }
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