package com.buildsol.bottolshort.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    background = background,
    surface = surface,
    onBackground = onBackground,
    primary = primary,
    onPrimary = onPrimary,
    outline = outline,
    secondaryContainer = secondaryContainer,
)

@Composable
fun BottolShortTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}
