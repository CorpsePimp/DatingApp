package com.example.datingapp.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.datingapp.presentation.ui.auth.login.LoginScreen
import com.example.datingapp.presentation.ui.auth.register.RegisterScreen
import com.example.datingapp.presentation.ui.main.MainScaffold

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")
    object Likes : Screen("likes")
    object Cards : Screen("cards")
    object Profile : Screen("profile")
    object Chat : Screen("chat")
    object Settings : Screen("settings")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Login Screen
        composable(
            route = Screen.Login.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(500))
            }
        ) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Register Screen
        composable(
            route = Screen.Register.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(500))
            }
        ) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Main Screen (Discovery)
        composable(
            route = Screen.Main.route,
            enterTransition = {
                fadeIn(animationSpec = tween(600)) + scaleIn(
                    initialScale = 0.95f,
                    animationSpec = tween(600, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            }
        ) {
            MainScaffold(navController = navController)
        }

        // Settings Screen
        composable(
            route = Screen.Settings.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(500))
            }
        ) {
            com.example.datingapp.presentation.ui.settings.SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
