package com.example.datingapp.presentation.ui.profile

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*


// Light theme colors
private val BackgroundStart = Color(0xFFFFF5F7)
private val BackgroundEnd = Color(0xFFF5F0FF)
private val CardBackground = Color(0xFFFFFFFF)
private val GlassOverlay = Color(0x15000000)
private val AccentPink = Color(0xFFE91E63)
private val AccentViolet = Color(0xFF9C27B0)
private val GoldAccent = Color(0xFFFFD700)
private val TextPrimary = Color(0xFF2D2D2D)
private val TextSecondary = Color(0xFF757575)

@Composable
fun ProfileScreen(
    onEditProfile: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BackgroundStart,
                        BackgroundEnd
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 100.dp)
        ) {
            // Header with settings
            ProfileHeader(onSettingsClick = onSettingsClick)

            // Profile Avatar Section
            ProfileAvatarSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Bento Grid
            BentoGridSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Edit Profile Button
            EditProfileButton(onClick = onEditProfile)

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Section
            StatsSection()
        }
    }
}

@Composable
private fun ProfileHeader(onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Профиль",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .size(44.dp)
                .background(CardBackground, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "Settings",
                tint = TextPrimary
            )
        }
    }
}

@Composable
private fun ProfileAvatarSection() {
    val profileCompletion = 0.78f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar with progress ring
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(140.dp)
        ) {
            // Progress ring
            CircularProgressRing(
                progress = profileCompletion,
                modifier = Modifier.fillMaxSize()
            )

            // Avatar
            AsyncImage(
                model = "https://picsum.photos/seed/profile/400/400",
                contentDescription = "Profile photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(3.dp, CardBackground, CircleShape)
            )

            // Completion badge
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = AccentPink
            ) {
                Text(
                    text = "${(profileCompletion * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Name
        Text(
            text = "Александр, 25",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Location
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = "Location",
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Москва, Россия",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun CircularProgressRing(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    val gradient = Brush.sweepGradient(
        colors = listOf(AccentPink, AccentViolet, AccentPink)
    )

    Box(
        modifier = modifier
            .drawBehind {
                // Background ring
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                )

                // Progress arc
                drawArc(
                    brush = gradient,
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                )
            }
    )
}

@Composable
private fun BentoGridSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Premium Card (Large)
        PremiumCard()

        // Small cards row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FeatureCard(
                title = "Режим\nпутешествий",
                icon = Icons.Outlined.Flight,
                gradientColors = listOf(Color(0xFF4158D0), Color(0xFFC850C0)),
                modifier = Modifier.weight(1f)
            )
            FeatureCard(
                title = "Буст",
                icon = Icons.Rounded.RocketLaunch,
                value = "5",
                gradientColors = listOf(Color(0xFFFF9A00), Color(0xFFFF5F00)),
                modifier = Modifier.weight(1f)
            )
        }

        // Another row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FeatureCard(
                title = "Супер\nлайки",
                icon = Icons.Rounded.Star,
                value = "3",
                gradientColors = listOf(Color(0xFF00D4FF), Color(0xFF0099FF)),
                modifier = Modifier.weight(1f)
            )
            FeatureCard(
                title = "Топ Пикс",
                icon = Icons.Rounded.LocalFireDepartment,
                gradientColors = listOf(Color(0xFFFF416C), Color(0xFFFF4B2B)),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PremiumCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            AccentViolet.copy(alpha = 0.8f),
                            AccentPink.copy(alpha = 0.8f)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.2f),
                    RoundedCornerShape(28.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.WorkspacePremium,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Premium",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Безлимитные лайки и функции",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = "Go to Premium",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(
    title: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    value: String? = null
) {
    Surface(
        modifier = modifier
            .height(110.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(gradientColors),
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.2f),
                    RoundedCornerShape(24.dp)
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )

                    if (value != null) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = value,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun EditProfileButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            border = androidx.compose.foundation.BorderStroke(
                2.dp,
                Brush.linearGradient(
                    colors = listOf(AccentPink, AccentViolet)
                )
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = TextPrimary
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Редактировать профиль",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun StatsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Статистика",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = CardBackground,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(value = "142", label = "Лайки")
                StatDivider()
                StatItem(value = "28", label = "Мэтчи")
                StatDivider()
                StatItem(value = "12", label = "Чаты")
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = AccentPink
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(TextSecondary.copy(alpha = 0.3f))
    )
}
