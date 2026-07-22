package com.vga.spinwheel.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.components.SpinFeatureCard
import com.vga.spinwheel.ui.components.SpinFeatureCardStyle
import com.vga.spinwheel.ui.components.SpinFeatureVisual
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.nav.Screen
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun HomeScreen(
    onFeatureClick: (Screen) -> Unit,
    onSettingsClick: () -> Unit,
    onPaymentClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = "Spin Wheel",
                navigationIcon = SpinIconGlyph.Settings,
                navigationDescription = "Cai dat",
                onNavigationClick = onSettingsClick,
            ) {
                SpinIconButton(
                    glyph = SpinIconGlyph.Crown,
                    contentDescription = "Pro",
                    onClick = onPaymentClick,
                    tint = SpinColors.Premium,
                )
            }
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(SpinColors.Background),
            contentPadding = PaddingValues(
                start = SpinSpacing.ScreenHorizontal,
                top = innerPadding.calculateTopPadding() + 8.dp,
                end = SpinSpacing.ScreenHorizontal,
                bottom = 28.dp,
            ),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(SpinSpacing.CardGap),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(SpinSpacing.CardGap),
        ) {
            items(homeFeatures, key = { it.screen.route }) { item ->
                SpinFeatureCard(
                    title = item.screen.title,
                    style = item.style,
                    onClick = { onFeatureClick(item.screen) },
                    modifier = Modifier.aspectRatio(1.18f),
                )
            }
        }
    }
}

private data class HomeFeature(
    val screen: Screen,
    val style: SpinFeatureCardStyle,
)

private val homeFeatures = listOf(
    HomeFeature(
        screen = Screen.Wheel,
        style = SpinFeatureCardStyle(
            visual = SpinFeatureVisual.Wheel,
            gradient = listOf(Color(0xFF4F3B78), Color(0xFF292640)),
        ),
    ),
    HomeFeature(
        screen = Screen.Finger,
        style = SpinFeatureCardStyle(
            visual = SpinFeatureVisual.Finger,
            gradient = listOf(Color(0xFFB83B5E), Color(0xFF292640)),
        ),
    ),
    HomeFeature(
        screen = Screen.Coin,
        style = SpinFeatureCardStyle(
            visual = SpinFeatureVisual.Coin,
            gradient = listOf(Color(0xFFE29C32), Color(0xFF292640)),
            titleColor = SpinColors.WarningText,
        ),
    ),
    HomeFeature(
        screen = Screen.Team,
        style = SpinFeatureCardStyle(
            visual = SpinFeatureVisual.Team,
            gradient = listOf(Color(0xFF1C728E), Color(0xFF292640)),
            titleColor = SpinColors.Success,
        ),
    ),
    HomeFeature(
        screen = Screen.Number,
        style = SpinFeatureCardStyle(
            visual = SpinFeatureVisual.Number,
            gradient = listOf(Color(0xFF111111), Color(0xFF292640)),
        ),
    ),
    HomeFeature(
        screen = Screen.Drawing,
        style = SpinFeatureCardStyle(
            visual = SpinFeatureVisual.Drawing,
            gradient = listOf(Color(0xFFD33C5E), Color(0xFF292640)),
            titleColor = SpinColors.BlueText,
        ),
    ),
    HomeFeature(
        screen = Screen.Bottle,
        style = SpinFeatureCardStyle(
            visual = SpinFeatureVisual.Bottle,
            gradient = listOf(Color(0xFF6C3272), Color(0xFF292640)),
            titleColor = SpinColors.WarningText,
        ),
    ),
    HomeFeature(
        screen = Screen.Dice,
        style = SpinFeatureCardStyle(
            visual = SpinFeatureVisual.Dice,
            gradient = listOf(Color(0xFFCC447A), Color(0xFF292640)),
        ),
    ),
    HomeFeature(
        screen = Screen.Card,
        style = SpinFeatureCardStyle(
            visual = SpinFeatureVisual.Card,
            gradient = listOf(Color(0xFF329CA8), Color(0xFF292640)),
        ),
    ),
)
