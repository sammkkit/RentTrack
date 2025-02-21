package com.samapp.renttrack.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1E88E5),  // Blue (FAB & selected bottom nav icon)
    onPrimary = Color.White,       // White text/icons on primary
    background = Color(0xFFFAF5EE), // Light cream background
    onBackground = Color.Black,    // Black text
    surface = Color.White,         // Card background
    onSurface = Color.Black,       // Text on card
    secondary = Color(0xFF000000), // Black (price button)
    onSecondary = Color.White      // White text on price button
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),   // Lighter blue for dark mode
    onPrimary = Color.Black,       // Dark text on primary
    background = Color(0xFF121212), // Dark background
    onBackground = Color.White,    // White text
    surface = Color(0xFF1E1E1E),   // Darker surface for cards
    onSurface = Color.White,       // White text on card
    secondary = Color(0xFFFFFFFF), // White (price button in dark mode)
    onSecondary = Color.Black      // Black text on price button
)


@Composable
fun RentTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}