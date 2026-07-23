package com.vga.spinwheel.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.vga.spinwheel.R
import com.vga.spinwheel.advertisement.NativeAdSlot
import com.vga.spinwheel.firebase.Remote
import com.vga.spinwheel.platform.IapLauncher
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
    var navigationPending by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var isPremium by remember { mutableStateOf(IapLauncher.isPremium()) }
    val nativeHomeConfigured = remember { Remote.instance.isAdEnabled("native_home") }
    val showNativeHome = nativeHomeConfigured && !isPremium

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isPremium = IapLauncher.isPremium()
                navigationPending = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = stringResource(R.string.screen_home),
                navigationIcon = SpinIconGlyph.Settings,
                navigationDescription = stringResource(R.string.content_description_settings),
                onNavigationClick = onSettingsClick,
            ) {
                SpinIconButton(
                    glyph = SpinIconGlyph.Crown,
                    contentDescription = stringResource(R.string.content_description_premium),
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
            val adInsertionIndex = 4
            val beforeAd = homeFeatures.take(adInsertionIndex)
            val afterAd = homeFeatures.drop(adInsertionIndex)

            items(beforeAd, key = { it.screen.route }) { item ->
                SpinFeatureCard(
                    title = stringResource(featureTitleRes(item.screen)),
                    style = item.style,
                    onClick = {
                        if (!navigationPending) {
                            navigationPending = true
                            onFeatureClick(item.screen)
                        }
                    },
                    modifier = Modifier.aspectRatio(1.18f),
                )
            }

            if (showNativeHome) {
                item(
                    key = "ad_native_home",
                    span = { GridItemSpan(maxLineSpan) },
                ) {
                    NativeAdSlot(placement = "native_home")
                }
            }

            items(afterAd, key = { it.screen.route }) { item ->
                SpinFeatureCard(
                    title = stringResource(featureTitleRes(item.screen)),
                    style = item.style,
                    onClick = {
                        if (!navigationPending) {
                            navigationPending = true
                            onFeatureClick(item.screen)
                        }
                    },
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

@StringRes
private fun featureTitleRes(screen: Screen): Int = when (screen) {
    Screen.Wheel -> R.string.feature_wheel
    Screen.Finger -> R.string.feature_finger
    Screen.Coin -> R.string.feature_coin
    Screen.Team -> R.string.feature_team
    Screen.Number -> R.string.feature_number
    Screen.Drawing -> R.string.feature_drawing
    Screen.Bottle -> R.string.feature_bottle
    Screen.Dice -> R.string.feature_dice
    Screen.Card -> R.string.feature_card
    else -> R.string.screen_home
}

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
