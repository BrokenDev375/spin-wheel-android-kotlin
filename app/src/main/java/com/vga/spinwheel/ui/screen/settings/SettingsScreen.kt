package com.vga.spinwheel.ui.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vga.spinwheel.BuildConfig
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinSettingRow
import com.vga.spinwheel.ui.components.SpinToggle
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onShareClick: () -> Unit,
    onLanguageClick: () -> Unit,
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
                title = stringResource(R.string.settings),
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = stringResource(R.string.content_description_back),
                onNavigationClick = onBack,
                actions = {
                    Text(
                        text = "v${BuildConfig.VERSION_NAME}",
                        color = SpinColors.TextMuted,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
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
            Text(
                text = stringResource(R.string.General),
                color = SpinColors.TextMuted,
                style = MaterialTheme.typography.titleLarge,
            )
            SpinSettingRow(
                title = stringResource(R.string.share_app),
                onClick = onShareClick,
                leading = { SettingsGlyph(SpinIconGlyph.ShareNetwork) },
            )
            SpinSettingRow(
                title = stringResource(R.string.language),
                onClick = onLanguageClick,
                leading = { SettingsGlyph(SpinIconGlyph.Language) },
            )
            SpinSettingRow(
                title = stringResource(R.string.rate_us),
                onClick = onRateClick,
                leading = { SettingsGlyph(SpinIconGlyph.Rate) },
            )
            SpinSettingRow(
                title = stringResource(R.string.background_music),
                leading = { SettingsGlyph(SpinIconGlyph.Music) },
                trailing = {
                    SpinToggle(
                        checked = state.musicEnabled,
                        onCheckedChange = viewModel::setMusicEnabled,
                    )
                },
            )
            SpinSettingRow(
                title = stringResource(R.string.game_sound),
                leading = { SettingsGlyph(SpinIconGlyph.Sound) },
                trailing = {
                    SpinToggle(
                        checked = state.gameSoundEnabled,
                        onCheckedChange = viewModel::setGameSoundEnabled,
                    )
                },
            )
            SpinSettingRow(
                title = stringResource(R.string.vibration),
                leading = { SettingsGlyph(SpinIconGlyph.Vibration) },
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
        modifier = Modifier.size(42.dp),
        contentAlignment = Alignment.Center,
    ) {
        SpinIcon(
            glyph = glyph,
            tint = Color(0xFF50C450),
            modifier = Modifier.size(30.dp),
        )
    }
}
