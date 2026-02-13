package com.example.datingapp.presentation.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*

// Design system colors
private val BackgroundStart = Color(0xFFFFF5F7)
private val BackgroundEnd = Color(0xFFF5F0FF)
private val CardBackground = Color(0xFFFFFFFF)
private val AccentPink = Color(0xFFE91E63)
private val AccentViolet = Color(0xFF9C27B0)
private val AccentOrange = Color(0xFFFF9800)
private val AccentBlue = Color(0xFF2196F3)
private val AccentGreen = Color(0xFF4CAF50)
private val TextPrimary = Color(0xFF2D2D2D)
private val TextSecondary = Color(0xFF757575)

// Flowwow brand colors
private val FlowwowPrimary = Color(0xFFFF6B9D)
private val FlowwowSecondary = Color(0xFFFF8E53)

/**
 * Data class for activity/feature cards
 */
data class ActivityCard(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val gradientColors: List<Color>,
    val badgeText: String? = null
)

/**
 * Data class for local venue
 */
data class LocalVenue(
    val id: String,
    val name: String,
    val category: String,
    val imageUrl: String,
    val rating: Float? = null,
    val isSponsored: Boolean = false
)

@Composable
fun ActivitiesScreen(
    onCardClick: (String) -> Unit = {},
    onVenueClick: (String) -> Unit = {},
    onAdClick: (String) -> Unit = {}
) {
    val activities = remember { getActivityCards() }
    val venues = remember { getLocalVenues() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BackgroundStart, BackgroundEnd)
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section Header
            item {
                Text(
                    text = "Активности",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            // Top Feature Card - "Topics for Conversation"
            item {
                FeatureCard(
                    title = "Темы для разговора",
                    subtitle = "Не знаешь о чём поговорить? Мы подготовили 100+ интересных тем!",
                    icon = Icons.Rounded.Chat,
                    gradientColors = listOf(AccentPink, AccentViolet),
                    onClick = { onCardClick("topics") },
                    modifier = Modifier.fillMaxWidth(),
                    height = 160.dp
                )
            }

            // Secondary Row - Two medium cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Dating Places
                    FeatureCard(
                        title = "Места для свиданий",
                        subtitle = "Лучшие локации рядом",
                        icon = Icons.Rounded.LocationOn,
                        gradientColors = listOf(AccentOrange, Color(0xFFFF5722)),
                        onClick = { onCardClick("places") },
                        modifier = Modifier.weight(1f),
                        height = 140.dp,
                        isCompact = true
                    )

                    // Daily Quests
                    FeatureCard(
                        title = "Ежедневные квесты",
                        subtitle = "Выполняй и получай бонусы",
                        icon = Icons.Rounded.EmojiEvents,
                        gradientColors = listOf(AccentBlue, Color(0xFF3F51B5)),
                        onClick = { onCardClick("quests") },
                        modifier = Modifier.weight(1f),
                        height = 140.dp,
                        isCompact = true,
                        badgeText = "3"
                    )
                }
            }

            // Partner Ad Banner - Flowwow
            item {
                PartnerAdBanner(
                    title = "Flowwow",
                    subtitle = "Доставка цветов и подарков для вашей половинки 💐",
                    imageUrl = "https://images.unsplash.com/photo-1490750967868-88aa4486c946?w=200&h=200&fit=crop",
                    gradientColors = listOf(FlowwowPrimary, FlowwowSecondary),
                    onClick = { onAdClick("flowwow") }
                )
            }

            // More Activities Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Ice Breakers
                    FeatureCard(
                        title = "Ледоколы",
                        subtitle = "Игры для знакомства",
                        icon = Icons.Rounded.AcUnit,
                        gradientColors = listOf(Color(0xFF00BCD4), Color(0xFF0097A7)),
                        onClick = { onCardClick("icebreakers") },
                        modifier = Modifier.weight(1f),
                        height = 120.dp,
                        isCompact = true
                    )

                    // Date Ideas
                    FeatureCard(
                        title = "Идеи для свидания",
                        subtitle = "Вдохновляйся",
                        icon = Icons.Rounded.Lightbulb,
                        gradientColors = listOf(AccentGreen, Color(0xFF388E3C)),
                        onClick = { onCardClick("ideas") },
                        modifier = Modifier.weight(1f),
                        height = 120.dp,
                        isCompact = true
                    )
                }
            }

            // Local Venues Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Рядом с вами",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    TextButton(onClick = { onCardClick("all_venues") }) {
                        Text(
                            text = "Все",
                            color = AccentPink,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = Icons.Rounded.ChevronRight,
                            contentDescription = null,
                            tint = AccentPink,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Local Venues LazyRow
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) {
                    items(venues) { venue ->
                        VenueCard(
                            name = venue.name,
                            category = venue.category,
                            imageUrl = venue.imageUrl,
                            rating = venue.rating,
                            isSponsored = venue.isSponsored,
                            onClick = { onVenueClick(venue.id) }
                        )
                    }
                }
            }

            // Second Ad Banner - Restaurant Partner
            item {
                PartnerAdBanner(
                    title = "Романтический ужин",
                    subtitle = "Скидка 20% на ужин для двоих в ресторанах-партнёрах",
                    imageUrl = "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=200&h=200&fit=crop",
                    gradientColors = listOf(Color(0xFF8E24AA), Color(0xFF5E35B1)),
                    onClick = { onAdClick("restaurant_partner") }
                )
            }

            // Compatibility Games Section
            item {
                Text(
                    text = "Игры на совместимость",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Games Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Quiz
                    FeatureCard(
                        title = "Тест совместимости",
                        subtitle = "Узнай свой %",
                        icon = Icons.Rounded.Psychology,
                        gradientColors = listOf(Color(0xFFE91E63), Color(0xFFC2185B)),
                        onClick = { onCardClick("compatibility_quiz") },
                        modifier = Modifier.weight(1f),
                        height = 120.dp,
                        isCompact = true
                    )

                    // Truth or Dare
                    FeatureCard(
                        title = "Правда или Действие",
                        subtitle = "Классика!",
                        icon = Icons.Rounded.Casino,
                        gradientColors = listOf(Color(0xFFFF5722), Color(0xFFE64A19)),
                        onClick = { onCardClick("truth_or_dare") },
                        modifier = Modifier.weight(1f),
                        height = 120.dp,
                        isCompact = true
                    )
                }
            }
        }
    }
}

/**
 * Feature Card Component
 * Used for activity/feature cards in the bento grid
 */
@Composable
private fun FeatureCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 140.dp,
    isCompact: Boolean = false,
    badgeText: String? = null
) {
    val shape = RoundedCornerShape(28.dp)

    Surface(
        modifier = modifier
            .height(height)
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Color.White.copy(alpha = 0.3f)),
                onClick = onClick
            ),
        shape = shape,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(gradientColors),
                    shape = shape
                )
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.2f),
                    shape
                )
                .padding(16.dp)
        ) {
            // Icon (top right)
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(if (isCompact) 48.dp else 64.dp)
            )

            // Badge (top right, next to icon)
            if (badgeText != null) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-40).dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = gradientColors.first(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // Content (bottom left)
            Column(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Text(
                    text = title,
                    style = if (isCompact) {
                        MaterialTheme.typography.titleMedium
                    } else {
                        MaterialTheme.typography.titleLarge
                    },
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = if (isCompact) 20.sp else 24.sp
                )

                if (!isCompact || subtitle.length < 25) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f),
                        maxLines = 2
                    )
                }
            }
        }
    }
}

// Dummy data for activity cards
private fun getActivityCards(): List<ActivityCard> = listOf(
    ActivityCard(
        id = "topics",
        title = "Темы для разговора",
        subtitle = "100+ интересных тем для общения",
        icon = Icons.Rounded.Chat,
        gradientColors = listOf(AccentPink, AccentViolet)
    ),
    ActivityCard(
        id = "places",
        title = "Места для свиданий",
        subtitle = "Лучшие локации рядом с вами",
        icon = Icons.Rounded.LocationOn,
        gradientColors = listOf(AccentOrange, Color(0xFFFF5722))
    ),
    ActivityCard(
        id = "quests",
        title = "Ежедневные квесты",
        subtitle = "Выполняй задания и получай бонусы",
        icon = Icons.Rounded.EmojiEvents,
        gradientColors = listOf(AccentBlue, Color(0xFF3F51B5)),
        badgeText = "3"
    )
)

// Dummy data for local venues with real image URLs
private fun getLocalVenues(): List<LocalVenue> = listOf(
    LocalVenue(
        id = "1",
        name = "Кофемания",
        category = "Кофейня",
        imageUrl = "https://images.unsplash.com/photo-1554118811-1e0d58224f24?w=400&h=500&fit=crop",
        rating = 4.8f,
        isSponsored = true
    ),
    LocalVenue(
        id = "2",
        name = "Пушкин",
        category = "Ресторан",
        imageUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=400&h=500&fit=crop",
        rating = 4.9f,
        isSponsored = false
    ),
    LocalVenue(
        id = "3",
        name = "Wine & Crab",
        category = "Винный бар",
        imageUrl = "https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?w=400&h=500&fit=crop",
        rating = 4.7f,
        isSponsored = true
    ),
    LocalVenue(
        id = "4",
        name = "Гараж",
        category = "Бар",
        imageUrl = "https://images.unsplash.com/photo-1572116469696-31de0f17cc34?w=400&h=500&fit=crop",
        rating = 4.5f,
        isSponsored = false
    ),
    LocalVenue(
        id = "5",
        name = "Москва-Сити",
        category = "Смотровая",
        imageUrl = "https://images.unsplash.com/photo-1513326738677-b964603b136d?w=400&h=500&fit=crop",
        rating = 4.6f,
        isSponsored = false
    )
)
