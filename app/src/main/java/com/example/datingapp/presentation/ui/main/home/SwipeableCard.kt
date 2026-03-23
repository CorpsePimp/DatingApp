package com.example.datingapp.presentation.ui.main.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.datingapp.domain.model.User
import com.example.datingapp.presentation.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

enum class SwipeDirection {
    Left, Right, None
}

// State for controlling swipe programmatically
class SwipeableCardState {
    internal var onSwipeLeftInternal: (() -> Unit)? = null
    internal var onSwipeRightInternal: (() -> Unit)? = null

    fun swipeLeft() {
        onSwipeLeftInternal?.invoke()
    }

    fun swipeRight() {
        onSwipeRightInternal?.invoke()
    }
}

@Composable
fun rememberSwipeableCardState(): SwipeableCardState {
    return remember { SwipeableCardState() }
}

@Composable
fun SwipeableCard(
    user: User,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier,
    state: SwipeableCardState = rememberSwipeableCardState()
) {
    val scope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenWidthPx = with(LocalDensity.current) { screenWidth.toPx() }

    // Single source of truth for position
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    // Animation state
    var isAnimating by remember { mutableStateOf(false) }

    val swipeThreshold = screenWidthPx * 0.4f
    val rotation = (offsetX / screenWidthPx) * 15f
    val swipeProgress = (abs(offsetX) / swipeThreshold).coerceIn(0f, 1f)

    // Function to animate card out and call callback
    fun animateSwipeOut(direction: SwipeDirection, callback: () -> Unit) {
        if (isAnimating) return
        isAnimating = true

        scope.launch {
            val targetX = if (direction == SwipeDirection.Left) -screenWidthPx * 1.5f else screenWidthPx * 1.5f
            val targetY = 200f

            // Animate to target
            animate(
                initialValue = offsetX,
                targetValue = targetX,
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ) { value, _ ->
                offsetX = value
            }

            // Reset and notify
            offsetX = 0f
            offsetY = 0f
            isAnimating = false
            callback()
        }
    }

    // Function to animate card back to center
    fun animateToCenter() {
        if (isAnimating) return
        isAnimating = true

        scope.launch {
            // Animate X back to 0
            launch {
                animate(
                    initialValue = offsetX,
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) { value, _ ->
                    offsetX = value
                }
            }
            // Animate Y back to 0
            launch {
                animate(
                    initialValue = offsetY,
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) { value, _ ->
                    offsetY = value
                }
            }
        }.invokeOnCompletion {
            isAnimating = false
        }
    }

    // Setup programmatic swipe functions
    LaunchedEffect(Unit) {
        state.onSwipeLeftInternal = {
            animateSwipeOut(SwipeDirection.Left, onSwipeLeft)
        }
        state.onSwipeRightInternal = {
            animateSwipeOut(SwipeDirection.Right, onSwipeRight)
        }
    }

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .graphicsLayer {
                rotationZ = rotation
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        when {
                            offsetX > swipeThreshold -> {
                                animateSwipeOut(SwipeDirection.Right, onSwipeRight)
                            }
                            offsetX < -swipeThreshold -> {
                                animateSwipeOut(SwipeDirection.Left, onSwipeLeft)
                            }
                            else -> {
                                animateToCenter()
                            }
                        }
                    },
                    onDragCancel = {
                        animateToCenter()
                    },
                    onDrag = { change, dragAmount ->
                        if (!isAnimating) {
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
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
                // User Photo with Coil
                SubcomposeAsyncImage(
                    model = user.photoUrl,
                    contentDescription = "Photo of ${user.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            SoftPink.copy(alpha = 0.3f),
                                            LightViolet.copy(alpha = 0.3f),
                                            SoftPink.copy(alpha = 0.3f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = DeepPink,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    },
                    error = {
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
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = "Default avatar",
                                tint = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(120.dp)
                            )
                        }
                    }
                )

                // Swipe Left Indicator (NOPE)
                if (offsetX < -50f) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.error.copy(alpha = swipeProgress * 0.3f))
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

                // Swipe Right Indicator (LIKE)
                if (offsetX > 50f) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF4CAF50).copy(alpha = swipeProgress * 0.3f))
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
