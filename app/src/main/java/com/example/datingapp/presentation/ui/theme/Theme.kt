package com.example.datingapp.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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

private val ItLightColorScheme = lightColorScheme(
    primary = ItPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDDE3FF),
    onPrimaryContainer = Color(0xFF1F255A),
    secondary = ItSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD8F6E0),
    onSecondaryContainer = Color(0xFF173B25),
    tertiary = Color(0xFF0969DA),
    onTertiary = Color.White,
    background = ItBackgroundLight,
    onBackground = Color(0xFF1F2328),
    surface = ItCardLight,
    onSurface = Color(0xFF1F2328),
    error = ErrorRed,
    onError = Color.White,
    outline = ItBorderLight,
    outlineVariant = Color(0xFFE6EDF3)
)

private val ItDarkColorScheme = darkColorScheme(
    primary = ItPrimaryDark,
    onPrimary = Color(0xFF10153D),
    primaryContainer = Color(0xFF232A62),
    onPrimaryContainer = Color(0xFFDDE3FF),
    secondary = ItSecondaryDark,
    onSecondary = Color(0xFF0E2D1A),
    secondaryContainer = Color(0xFF1B5732),
    onSecondaryContainer = Color(0xFFD8F6E0),
    tertiary = Color(0xFF58A6FF),
    onTertiary = Color(0xFF08213E),
    background = ItBackgroundDark,
    onBackground = Color(0xFFE6EDF3),
    surface = ItCardDark,
    onSurface = Color(0xFFE6EDF3),
    error = ErrorRed,
    onError = Color.White,
    outline = ItBorderDark,
    outlineVariant = Color(0xFF484F58)
)

@Composable
fun DatingAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isItMode: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isItMode && darkTheme -> ItDarkColorScheme
        isItMode && !darkTheme -> ItLightColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalIsItMode provides isItMode) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
