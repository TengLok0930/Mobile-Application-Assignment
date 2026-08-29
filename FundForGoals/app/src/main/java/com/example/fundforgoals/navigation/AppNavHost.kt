package com.example.fundforgoals.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fundforgoals.core.session.SessionManager
import com.example.fundforgoals.feature.admin.home.presentation.AdminHomeRoute
import com.example.fundforgoals.feature.admin.home.presentation.AdminHomeViewModel
import com.example.fundforgoals.feature.admin.profile.presentation.AdminProfileRoute
import com.example.fundforgoals.feature.admin.requests.presentation.AdminRequestRoute
import com.example.fundforgoals.feature.admin.requests.presentation.AdminRequestViewModel
import com.example.fundforgoals.feature.auth.changepassword.presentation.ChangePasswordRoute
import com.example.fundforgoals.feature.auth.forgotpassword.presentation.ForgotPasswordRoute
import com.example.fundforgoals.feature.auth.login.presentation.LoginRoute
import com.example.fundforgoals.feature.auth.login.presentation.LoginViewModel
import com.example.fundforgoals.feature.auth.registration.member.MemberRegRoute
import com.example.fundforgoals.feature.auth.registration.member.MemberRegViewModel
import com.example.fundforgoals.feature.auth.registration.organisation.presentation.OrganisationRegRoute
import com.example.fundforgoals.feature.auth.registration.organisation.presentation.OrganisationRegViewModel
import com.example.fundforgoals.feature.auth.registration.signup_choice.presentation.SignUpChoiceRoute
import com.example.fundforgoals.feature.chat.presentation.AdminChatroomRoute
import com.example.fundforgoals.feature.chat.presentation.ChatRoute
import com.example.fundforgoals.feature.chat.presentation.ChatViewModel
import com.example.fundforgoals.feature.member.contribute.presentation.MemberContributeRoute
import com.example.fundforgoals.feature.member.contribute.presentation.MemberContributeViewModel
import com.example.fundforgoals.feature.member.home.presentation.MemberHomeRoute
import com.example.fundforgoals.feature.member.home.presentation.MemberHomeViewModel
import com.example.fundforgoals.feature.member.profile.presentation.MemberProfileRoute
import com.example.fundforgoals.feature.member.profile.presentation.MemberProfileViewModel
import com.example.fundforgoals.feature.organisation.contribute.presentation.OrganisationContributeRoute
import com.example.fundforgoals.feature.organisation.contribute.presentation.OrganisationContributeViewModel
import com.example.fundforgoals.feature.organisation.createProject.presentation.CreateProjectRoute
import com.example.fundforgoals.feature.organisation.home.presentation.OrganisationHomeRoute
import com.example.fundforgoals.feature.organisation.home.presentation.OrganisationHomeViewModel
import com.example.fundforgoals.feature.organisation.profile.presentation.OrganisationProfileRoute
import com.example.fundforgoals.feature.organisation.profile.presentation.OrganisationProfileViewModel
import com.example.fundforgoals.feature.organisation.viewProject.ViewProjectRoute
import com.example.fundforgoals.feature.organisation.viewProject.ViewProjectViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val savedUsername = sessionManager.getUsername().orEmpty()
    val savedUserType = sessionManager.getUserType().orEmpty()

    val startDestination = when {
        !sessionManager.isLoggedIn() -> AppDestination.Login.route

        savedUserType.equals("admin", ignoreCase = true) ->
            AppDestination.AdminHome.route

        savedUserType.equals("organisation", ignoreCase = true) &&
                savedUsername.isNotBlank() ->
            AppDestination.OrganisationHome.createRoute(savedUsername)

        savedUserType.equals("member", ignoreCase = true) &&
                savedUsername.isNotBlank() ->
            AppDestination.MemberHome.createRoute(savedUsername)

        else -> AppDestination.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = AppDestination.Login.route) {
            val loginViewModel: LoginViewModel = viewModel()

            LoginRoute(
                viewModel = loginViewModel,
                onForgotPasswordClick = {
                    navController.navigate(AppDestination.ForgotPassword.route) {
                        launchSingleTop = true
                    }
                },
                onSignUpClick = {
                    navController.navigate(AppDestination.SignUpChoice.route) {
                        launchSingleTop = true
                    }
                },
                onLoginSuccess = { username, userType ->
                    sessionManager.saveLoginSession(
                        username = username,
                        userType = userType
                    )

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
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(route = AppDestination.ForgotPassword.route) {
            ForgotPasswordRoute(
                onBackClick = {
                    navController.popBackStack()
                },
                onRequestSubmitted = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppDestination.ChangePassword.route,
            arguments = listOf(
                navArgument("currentUser") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val currentUser = backStackEntry.arguments?.getString("currentUser") ?: return@composable

            ChangePasswordRoute(
                username = currentUser,
                onBackClick = {
                    navController.popBackStack()
                },
                onPasswordChanged = {
                    navController.popBackStack()
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
                onMessagesClick = {
                    navController.navigate(AppDestination.Chat.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigate(
                        AppDestination.MemberProfile.createRoute(currentUser)
                    ) {
                        launchSingleTop = true
                    }
                },
                onContributeClick = { projectId ->
                    navController.navigate(
                        AppDestination.MemberContribute.createRoute(currentUser, projectId)
                    )
                }
            )
        }

        composable(
            route = AppDestination.MemberContribute.route,
            arguments = listOf(
                navArgument("currentUser") { type = NavType.StringType },
                navArgument("projectId") { type = NavType.IntType }
            )
        ) {
            MemberContributeRoute(
                viewModel = viewModel<MemberContributeViewModel>(),
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppDestination.MemberProfile.route,
            arguments = listOf(
                navArgument("currentUser") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val currentUser = backStackEntry.arguments?.getString("currentUser") ?: return@composable

            MemberProfileRoute(
                viewModel = viewModel<MemberProfileViewModel>(),
                isDarkTheme = isDarkTheme,
                onLogoutClick = {
                    sessionManager.clearSession()
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onMessagesClick = {
                    navController.navigate(AppDestination.Chat.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
                },
                onHomeClick = {
                    navController.navigate(AppDestination.MemberHome.createRoute(currentUser)) {
                        popUpTo(AppDestination.MemberHome.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onViewContributionsClick = {
                    navController.navigate(AppDestination.MemberContributions.route) {
                        launchSingleTop = true
                    }
                },
                onAppearanceClick = {
                    onToggleTheme()
                },
                onChangePasswordClick = {
                    navController.navigate(AppDestination.ChangePassword.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
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
                onCreateProjectClick = { currentUser ->
                    navController.navigate(
                        AppDestination.OrganisationCreateProject.createRoute(currentUser)
                    ) {
                        launchSingleTop = true
                    }
                },
                onViewProjectClick = {
                    navController.navigate(
                        AppDestination.OrganisationViewProject.createRoute(currentUser)
                    ) {
                        launchSingleTop = true
                    }
                },
                onMessagesClick = {
                    navController.navigate(AppDestination.Chat.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigate(
                        AppDestination.OrganisationProfile.createRoute(currentUser)
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = AppDestination.OrganisationCreateProject.route,
            arguments = listOf(
                navArgument("currentUser") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val currentUser = backStackEntry.arguments?.getString("currentUser") ?: return@composable

            CreateProjectRoute(
                currentUser = currentUser,
                onBackClick = {
                    navController.popBackStack()
                },
                onProjectCreated = {
                    navController.navigate(
                        AppDestination.OrganisationHome.createRoute(currentUser)
                    ) {
                        popUpTo(AppDestination.OrganisationHome.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onMessagesClick = {
                    navController.navigate(AppDestination.Chat.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
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
                onProfileClick = {
                    navController.navigate(
                        AppDestination.OrganisationProfile.createRoute(currentUser)
                    ) {
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
                onContributeClick = { projectId ->
                    navController.navigate(
                        AppDestination.OrganisationContribute.createRoute(currentUser, projectId)
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
                onMessagesClick = {
                    navController.navigate(AppDestination.Chat.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigate(
                        AppDestination.OrganisationProfile.createRoute(currentUser)
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = AppDestination.OrganisationContribute.route,
            arguments = listOf(
                navArgument("currentUser") { type = NavType.StringType },
                navArgument("projectId") { type = NavType.IntType }
            )
        ) {
            OrganisationContributeRoute(
                viewModel = viewModel<OrganisationContributeViewModel>(),
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppDestination.OrganisationProfile.route,
            arguments = listOf(
                navArgument("currentUser") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val currentUser = backStackEntry.arguments?.getString("currentUser") ?: return@composable

            OrganisationProfileRoute(
                viewModel = viewModel<OrganisationProfileViewModel> (),
                onLogoutClick = {
                    sessionManager.clearSession()
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onMessagesClick = {
                    navController.navigate(AppDestination.Chat.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
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
                onViewContributionsClick = {
                    navController.navigate(AppDestination.MemberContributions.route) {
                        launchSingleTop = true
                    }
                },
                onChangePasswordClick = {
                    navController.navigate(AppDestination.ChangePassword.createRoute(currentUser)) {
                        launchSingleTop = true
                    }
                },
                onAppearanceClick = {
                    onToggleTheme()
                },
                isDarkTheme = isDarkTheme
            )
        }

        composable(route = AppDestination.AdminHome.route) {
            val adminUser = sessionManager.getUsername().orEmpty()

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
                },
                onMonitorClick = { /* handled internally by AdminHomeRoute for compact; no-op here */ },
                onWarnProjectClick = { projectId ->
                    navController.navigate(
                        AppDestination.AdminWarningDetail.createRoute(projectId.toString())
                    ) {
                        launchSingleTop = true
                    }
                },
                onViewChatroomClick = { projectId ->
                    navController.navigate(
                        AppDestination.AdminChatroom.createRoute(adminUser, projectId)
                    ) {
                        launchSingleTop = true
                    }
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
                    navController.navigate(AppDestination.AdminProfile.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = AppDestination.AdminProfile.route) {
            AdminProfileRoute(
                onLogoutClick = {
                    sessionManager.clearSession()
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(navController.graph.id) {
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
                    val currentUser = sessionManager.getUsername().orEmpty()
                    if (currentUser.isNotBlank()) {
                        navController.navigate(AppDestination.ChangePassword.createRoute(currentUser)) {
                            launchSingleTop = true
                        }
                    }
                },
                onAppearanceClick = {
                    onToggleTheme()
                },
                isDarkTheme = isDarkTheme
            )
        }

        composable(
            route = AppDestination.Chat.route,
            arguments = listOf(
                navArgument("currentUser") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val currentUser = backStackEntry.arguments?.getString("currentUser")
                ?: return@composable
            val userType = sessionManager.getUserType().orEmpty()

            ChatRoute(
                viewModel = viewModel<ChatViewModel>(),
                onHomeClick = {
                    val destination = when {
                        userType.equals("organisation", ignoreCase = true) ->
                            AppDestination.OrganisationHome.createRoute(currentUser)
                        userType.equals("member", ignoreCase = true) ->
                            AppDestination.MemberHome.createRoute(currentUser)
                        userType.equals("admin", ignoreCase = true) ->
                            AppDestination.AdminHome.route
                        else -> null
                    }
                    if (destination != null) {
                        navController.navigate(destination) {
                            popUpTo(navController.graph.id) { inclusive = false }
                            launchSingleTop = true
                        }
                    } else {
                        navController.popBackStack()
                    }
                },
                onProfileClick = {
                    val destination = when {
                        userType.equals("organisation", ignoreCase = true) ->
                            AppDestination.OrganisationProfile.createRoute(currentUser)
                        userType.equals("member", ignoreCase = true) ->
                            AppDestination.MemberProfile.createRoute(currentUser)
                        userType.equals("admin", ignoreCase = true) ->
                            AppDestination.AdminProfile.route
                        else -> null
                    }
                    if (destination != null) {
                        navController.navigate(destination) { launchSingleTop = true }
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable(
            route = AppDestination.AdminChatroom.route,
            arguments = listOf(
                navArgument("currentUser") { type = NavType.StringType },
                navArgument("projectId") { type = NavType.StringType }
            )
        ) {
            AdminChatroomRoute(
                viewModel = viewModel<ChatViewModel>(),
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}