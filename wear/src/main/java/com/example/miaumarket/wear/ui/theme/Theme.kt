package com.example.miaumarket.wear.ui.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.darkColors

private val DarkColorPalette = darkColors(
    primary = md_theme_dark_primary,
    primaryVariant = md_theme_dark_primaryContainer,
    secondary = md_theme_dark_secondary,
    secondaryVariant = md_theme_dark_secondaryContainer,
    error = md_theme_dark_error,
    background = md_theme_dark_background,
    surface = md_theme_dark_surface
)

@Composable
fun MiauMarketWearTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = DarkColorPalette,
        content = content
    )
}

