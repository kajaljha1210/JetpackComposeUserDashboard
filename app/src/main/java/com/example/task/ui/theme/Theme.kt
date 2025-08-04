package com.example.task.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val DarkColors = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryDarkColor,
    background = BackgroundColor,
    onBackground = OnBackgroundDarkColor
)

private val LightColors = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryLightColor,
    background = BackgroundColor,
    onBackground = OnBackgroundLightColor
)

@Composable
fun MyAppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
