package com.andef.mycarandef.design.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkGrayColor,
    secondary = DarkGrayColor,
    tertiary = DarkGrayColor,
    background = DarkGrayColor,
    surface = DarkGrayColor,
    onPrimary = WhiteColor,
    onSecondary = WhiteColor,
    onTertiary = WhiteColor,
    onBackground = WhiteColor,
    onSurface = WhiteColor
)

private val LightColorScheme = lightColorScheme(
    primary = WhiteColor,
    secondary = WhiteColor,
    tertiary = WhiteColor,
    background = WhiteColor,
    surface = WhiteColor,
    onPrimary = BlackColor,
    onSecondary = BlackColor,
    onTertiary = BlackColor,
    onBackground = BlackColor,
    onSurface = BlackColor
)

@Composable
fun MyCarAndefTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}