package com.example.week3_assignment.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

private val SmartColors = lightColorScheme(
    primary = Teal,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    surface = androidx.compose.ui.graphics.Color.White,
    background = PageBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = CardBorder
)

private val SmartTypography = Typography(
    headlineMedium = TextStyle(fontSize = 27.sp, fontWeight = FontWeight.Bold),
    headlineSmall = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
    titleLarge = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
    bodyMedium = TextStyle(fontSize = 13.sp),
    labelMedium = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium)
)

@Composable
fun SmartHomeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SmartColors,
        typography = SmartTypography,
        content = content
    )
}
