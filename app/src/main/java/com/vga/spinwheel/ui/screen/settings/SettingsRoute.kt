package com.vga.spinwheel.ui.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinSettingRow
import com.vga.spinwheel.ui.components.SpinToggle
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun SettingsRoute(
    onBack: () -> Unit,
    onShareClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onPremiumClick: () -> Unit,
    onRateClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = stringResource(R.string.screen_settings),
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = stringResource(R.string.content_description_back),
                onNavigationClick = onBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    horizontal = SpinSpacing.ScreenHorizontal,
                    vertical = 18.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SpinSettingRow(
                title = stringResource(R.string.settings_premium),
                onClick = onPremiumClick,
                leading = { SettingsGlyph(SpinIconGlyph.Crown) },
            )
            SpinSettingRow(
                title = stringResource(R.string.settings_share),
                onClick = onShareClick,
                leading = { SettingsGlyph(SpinIconGlyph.ChevronRight) },
            )
            SpinSettingRow(
                title = stringResource(R.string.settings_language),
                onClick = onLanguageClick,
                leading = { SettingsGlyph(SpinIconGlyph.Home) },
            )
            SpinSettingRow(
                title = stringResource(R.string.settings_rate),
                onClick = onRateClick,
                leading = { SettingsGlyph(SpinIconGlyph.Crown) },
            )
            SpinSettingRow(
                title = stringResource(R.string.settings_music),
                leading = { SettingsGlyph(SpinIconGlyph.Settings) },
                trailing = {
                    SpinToggle(
                        checked = state.musicEnabled,
                        onCheckedChange = viewModel::setMusicEnabled,
                    )
                },
            )
            SpinSettingRow(
                title = stringResource(R.string.settings_game_sound),
                leading = { SettingsGlyph(SpinIconGlyph.Check) },
                trailing = {
                    SpinToggle(
                        checked = state.gameSoundEnabled,
                        onCheckedChange = viewModel::setGameSoundEnabled,
                    )
                },
            )
            SpinSettingRow(
                title = stringResource(R.string.settings_vibration),
                leading = { SettingsGlyph(SpinIconGlyph.Minus) },
                trailing = {
                    SpinToggle(
                        checked = state.vibrationEnabled,
                        onCheckedChange = viewModel::setVibrationEnabled,
                    )
                },
            )
        }
    }
}

@Composable
private fun SettingsGlyph(glyph: SpinIconGlyph) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.08f)),
    ) {
        SpinIcon(
            glyph = glyph,
            tint = Color(0xFF50C450),
            modifier = Modifier
                .align(Alignment.Center)
                .size(26.dp)
                .padding(2.dp),
        )
    }
}
