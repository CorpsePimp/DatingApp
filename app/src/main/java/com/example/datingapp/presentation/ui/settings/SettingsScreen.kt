package com.example.datingapp.presentation.ui.settings

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.datingapp.R
import com.example.datingapp.presentation.ui.theme.BackgroundEnd
import com.example.datingapp.presentation.ui.theme.BackgroundStart
import com.example.datingapp.presentation.ui.theme.GlassWhite
import com.example.datingapp.presentation.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val languageCode by viewModel.languageCode.collectAsState()
    val isItMode by viewModel.isItMode.collectAsState()

    var pushEnabled by remember { mutableStateOf(true) }
    var emailDigestEnabled by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isDarkTheme) {
                        listOf(Color(0xFF1A1A1A), Color(0xFF2D2D2D))
                    } else {
                        listOf(BackgroundStart, BackgroundEnd)
                    }
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cd_back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.settings_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(R.string.settings_version_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                SettingsSectionTitle(stringResource(R.string.settings_section_account))
                SettingsGroupCard {
                    SettingsNavRow(
                        title = stringResource(R.string.settings_row_phone_title),
                        subtitle = stringResource(R.string.settings_row_phone_subtitle),
                        icon = Icons.Outlined.Smartphone,
                        onClick = { }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    SettingsNavRow(
                        title = stringResource(R.string.settings_row_subscription_title),
                        subtitle = stringResource(R.string.settings_row_subscription_subtitle),
                        icon = Icons.Outlined.CardMembership,
                        onClick = { }
                    )
                }

                SettingsSectionTitle(stringResource(R.string.settings_section_appearance))
                SettingsGroupCard {
                    SettingsToggleRow(
                        title = stringResource(R.string.theme_title),
                        subtitle = null,
                        icon = Icons.Outlined.DarkMode,
                        checked = isDarkTheme,
                        onCheckedChange = { viewModel.toggleTheme(it) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    SettingsToggleRow(
                        title = stringResource(R.string.settings_it_mode_title),
                        subtitle = stringResource(R.string.settings_it_mode_subtitle),
                        icon = Icons.Outlined.Code,
                        checked = isItMode,
                        onCheckedChange = { viewModel.setItMode(it) }
                    )
                }

                SettingsSectionTitle(stringResource(R.string.settings_section_language))
                SettingsGroupCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Language,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = stringResource(R.string.language_title),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(GlassWhite.copy(alpha = 0.5f))
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LanguageButton(
                                text = stringResource(id = R.string.language_ru),
                                isSelected = languageCode == "ru",
                                onClick = { viewModel.setLanguage("ru") }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            LanguageButton(
                                text = stringResource(id = R.string.language_en),
                                isSelected = languageCode == "en",
                                onClick = { viewModel.setLanguage("en") }
                            )
                        }
                    }
                }

                SettingsSectionTitle(stringResource(R.string.settings_section_notifications))
                SettingsGroupCard {
                    SettingsToggleRow(
                        title = stringResource(R.string.settings_notif_push_title),
                        subtitle = stringResource(R.string.settings_notif_push_subtitle),
                        icon = Icons.Outlined.NotificationsNone,
                        checked = pushEnabled,
                        onCheckedChange = { pushEnabled = it }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    SettingsToggleRow(
                        title = stringResource(R.string.settings_notif_email_title),
                        subtitle = stringResource(R.string.settings_notif_email_subtitle),
                        icon = Icons.Outlined.Email,
                        checked = emailDigestEnabled,
                        onCheckedChange = { emailDigestEnabled = it }
                    )
                }

                SettingsSectionTitle(stringResource(R.string.settings_section_privacy))
                SettingsGroupCard {
                    SettingsNavRow(
                        title = stringResource(R.string.settings_privacy_visibility),
                        subtitle = null,
                        icon = Icons.Outlined.Visibility,
                        onClick = { }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    SettingsNavRow(
                        title = stringResource(R.string.settings_privacy_blocked),
                        subtitle = null,
                        icon = Icons.Outlined.Block,
                        onClick = { }
                    )
                }

                SettingsSectionTitle(stringResource(R.string.settings_section_about))
                SettingsGroupCard {
                    SettingsNavRow(
                        title = stringResource(R.string.settings_version_title),
                        subtitle = stringResource(R.string.settings_version_subtitle),
                        icon = Icons.Outlined.Info,
                        onClick = { },
                        showChevron = false
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    SettingsNavRow(
                        title = stringResource(R.string.settings_legal_title),
                        subtitle = null,
                        icon = Icons.Outlined.Policy,
                        onClick = { }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
private fun SettingsGroupCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 4.dp), content = content)
    }
}

@Composable
private fun SettingsNavRow(
    title: String,
    subtitle: String?,
    icon: ImageVector,
    onClick: () -> Unit,
    showChevron: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (showChevron) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    subtitle: String?,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}

@Composable
fun LanguageButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        elevation = if (isSelected) ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}
