package com.vga.spinwheel.ui.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsRoute(
    onBack: () -> Unit,
    onShareClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onRateClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    SettingsScreen(
        onBack = onBack,
        onShareClick = onShareClick,
        onLanguageClick = onLanguageClick,
        onRateClick = onRateClick,
        modifier = modifier,
        viewModel = viewModel,
    )
}
