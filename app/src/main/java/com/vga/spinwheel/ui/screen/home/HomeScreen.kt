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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.vga.spinwheel.R
import com.vga.spinwheel.advertisement.NativeAdSlot
import com.vga.spinwheel.firebase.Remote
import com.vga.spinwheel.platform.IapLauncher
import com.vga.spinwheel.ui.components.SpinFeatureCard
import com.vga.spinwheel.ui.components.SpinFeatureCardStyle
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
                title = stringResource(R.string.spinwheel),
                navigationIcon = SpinIconGlyph.Settings,
                navigationDescription = stringResource(R.string.settings),
                onNavigationClick = onSettingsClick,
            ) {
                SpinIconButton(
                    glyph = SpinIconGlyph.Crown,
                    contentDescription = stringResource(R.string.vip),
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
                    title = stringResource(item.screen.titleRes),
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
                    title = stringResource(item.screen.titleRes),
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

private val homeFeatures = listOf(
    HomeFeature(
        screen = Screen.Wheel,
        style = SpinFeatureCardStyle(
            backgroundRes = R.drawable.home_game_wheel,
        ),
    ),
    HomeFeature(
        screen = Screen.Finger,
        style = SpinFeatureCardStyle(
            backgroundRes = R.drawable.home_game_finger,
        ),
    ),
    HomeFeature(
        screen = Screen.Coin,
        style = SpinFeatureCardStyle(
            backgroundRes = R.drawable.home_game_coin,
            titleColor = SpinColors.WarningText,
        ),
    ),
    HomeFeature(
        screen = Screen.Team,
        style = SpinFeatureCardStyle(
            backgroundRes = R.drawable.home_game_team,
            titleColor = SpinColors.Success,
        ),
    ),
    HomeFeature(
        screen = Screen.Number,
        style = SpinFeatureCardStyle(
            backgroundRes = R.drawable.home_game_number,
        ),
    ),
    HomeFeature(
        screen = Screen.Drawing,
        style = SpinFeatureCardStyle(
            backgroundRes = R.drawable.home_game_drawing,
            titleColor = SpinColors.BlueText,
        ),
    ),
    HomeFeature(
        screen = Screen.Bottle,
        style = SpinFeatureCardStyle(
            backgroundRes = R.drawable.home_game_bottle,
            titleColor = SpinColors.WarningText,
        ),
    ),
    HomeFeature(
        screen = Screen.Dice,
        style = SpinFeatureCardStyle(
            backgroundRes = R.drawable.home_game_dice,
        ),
    ),
    HomeFeature(
        screen = Screen.Card,
        style = SpinFeatureCardStyle(
            backgroundRes = R.drawable.home_game_card,
        ),
    ),
)
