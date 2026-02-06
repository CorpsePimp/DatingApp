package com.example.datingapp.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Light theme colors
private val NavBarBackground = Color(0xFFFFFFFF)
private val GlassOverlay = Color(0x15000000)
private val AccentPink = Color(0xFFE91E63)
private val AccentViolet = Color(0xFF9C27B0)
private val TextSecondary = Color(0xFF757575)

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val iconSize: Dp = 24.dp
)

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = NavBarBackground,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                BottomNavItemView(
                    item = item,
                    isSelected = currentRoute == item.route,
                    onClick = { onItemClick(item.route) }
                )
            }
        }
    }
}

@Composable
fun BottomNavItemView(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) AccentPink else TextSecondary,
        animationSpec = tween(durationMillis = 300),
        label = "iconColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(60.dp)
    ) {
        // Glow effect for selected item
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .blur(16.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                AccentPink.copy(alpha = 0.6f),
                                AccentViolet.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }

        Surface(
            onClick = onClick,
            modifier = Modifier
                .size(52.dp),
            shape = RoundedCornerShape(16.dp),
            color = if (isSelected) AccentPink.copy(alpha = 0.15f) else Color.Transparent
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = animatedColor,
                    modifier = Modifier
                        .size(item.iconSize * scale)
                )
            }
        }
    }
}
