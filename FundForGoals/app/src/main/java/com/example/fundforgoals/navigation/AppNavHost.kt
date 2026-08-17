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
import com.example.fundforgoals.feature.admin.requests.presentation.AdminRequestRoute
import com.example.fundforgoals.feature.admin.requests.presentation.AdminRequestViewModel
import com.example.fundforgoals.feature.auth.presentation.login.LoginRoute
import com.example.fundforgoals.feature.auth.presentation.login.LoginViewModel
import com.example.fundforgoals.feature.auth.presentation.register.RegisterRoute
import com.example.fundforgoals.feature.auth.presentation.register.RegisterViewModel
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
            LoginRoute(
                viewModel = viewModel<LoginViewModel>(),
                onBackClick = {
                    navController.popBackStack()
                },
                onForgotPasswordClick = {
                },
                onSignUpClick = {
                    navController.navigate(AppDestination.Register.route)
                },
                onLoginSuccess = { username ->
                    val destination = if (username.trim().equals("admin", ignoreCase = true)) {
                        AppDestination.AdminHome.route
                    } else if (username.trim().equals("org", ignoreCase = true)) {
                        AppDestination.OrganisationHome.route
                    } else {
                        AppDestination.MemberHome.route
                    }

                    navController.navigate(destination) {
                        popUpTo(AppDestination.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AppDestination.Register.route) {
            RegisterRoute(
                viewModel = viewModel<RegisterViewModel>(),
                onBackClick = {
                    navController.popBackStack()
                },
                onLoginClick = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(AppDestination.MemberHome.route) {
                        popUpTo(AppDestination.Login.route) {
                            inclusive = true
                        }
                    }
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
                    navController.navigate(AppDestination.AdminRequest.route)
                },
                onMonitorProjectClick = {
                },
                onProfileClick = {
                }
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