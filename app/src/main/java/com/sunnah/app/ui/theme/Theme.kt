package com.sunnah.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    secondary = SageGreen,
    tertiary = DeepOlive,
    background = CreamBackground,
    surface = CreamBackground,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = DeepOlive,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    onBackground = DeepOlive,
    onSurface = DeepOlive,
)

@Composable
fun SunnahAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
