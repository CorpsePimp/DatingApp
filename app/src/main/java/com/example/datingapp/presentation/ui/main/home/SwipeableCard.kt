package com.example.datingapp.presentation.ui.main.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.datingapp.domain.model.User
import com.example.datingapp.presentation.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun SwipeableCard(
    user: User,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    val animatedOffsetX = remember { Animatable(0f) }
    val animatedOffsetY = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    // Sync animated values with offset during drag
    LaunchedEffect(offsetX) {
        animatedOffsetX.snapTo(offsetX)
    }

    LaunchedEffect(offsetY) {
        animatedOffsetY.snapTo(offsetY)
    }

    val rotation = (animatedOffsetX.value / 20f).coerceIn(-15f, 15f)
    val swipeThreshold = 300f

    // Calculate alpha for swipe indicators
    val swipeProgress = (abs(animatedOffsetX.value) / swipeThreshold).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .graphicsLayer {
                translationX = animatedOffsetX.value
                translationY = animatedOffsetY.value
                rotationZ = rotation
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        when {
                            offsetX > swipeThreshold -> {
                                // Swipe right - Like
                                onSwipeRight()
                            }
                            offsetX < -swipeThreshold -> {
                                // Swipe left - Dislike
                                onSwipeLeft()
                            }
                            else -> {
                                // Return to center with animation
                                coroutineScope.launch {
                                    launch {
                                        animatedOffsetX.animateTo(
                                            targetValue = 0f,
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessMedium
                                            )
                                        )
                                    }
                                    launch {
                                        animatedOffsetY.animateTo(
                                            targetValue = 0f,
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessMedium
                                            )
                                        )
                                    }
                                }
                                offsetX = 0f
                                offsetY = 0f
                            }
                        }
                    },
                    onDragCancel = {
                        coroutineScope.launch {
                            launch {
                                animatedOffsetX.animateTo(0f, spring())
                            }
                            launch {
                                animatedOffsetY.animateTo(0f, spring())
                            }
                        }
                        offsetX = 0f
                        offsetY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                )
            }
    ) {
        // Card Content
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Placeholder for photo (gradient background)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    SoftPink,
                                    LightViolet,
                                    DeepPink.copy(alpha = 0.6f)
                                )
                            )
                        )
                )

                // Swipe Left Indicator
                if (offsetX < -50f) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ErrorRed.copy(alpha = swipeProgress * 0.3f))
                            .padding(32.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Text(
                            text = "NOPE",
                            style = MaterialTheme.typography.displaySmall,
                            color = Color.White.copy(alpha = swipeProgress),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Swipe Right Indicator
                if (offsetX > 50f) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SuccessGreen.copy(alpha = swipeProgress * 0.3f))
                            .padding(32.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Text(
                            text = "LIKE",
                            style = MaterialTheme.typography.displaySmall,
                            color = Color.White.copy(alpha = swipeProgress),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // User Info Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.8f)
                                ),
                                startY = 300f
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = user.name,
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${user.age}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }

                        user.distance?.let { distance ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "📍 ${distance} км от вас",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = user.bio,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.9f),
                            maxLines = 3
                        )

                        if (user.interests.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                user.interests.take(3).forEach { interest ->
                                    InterestChip(interest)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InterestChip(text: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
