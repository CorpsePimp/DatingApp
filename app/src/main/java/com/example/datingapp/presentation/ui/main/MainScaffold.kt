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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.datingapp.presentation.ui.cards.ActivitiesScreen
import com.example.datingapp.presentation.ui.chat.ChatDetailScreen
import com.example.datingapp.presentation.ui.chat.ChatListScreen
import com.example.datingapp.presentation.ui.components.BottomNavItem
import com.example.datingapp.presentation.ui.components.BottomNavigationBar
import com.example.datingapp.presentation.ui.main.home.MainScreen
import com.example.datingapp.presentation.ui.profile.ProfileScreen
import com.example.datingapp.presentation.ui.profile.wizard.EditProfileWizard
import com.example.datingapp.presentation.ui.theme.LocalIsItMode
import com.example.datingapp.presentation.ui.theme.TextSecondary
import com.example.datingapp.presentation.viewmodel.ChatViewModel
import com.example.datingapp.presentation.viewmodel.ProfileViewModel

// Light theme colors
private val BackgroundStart = Color(0xFFFFF5F7)
private val BackgroundEnd = Color(0xFFF5F0FF)

@Composable
fun MainScaffold(
    navController: NavHostController
) {
    val isItMode = LocalIsItMode.current
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Shared ViewModel for profile screens
    val profileViewModel: ProfileViewModel = viewModel()
    val profileState by profileViewModel.profileState.collectAsState()
    val showSuccessSnackbar by profileViewModel.showSuccessSnackbar.collectAsState()

    val bottomNavItems = listOf(
        BottomNavItem(
            route = "chat",
            icon = Icons.Outlined.ChatBubbleOutline,
            label = if (isItMode) "Комьюнити" else "Чаты"
        ),
        BottomNavItem(
            route = "likes",
            icon = Icons.Rounded.Favorite,
            label = if (isItMode) "Отклики" else "Лайки"
        ),
        BottomNavItem(
            route = "main",
            icon = Icons.Rounded.FavoriteBorder,
            label = if (isItMode) "Поиск" else "Метч",
            iconSize = 32.dp
        ),
        BottomNavItem(
            route = "cards",
            icon = Icons.Outlined.Style,
            label = if (isItMode) "Идеи" else "Карточки"
        ),
        BottomNavItem(
            route = "profile",
            icon = Icons.Rounded.PersonOutline,
            label = "Профиль"
        )
    )

    // Hide bottom bar on chat detail screen
    val shouldShowBottomBar = currentRoute?.startsWith("chat_detail") != true

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            if (shouldShowBottomBar) {
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
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
                        onChatClick = { chatId ->
                            bottomNavController.navigate("chat_detail/$chatId")
                        },
                        onMatchClick = { matchId ->
                            bottomNavController.navigate("chat_detail/$matchId")
                        }
                    )
                }

                composable(
                    route = "chat_detail/{userId}",
                    arguments = listOf(navArgument("userId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
                    val chatViewModel: ChatViewModel = viewModel()
                    val chatUiState by chatViewModel.uiState.collectAsState()

                    LaunchedEffect(userId) {
                        chatViewModel.initChat(userId)
                    }

                    ChatDetailScreen(
                        uiState = chatUiState,
                        onInputChange = { chatViewModel.updateInputText(it) },
                        onSendClick = { chatViewModel.sendMessage() },
                        onBackClick = { bottomNavController.popBackStack() }
                    )
                }

                composable("likes") {
                    PlaceholderScreen(title = "Лайки", emoji = "❤️")
                }

                composable("cards") {
                    ActivitiesScreen(
                        onCardClick = { cardId -> /* TODO: Navigate to card detail */ },
                        onVenueClick = { venueId -> /* TODO: Navigate to venue detail */ },
                        onAdClick = { adId -> /* TODO: Handle ad click */ }
                    )
                }

                composable("profile") {
                    ProfileScreen(
                        profile = profileState,
                        showSuccessSnackbar = showSuccessSnackbar,
                        onSnackbarDismissed = { profileViewModel.clearSuccessSnackbar() },
                        onEditProfile = {
                            profileViewModel.initWizard()
                            bottomNavController.navigate("edit_profile")
                        },
                        onSettingsClick = {
                            navController.navigate("settings")
                        }
                    )
                }

                composable("edit_profile") {
                    val wizardState by profileViewModel.wizardState.collectAsState()

                    EditProfileWizard(
                        state = wizardState,
                        onNameChange = { profileViewModel.updateName(it) },
                        onCityChange = { profileViewModel.updateCity(it) },
                        onBioChange = { profileViewModel.updateBio(it) },
                        onOccupationChange = { profileViewModel.updateOccupation(it) },
                        onAddPhoto = { profileViewModel.addPhoto() },
                        onRemovePhoto = { profileViewModel.removePhoto(it) },
                        onPreviewIndexChange = { profileViewModel.setPreviewPhotoIndex(it) },
                        onSearchChange = { profileViewModel.updateInterestSearch(it) },
                        onToggleInterest = { profileViewModel.toggleInterest(it) },
                        onNextStep = { profileViewModel.nextStep() },
                        onPreviousStep = { profileViewModel.previousStep() },
                        onGoToStep1 = { profileViewModel.goToStep1() },
                        onSave = {
                            profileViewModel.saveProfile {
                                bottomNavController.popBackStack()
                            }
                        },
                        onClose = { bottomNavController.popBackStack() }
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
