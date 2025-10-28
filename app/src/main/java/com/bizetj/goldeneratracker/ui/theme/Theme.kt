package com.bizetj.goldeneratracker.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GoldenPrimary,
    onPrimary = BlackGolden,
    primaryContainer = GoldenPrimaryDark,
    onPrimaryContainer = CreamGolden,

    secondary = GoldenAccent,
    onSecondary = BlackGolden,
    secondaryContainer = GoldenPrimaryLight,
    onSecondaryContainer = BlackGolden,

    tertiary = GoldenPrimaryLight,
    onTertiary = BlackGolden,

    background = BlackGolden,
    onBackground = CreamGolden,

    surface = SurfaceDark,
    onSurface = CreamGolden,
    surfaceVariant = BlackGoldenLight,
    onSurfaceVariant = CreamGoldenDark,

    error = ErrorRed,
    onError = BlackGolden,

    outline = GoldenPrimaryDark,
    outlineVariant = Color(0xFF3D3D3D)
)

private val LightColorScheme = lightColorScheme(
    primary = GoldenPrimary,
    onPrimary = Color.White,
    primaryContainer = GoldenPrimaryLight,
    onPrimaryContainer = BlackGolden,

    secondary = GoldenAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF8E7), // Crème doré très clair pour les cards
    onSecondaryContainer = BlackGolden,

    tertiary = GoldenPrimaryDark,
    onTertiary = Color.White,

    background = CreamGolden, // #FBFBFC
    onBackground = BlackGolden,

    surface = Color(0xFFFFFBF5), // Blanc cassé légèrement doré
    onSurface = BlackGolden,
    surfaceVariant = Color(0xFFFFF4E0), // Beige très clair pour variation
    onSurfaceVariant = Color(0xFF4A4A4A), // Gris foncé pour textes secondaires

    error = ErrorRed,
    onError = Color.White,

    outline = GoldenPrimaryDark,
    outlineVariant = Color(0xFFE0D5C0) // Beige moyen pour bordures
)

@Composable
fun GoldenEraTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}