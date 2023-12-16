package com.susu.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun SusuTheme(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalTypography provides Typography,
        LocalColorScheme provides LightColorScheme,
        LocalSpacing provides Spacing,
    ) {
        MaterialTheme(
            colorScheme = lightColorScheme(
                background = SusuTheme.colorScheme.background15,
            ),
            content = content,
        )
    }
}

object SusuTheme {
    val typography: SusuTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val colorScheme: SusuColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val spacing: SusuSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current
}
