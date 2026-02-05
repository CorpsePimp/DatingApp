package com.example.datingapp.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DeepPink,
    onPrimary = Color.White,
    primaryContainer = SoftPink,
    onPrimaryContainer = TextPrimary,

    secondary = MediumViolet,
    onSecondary = Color.White,
    secondaryContainer = LightViolet,
    onSecondaryContainer = TextPrimary,

    tertiary = DeepPink,
    onTertiary = Color.White,

    background = BackgroundStart,
    onBackground = TextPrimary,

    surface = GlassWhite,
    onSurface = TextPrimary,

    error = ErrorRed,
    onError = Color.White,

    outline = GlassBorder,
    outlineVariant = TextHint
)

private val DarkColorScheme = darkColorScheme(
    primary = SoftPink,
    onPrimary = TextPrimary,
    primaryContainer = DeepPink,
    onPrimaryContainer = Color.White,

    secondary = LightViolet,
    onSecondary = TextPrimary,
    secondaryContainer = MediumViolet,
    onSecondaryContainer = Color.White,

    tertiary = SoftPink,
    onTertiary = TextPrimary,

    background = Color(0xFF1A1A1A),
    onBackground = Color.White,

    surface = Color(0x33FFFFFF),
    onSurface = Color.White,

    error = ErrorRed,
    onError = Color.White,

    outline = Color(0x33FFFFFF),
    outlineVariant = Color(0x66FFFFFF)
)

@Composable
fun DatingAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
