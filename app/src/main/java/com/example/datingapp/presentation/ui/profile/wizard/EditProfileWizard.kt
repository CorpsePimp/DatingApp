package com.example.datingapp.presentation.ui.profile.wizard

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.datingapp.presentation.viewmodel.EditProfileWizardState
import com.example.datingapp.presentation.viewmodel.WizardStep

// Design colors
private val BackgroundStart = Color(0xFFFFF5F7)
private val BackgroundEnd = Color(0xFFF5F0FF)
private val AccentPink = Color(0xFFE91E63)
private val AccentViolet = Color(0xFF9C27B0)
private val TextPrimary = Color(0xFF2D2D2D)
private val TextSecondary = Color(0xFF757575)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileWizard(
    state: EditProfileWizardState,
    onNameChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onOccupationChange: (String) -> Unit,
    onAddPhoto: () -> Unit,
    onRemovePhoto: (String) -> Unit,
    onPreviewIndexChange: (Int) -> Unit,
    onSearchChange: (String) -> Unit,
    onToggleInterest: (String) -> Unit,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    onGoToStep1: () -> Unit,
    onSave: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BackgroundStart, BackgroundEnd)
                )
            )
    ) {
        // Loading overlay
        if (state.isSaving) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                color = AccentPink,
                trackColor = AccentPink.copy(alpha = 0.2f)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar with progress
            WizardTopBar(
                currentStep = state.currentStep,
                progress = state.progress,
                canGoBack = !state.isFirstStep,
                onBack = onPreviousStep,
                onClose = onClose
            )

            // Step indicator
            StepIndicator(
                currentStep = state.currentStep,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            // Step content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = state.currentStep,
                    transitionSpec = {
                        if (targetState.index > initialState.index) {
                            // Moving forward
                            slideInHorizontally { width -> width } + fadeIn() togetherWith
                                    slideOutHorizontally { width -> -width } + fadeOut()
                        } else {
                            // Moving backward
                            slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                    slideOutHorizontally { width -> width } + fadeOut()
                        }
                    },
                    label = "step_animation"
                ) { step ->
                    when (step) {
                        WizardStep.BASIC_INFO -> Step1BasicInfo(
                            draft = state.draft,
                            onNameChange = onNameChange,
                            onCityChange = onCityChange,
                            onBioChange = onBioChange,
                            onOccupationChange = onOccupationChange
                        )
                        WizardStep.PHOTOS -> Step2Photos(
                            photos = state.draft.photoUrls,
                            previewIndex = state.previewPhotoIndex,
                            onAddPhoto = onAddPhoto,
                            onRemovePhoto = onRemovePhoto,
                            onPreviewIndexChange = onPreviewIndexChange
                        )
                        WizardStep.INTERESTS -> Step3Interests(
                            selectedInterests = state.draft.selectedInterests,
                            searchQuery = state.interestSearchQuery,
                            onSearchChange = onSearchChange,
                            onToggleInterest = onToggleInterest
                        )
                        WizardStep.PREVIEW -> Step4Preview(
                            draft = state.draft,
                            isSaving = state.isSaving,
                            onChangeClick = onGoToStep1,
                            onSaveClick = onSave
                        )
                    }
                }
            }

            // Bottom button (except for preview step)
            if (state.currentStep != WizardStep.PREVIEW) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    WizardNextButton(
                        visible = state.canProceedFromCurrentStep,
                        onClick = onNextStep
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WizardTopBar(
    currentStep: WizardStep,
    progress: Float,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onClose: () -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = when (currentStep) {
                        WizardStep.BASIC_INFO -> "Шаг 1 из 4"
                        WizardStep.PHOTOS -> "Шаг 2 из 4"
                        WizardStep.INTERESTS -> "Шаг 3 из 4"
                        WizardStep.PREVIEW -> "Шаг 4 из 4"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary
                )
            },
            navigationIcon = {
                if (canGoBack) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                }
            },
            actions = {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = TextPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Progress bar
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .padding(horizontal = 20.dp),
            color = AccentPink,
            trackColor = AccentPink.copy(alpha = 0.2f),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}

@Composable
private fun StepIndicator(
    currentStep: WizardStep,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WizardStep.entries.forEach { step ->
            val isCompleted = step.index < currentStep.index
            val isCurrent = step == currentStep

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Step circle
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = when {
                        isCompleted -> AccentPink
                        isCurrent -> AccentPink.copy(alpha = 0.2f)
                        else -> Color.White
                    },
                    border = if (!isCompleted && !isCurrent) {
                        BorderStroke(1.dp, AccentPink.copy(alpha = 0.3f))
                    } else null
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                text = "${step.index + 1}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrent) AccentPink else TextSecondary
                            )
                        }
                    }
                }

                // Connector line (except for last step)
                if (step.index < WizardStep.entries.size - 1) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .padding(horizontal = 4.dp)
                            .background(
                                color = if (isCompleted) AccentPink else AccentPink.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(1.dp)
                            )
                    )
                }
            }
        }
    }
}
