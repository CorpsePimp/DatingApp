package com.example.datingapp.presentation.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.datingapp.presentation.ui.chat.ChatListScreen
import com.example.datingapp.presentation.ui.components.BottomNavItem
import com.example.datingapp.presentation.ui.components.BottomNavigationBar
import com.example.datingapp.presentation.ui.main.home.MainScreen
import com.example.datingapp.presentation.ui.profile.ProfileScreen
import com.example.datingapp.presentation.ui.theme.TextSecondary

// Light theme colors
private val BackgroundStart = Color(0xFFFFF5F7)
private val BackgroundEnd = Color(0xFFF5F0FF)

@Composable
fun MainScaffold(
    navController: NavHostController
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem(
            route = "chat",
            icon = Icons.Outlined.ChatBubbleOutline,
            label = "Чаты"
        ),
        BottomNavItem(
            route = "likes",
            icon = Icons.Rounded.Favorite,
            label = "Лайки"
        ),
        BottomNavItem(
            route = "main",
            icon = Icons.Rounded.FavoriteBorder,
            label = "Метч",
            iconSize = 32.dp
        ),
        BottomNavItem(
            route = "cards",
            icon = Icons.Outlined.Style,
            label = "Карточки"
        ),
        BottomNavItem(
            route = "profile",
            icon = Icons.Rounded.PersonOutline,
            label = "Профиль"
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                currentRoute = currentRoute,
                onItemClick = { route ->
                    bottomNavController.navigate(route) {
                        popUpTo(bottomNavController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(BackgroundStart, BackgroundEnd)
                    )
                )
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = "main",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("main") {
                    MainScreen(
                        onNavigateToProfile = { /* Handled by bottom nav */ },
                        onNavigateToChat = { /* Handled by bottom nav */ }
                    )
                }

                composable("chat") {
                    ChatListScreen(
                        onChatClick = { chatId -> /* TODO: Navigate to chat detail */ },
                        onMatchClick = { matchId -> /* TODO: Navigate to match profile */ }
                    )
                }

                composable("likes") {
                    PlaceholderScreen(title = "Лайки", emoji = "❤️")
                }

                composable("cards") {
                    PlaceholderScreen(title = "Карточки", emoji = "🎴")
                }

                composable("profile") {
                    ProfileScreen(
                        onEditProfile = { /* TODO: Navigate to edit profile */ },
                        onSettingsClick = { /* TODO: Navigate to settings */ }
                    )
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(
    title: String,
    emoji: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Скоро здесь что-то будет...",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}
