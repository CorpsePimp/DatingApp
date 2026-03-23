package com.example.datingapp.presentation.ui.main.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.datingapp.domain.model.getDummyUsers
import com.example.datingapp.presentation.ui.theme.*

@Composable
fun MainScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToChat: () -> Unit = {}
) {
    val users = remember { getDummyUsers().toMutableStateList() }
    var currentIndex by remember { mutableIntStateOf(0) }
    val cardState = remember { SwipeableCardState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BackgroundStart, BackgroundEnd)
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Card Stack
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (currentIndex < users.size) {
                    // Show next 2 cards for depth effect
                    if (currentIndex + 2 < users.size) {
                        CardPlaceholder(
                            modifier = Modifier
                                .fillMaxSize()
                                .offset(y = 16.dp)
                                .padding(horizontal = 16.dp),
                            scale = 0.9f
                        )
                    }

                    if (currentIndex + 1 < users.size) {
                        CardPlaceholder(
                            modifier = Modifier
                                .fillMaxSize()
                                .offset(y = 8.dp)
                                .padding(horizontal = 8.dp),
                            scale = 0.95f
                        )
                    }

                    // Current card - key forces recomposition when index changes
                    key(currentIndex) {
                        SwipeableCard(
                            user = users[currentIndex],
                            state = cardState,
                            onSwipeLeft = {
                                // Dislike action
                                currentIndex++
                                if (currentIndex >= users.size) {
                                    // Reload users or show "no more users" message
                                    currentIndex = 0
                                }
                            },
                            onSwipeRight = {
                                // Like action
                                currentIndex++
                                if (currentIndex >= users.size) {
                                    currentIndex = 0
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        )
                    }
                } else {
                    // No more users
                    NoMoreUsersMessage(
                        onReload = { currentIndex = 0 }
                    )
                }
            }

            // Action Buttons
            if (currentIndex < users.size) {
                ActionButtonsRow(
                    onDislike = {
                        cardState.swipeLeft()
                    },
                    onSuperLike = {
                        // Super like action - можно добавить позже
                        currentIndex++
                        if (currentIndex >= users.size) currentIndex = 0
                    },
                    onLike = {
                        cardState.swipeRight()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 24.dp)
                )
            }
        }
    }
}

@Composable
fun CardPlaceholder(
    modifier: Modifier = Modifier,
    scale: Float = 1f
) {
    Box(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = GlassWhite.copy(alpha = 0.5f)
            )
        ) {}
    }
}

@Composable
fun NoMoreUsersMessage(
    onReload: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎉",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Вы просмотрели всех!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Скоро появятся новые анкеты",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onReload,
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Посмотреть снова",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun ActionButtonsRow(
    onDislike: () -> Unit,
    onSuperLike: () -> Unit,
    onLike: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Dislike Button
        ActionButton(
            icon = Icons.Rounded.Close,
            onClick = onDislike,
            backgroundColor = Color.White,
            iconTint = ErrorRed,
            size = 64.dp
        )

        // Super Like Button
        ActionButton(
            icon = Icons.Rounded.Star,
            onClick = onSuperLike,
            backgroundColor = Color(0xFF00BFFF),
            iconTint = Color.White,
            size = 56.dp
        )

        // Like Button
        ActionButton(
            icon = Icons.Rounded.Favorite,
            onClick = onLike,
            backgroundColor = Color.White,
            iconTint = SuccessGreen,
            size = 64.dp
        )
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    onClick: () -> Unit,
    backgroundColor: Color,
    iconTint: Color,
    size: Dp,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    Surface(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = CircleShape,
        color = backgroundColor,
        shadowElevation = 8.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(32.dp)
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}
