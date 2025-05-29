package com.example.foodscanapp.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4FD88F),      // Jasna zieleń neony
    onPrimary = Color.Black,
    secondary = Color(0xFFB1CCC5),    // Miętowy pastel
    onSecondary = Color.Black,
    tertiary = Color(0xFFB5D1CC),     // Jasny teal
    onTertiary = Color.Black,
    background = Color(0xFF121212),   // Ciemny szary
    onBackground = Color(0xFFE9ECEF), // Jasnoszary
    surface = Color(0xFF1E1E1E),      // Ciemny szary (ciemniejszy niż tło)
    onSurface = Color(0xFFE9ECEF),
    error = Color(0xFFFFB4AB),        // Jasny różowy
    primaryContainer = Color(0xFF005127), // Ciemna zieleń
    secondaryContainer = Color(0xFF3F5E5A)  // Ciemny teal
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006D3B),      // Głęboka zieleń
    onPrimary = Color.White,
    secondary = Color(0xFF4A635D),    // Stonowana zieleń/brąz
    onSecondary = Color.White,
    tertiary = Color(0xFF3F5E5A),     // Ciemny teal
    onTertiary = Color.White,
    background = Color(0xFFF8F9FA),   // Bardzo jasny szary
    onBackground = Color(0xFF212529),  // Ciemnoszary (prawie czarny)
    surface = Color(0xFFFFFFFF),      // Czysty biały
    onSurface = Color(0xFF212529),
    error = Color(0xFFBA1A1A),       // Czerwony
    primaryContainer = Color(0xFFA2F5C4), // Jasna zieleń
    secondaryContainer = Color(0xFFCCE4DF)  // Bardzo jasny teal
)

@Composable
fun FoodScanAppTheme(
    darkTheme: Boolean,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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