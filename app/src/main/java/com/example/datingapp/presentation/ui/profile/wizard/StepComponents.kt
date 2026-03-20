package com.example.datingapp.presentation.ui.profile.wizard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.datingapp.domain.model.AvailableInterests
import com.example.datingapp.presentation.viewmodel.DraftProfile

// Design colors
private val BackgroundStart = Color(0xFFFFF5F7)
private val BackgroundEnd = Color(0xFFF5F0FF)
private val CardBackground = Color(0xFFFFFFFF)
private val AccentPink = Color(0xFFE91E63)
private val AccentViolet = Color(0xFF9C27B0)
private val TextPrimary = Color(0xFF2D2D2D)
private val TextSecondary = Color(0xFF757575)
private val BorderColor = Color(0xFFE0E0E0)

// ==================== Step 1: Basic Information ====================

@Composable
fun Step1BasicInfo(
    draft: DraftProfile,
    onNameChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onOccupationChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        Text(
            text = "Основная информация",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Text(
            text = "Заполните обязательные поля, чтобы продолжить",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Name (Required)
        WizardTextField(
            value = draft.name,
            onValueChange = onNameChange,
            label = "Имя *",
            placeholder = "Как вас зовут?",
            leadingIcon = Icons.Rounded.Person,
            isRequired = true
        )

        // City (Required)
        WizardTextField(
            value = draft.city,
            onValueChange = onCityChange,
            label = "Город *",
            placeholder = "Где вы живёте?",
            leadingIcon = Icons.Rounded.LocationOn,
            isRequired = true
        )

        // Bio (Required)
        WizardTextField(
            value = draft.bio,
            onValueChange = onBioChange,
            label = "О себе *",
            placeholder = "Расскажите о себе...",
            leadingIcon = Icons.Rounded.Edit,
            isRequired = true,
            singleLine = false,
            maxLines = 4
        )

        // Occupation (Optional)
        WizardTextField(
            value = draft.occupation,
            onValueChange = onOccupationChange,
            label = "Профессия",
            placeholder = "Чем вы занимаетесь?",
            leadingIcon = Icons.Rounded.Work,
            isRequired = false
        )

        Spacer(modifier = Modifier.height(80.dp)) // Space for bottom button
    }
}

@Composable
private fun WizardTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    isRequired: Boolean,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    val isError = isRequired && value.isBlank()
    val borderColor = when {
        value.isNotBlank() -> AccentPink
        else -> BorderColor
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder, color = TextSecondary.copy(alpha = 0.6f)) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = if (value.isNotBlank()) AccentPink else TextSecondary
            )
        },
        trailingIcon = {
            if (value.isNotBlank()) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = AccentPink
                )
            }
        },
        singleLine = singleLine,
        maxLines = maxLines,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CardBackground,
            unfocusedContainerColor = CardBackground,
            focusedBorderColor = AccentPink,
            unfocusedBorderColor = borderColor,
            focusedLabelColor = AccentPink,
            unfocusedLabelColor = TextSecondary,
            cursorColor = AccentPink
        )
    )
}

// ==================== Step 2: Photo Management ====================

@Composable
fun Step2Photos(
    photos: List<String>,
    previewIndex: Int,
    onAddPhoto: () -> Unit,
    onRemovePhoto: (String) -> Unit,
    onPreviewIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        Text(
            text = "Фотографии",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Text(
            text = "Добавьте минимум 1 фото, чтобы продолжить",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        // Photo Preview Pager (if photos exist)
        if (photos.isNotEmpty()) {
            PhotoPreviewPager(
                photos = photos,
                currentIndex = previewIndex,
                onIndexChange = onPreviewIndexChange
            )
        }

        // Photo Grid (2x3)
        PhotoGrid(
            photos = photos,
            onAddPhoto = onAddPhoto,
            onRemovePhoto = onRemovePhoto
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun PhotoPreviewPager(
    photos: List<String>,
    currentIndex: Int,
    onIndexChange: (Int) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = currentIndex,
        pageCount = { photos.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        onIndexChange(pagerState.currentPage)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(28.dp))
        ) { page ->
            SubcomposeAsyncImage(
                model = photos[page],
                contentDescription = "Photo ${page + 1}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AccentPink.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AccentPink)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Page indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            photos.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(if (index == pagerState.currentPage) 10.dp else 8.dp)
                        .background(
                            color = if (index == pagerState.currentPage) AccentPink else BorderColor,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
private fun PhotoGrid(
    photos: List<String>,
    onAddPhoto: () -> Unit,
    onRemovePhoto: (String) -> Unit
) {
    val gridItems = (0 until 6).map { index -> photos.getOrNull(index) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = false
    ) {
        itemsIndexed(gridItems) { index, photoUrl ->
            PhotoSlot(
                photoUrl = photoUrl,
                slotNumber = index + 1,
                onAdd = onAddPhoto,
                onRemove = { photoUrl?.let(onRemovePhoto) }
            )
        }
    }
}

@Composable
private fun PhotoSlot(
    photoUrl: String?,
    slotNumber: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(shape)
            .background(CardBackground)
            .border(
                width = 2.dp,
                brush = if (photoUrl != null) {
                    Brush.linearGradient(listOf(AccentPink, AccentViolet))
                } else {
                    Brush.linearGradient(listOf(BorderColor, BorderColor))
                },
                shape = shape
            )
            .clickable { if (photoUrl == null) onAdd() },
        contentAlignment = Alignment.Center
    ) {
        if (photoUrl != null) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Photo $slotNumber",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Remove button
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(28.dp)
                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Remove",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Slot number badge
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp),
                shape = CircleShape,
                color = AccentPink
            ) {
                Text(
                    text = slotNumber.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(6.dp)
                )
            }
        } else {
            // Empty slot
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddAPhoto,
                    contentDescription = "Add photo",
                    tint = AccentPink,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$slotNumber",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
            }
        }
    }
}

// ==================== Step 3: Interests Selection ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Step3Interests(
    selectedInterests: List<String>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onToggleInterest: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredInterests = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            AvailableInterests.all
        } else {
            AvailableInterests.all.filter {
                it.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "Интересы",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Text(
            text = "Выберите минимум 1 интерес (выбрано: ${selectedInterests.size})",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        // Search field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Поиск интересов...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = TextSecondary
                )
            },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "Clear",
                            tint = TextSecondary
                        )
                    }
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                focusedBorderColor = AccentPink,
                unfocusedBorderColor = BorderColor
            )
        )

        // Selected interests preview
        if (selectedInterests.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedInterests.forEach { interest ->
                    AssistChip(
                        onClick = { onToggleInterest(interest) },
                        label = { Text(interest) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Remove",
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = AccentPink.copy(alpha = 0.15f),
                            labelColor = AccentPink,
                            leadingIconContentColor = AccentPink,
                            trailingIconContentColor = AccentPink
                        )
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = BorderColor
            )
        }

        // All interests (scrollable)
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filteredInterests.forEach { interest ->
                val isSelected = interest in selectedInterests

                FilterChip(
                    selected = isSelected,
                    onClick = { onToggleInterest(interest) },
                    label = {
                        Text(
                            text = interest,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    },
                    leadingIcon = if (isSelected) {
                        {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null,
                    shape = RoundedCornerShape(24.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentPink.copy(alpha = 0.15f),
                        selectedLabelColor = AccentPink,
                        selectedLeadingIconColor = AccentPink,
                        containerColor = CardBackground,
                        labelColor = TextPrimary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = BorderColor,
                        selectedBorderColor = AccentPink,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

// ==================== Step 4: Preview ====================

@Composable
fun Step4Preview(
    draft: DraftProfile,
    isSaving: Boolean,
    onChangeClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Предпросмотр",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Так ваш профиль видят другие:",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // User Card Preview (Tinder-style)
        UserCardPreview(
            draft = draft,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Change button
            OutlinedButton(
                onClick = onChangeClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Изменить")
            }

            // Save button
            Button(
                onClick = onSaveClick,
                enabled = !isSaving,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentPink
                )
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Сохранить")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun UserCardPreview(
    draft: DraftProfile,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(32.dp)
    val photos = draft.photoUrls

    // Pager state for swiping through photos
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { if (photos.isEmpty()) 1 else photos.size }
    )

    Card(
        modifier = modifier.clip(shape),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Photo background with HorizontalPager
            if (photos.isNotEmpty()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model = photos[page],
                        contentDescription = "Profile photo ${page + 1}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(AccentPink.copy(alpha = 0.3f), AccentViolet.copy(alpha = 0.3f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            // User info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                // Name and age
                Text(
                    text = "${draft.name}, 25",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Occupation
                if (draft.occupation.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Work,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = draft.occupation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                // City
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = draft.city,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                // Bio
                Text(
                    text = draft.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                    maxLines = 2,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Interests
                if (draft.selectedInterests.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        draft.selectedInterests.take(3).forEach { interest ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = interest,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                        if (draft.selectedInterests.size > 3) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "+${draft.selectedInterests.size - 3}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Photo count indicator (now interactive - shows current page)
            if (photos.size > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    photos.forEachIndexed { index, _ ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (index == pagerState.currentPage) Color.White
                                    else Color.White.copy(alpha = 0.4f)
                                )
                        )
                    }
                }
            }
        }
    }
}

// ==================== Next Button ====================

@Composable
fun WizardNextButton(
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentPink
            )
        ) {
            Text(
                text = "Далее",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null
            )
        }
    }
}
