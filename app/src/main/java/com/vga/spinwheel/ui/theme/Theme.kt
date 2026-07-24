package com.vga.spinwheel.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object SpinColors {
    val Background = Color(0xFF292640)
    val BackgroundDeep = Color(0xFF1D1B2E)
    val Card = Color(0xFF2B274C)
    val CardBorder = Color(0x1AFFFFFF)
    val TextPrimary = Color.White
    val TextMuted = Color(0xFFB9B7CF)
    val IconMuted = Color(0xFF8C8BA5)
    val Premium = Color(0xFFFFD21E)
    val Action = Color(0xFFEC9213)
    val Success = Color(0xFF4DFD4D)
    val WarningText = Color(0xFFFFFF33)
    val BlueText = Color(0xFF7A5CFF)
}

object SpinSpacing {
    val ScreenHorizontal = 18.dp
    val ScreenVertical = 14.dp
    val CardGap = 14.dp
    val CardPadding = 12.dp
    val HeaderButton = 44.dp
    val ControlHeight = 48.dp
    val BottomBarHeight = 74.dp
}

object SpinRadius {
    val Card = 18.dp
    val Button = 16.dp
    val Control = 6.dp
    val Sheet = 22.dp
}

private val SpinTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 34.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.ExtraBold,
    ),
    headlineSmall = TextStyle(
        fontSize = 20.sp,
        lineHeight = 34.sp,
        fontWeight = FontWeight.ExtraBold,
    ),
    titleLarge = TextStyle(
        fontSize = 24.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.ExtraBold,
    ),
    titleMedium = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleSmall = TextStyle(
        fontSize = 15.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium,
    ),
    labelLarge = TextStyle(
        fontSize = 15.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold,
    ),
)

private val LightColors: ColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color.White,
    secondary = Color(0xFF0F8B8D),
    onSecondary = Color.White,
    background = SpinColors.Background,
    onBackground = SpinColors.TextPrimary,
    surface = SpinColors.Card,
    onSurface = SpinColors.TextPrimary,
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = SpinTypography,
        content = content,
    )
}
