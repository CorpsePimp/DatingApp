package com.example.datingapp.presentation.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import com.example.datingapp.domain.model.ChatContacts

// Data classes
data class NewMatch(
    val id: String,
    val name: String,
    val photoUrl: String,
    val isOnline: Boolean = false
)

data class ChatPreview(
    val id: String,
    val name: String,
    val photoUrl: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val isVerified: Boolean = false,
    val isOnline: Boolean = false
)

// Light theme colors
private val BackgroundStart = Color(0xFFFFF5F7)
private val BackgroundEnd = Color(0xFFF5F0FF)
private val CardBackground = Color(0xFFFFFFFF)
private val GlassOverlay = Color(0x15000000)
private val AccentPink = Color(0xFFE91E63)
private val AccentViolet = Color(0xFF9C27B0)
private val OnlineGreen = Color(0xFF4CAF50)
private val TextPrimary = Color(0xFF2D2D2D)
private val TextSecondary = Color(0xFF757575)

@Composable
fun ChatListScreen(
    onChatClick: (String) -> Unit = {},
    onMatchClick: (String) -> Unit = {}
) {
    val newMatches = remember { getDummyMatches() }
    val chats = remember { getDummyChats() }

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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Search Bar
            item {
                SearchBar(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                )
            }

            // New Matches Section
            item {
                NewMatchesSection(
                    matches = newMatches,
                    onMatchClick = onMatchClick
                )
            }

            // Messages Header
            item {
                Text(
                    text = "Сообщения",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            // Chat List
            items(chats) { chat ->
                ChatItem(
                    chat = chat,
                    onClick = { onChatClick(chat.id) }
                )
            }
        }
    }
}


@Composable
private fun SearchBar(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = CardBackground,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                tint = TextSecondary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = if (searchText.isEmpty()) "Поиск" else searchText,
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun NewMatchesSection(
    matches: List<NewMatch>,
    onMatchClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = "Новые пары",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(matches) { match ->
                NewMatchItem(
                    match = match,
                    onClick = { onMatchClick(match.id) }
                )
            }
        }
    }
}

@Composable
private fun NewMatchItem(
    match: NewMatch,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box {
            // Gradient ring
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(AccentPink, AccentViolet)
                        ),
                        shape = CircleShape
                    )
                    .padding(3.dp)
            ) {
                AsyncImage(
                    model = match.photoUrl,
                    contentDescription = match.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(2.dp, CardBackground, CircleShape)
                )
            }

            // Online indicator
            if (match.isOnline) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-2).dp, y = (-2).dp)
                        .background(OnlineGreen, CircleShape)
                        .border(2.dp, CardBackground, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = match.name,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ChatItem(
    chat: ChatPreview,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 3.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with online indicator
            Box {
                AsyncImage(
                    model = chat.photoUrl,
                    contentDescription = chat.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                )

                if (chat.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.BottomEnd)
                            .background(OnlineGreen, CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Chat info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    if (chat.isVerified) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Rounded.Verified,
                            contentDescription = "Verified",
                            tint = Color(0xFF1DA1F2),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = chat.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Time and badge
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = chat.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (chat.unreadCount > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = chat.unreadCount.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

// Dummy data - synced with ChatContacts
private fun getDummyMatches(): List<NewMatch> = listOf(
    NewMatch("1", "Анна", "https://picsum.photos/seed/match1/200/200", true),
    NewMatch("2", "Мария", "https://picsum.photos/seed/match2/200/200", false),
    NewMatch("3", "Елена", "https://picsum.photos/seed/match3/200/200", true),
    NewMatch("4", "София", "https://picsum.photos/seed/match4/200/200", false),
    NewMatch("5", "Дарья", "https://picsum.photos/seed/match5/200/200", true),
    NewMatch("6", "Алиса", "https://picsum.photos/seed/match6/200/200", false)
)

private fun getDummyChats(): List<ChatPreview> = ChatContacts.contacts.mapIndexed { index, contact ->
    val lastMessages = listOf(
        "Привет! Как дела? 😊",
        "Давай встретимся завтра?",
        "Отличное фото!",
        "Спасибо за приятный вечер 💕",
        "Ты где?"
    )
    val timestamps = listOf("2 мин", "15 мин", "1 час", "3 часа", "Вчера")
    val unreadCounts = listOf(3, 1, 0, 0, 5)

    ChatPreview(
        id = contact.id,
        name = contact.name,
        photoUrl = contact.photoUrl,
        lastMessage = lastMessages.getOrElse(index) { "Начните общение!" },
        timestamp = timestamps.getOrElse(index) { "Только что" },
        unreadCount = unreadCounts.getOrElse(index) { 0 },
        isVerified = contact.isVerified,
        isOnline = contact.isOnline
    )
}
