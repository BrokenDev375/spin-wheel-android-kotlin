package com.vga.spinwheel.ui.screen.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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

    SettingsContent(
        state = state,
        onBack = onBack,
        onShareClick = onShareClick,
        onLanguageClick = onLanguageClick,
        onRateClick = onRateClick,
        onMusicChange = viewModel::setMusicEnabled,
        onGameSoundChange = viewModel::setGameSoundEnabled,
        onVibrationChange = viewModel::setVibrationEnabled,
        modifier = modifier,
    )
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onBack: () -> Unit,
    onShareClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onRateClick: () -> Unit,
    onMusicChange: (Boolean) -> Unit,
    onGameSoundChange: (Boolean) -> Unit,
    onVibrationChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SettingsBackground),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = SpinSpacing.ScreenHorizontal,
                        vertical = 18.dp,
                    ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(R.string.General),
                    modifier = Modifier.fillMaxWidth(),
                    color = SpinColors.TextMuted,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                StyledSettingRow(
                    title = stringResource(R.string.share_app),
                    glyph = SpinIconGlyph.ShareNetwork,
                    style = SettingsRowStyles[0],
                    onClick = onShareClick,
                )
                StyledSettingRow(
                    title = stringResource(R.string.language),
                    glyph = SpinIconGlyph.Language,
                    style = SettingsRowStyles[1],
                    onClick = onLanguageClick,
                )
                StyledSettingRow(
                    title = stringResource(R.string.rate_us),
                    glyph = SpinIconGlyph.Rate,
                    style = SettingsRowStyles[2],
                    onClick = onRateClick,
                )
                StyledSettingRow(
                    title = stringResource(R.string.background_music),
                    glyph = SpinIconGlyph.Music,
                    style = SettingsRowStyles[3],
                    trailing = {
                        SpinToggle(
                            checked = state.musicEnabled,
                            onCheckedChange = onMusicChange,
                        )
                    },
                )
                StyledSettingRow(
                    title = stringResource(R.string.game_sound),
                    glyph = SpinIconGlyph.Sound,
                    style = SettingsRowStyles[4],
                    trailing = {
                        SpinToggle(
                            checked = state.gameSoundEnabled,
                            onCheckedChange = onGameSoundChange,
                        )
                    },
                )
                StyledSettingRow(
                    title = stringResource(R.string.vibration),
                    glyph = SpinIconGlyph.Vibration,
                    style = SettingsRowStyles[5],
                    trailing = {
                        SpinToggle(
                            checked = state.vibrationEnabled,
                            onCheckedChange = onVibrationChange,
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun StyledSettingRow(
    title: String,
    glyph: SpinIconGlyph,
    style: SettingsRowStyle,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    SpinSettingRow(
        title = title,
        onClick = onClick,
        leading = { SettingsGlyph(glyph, style) },
        trailing = trailing,
        containerColor = style.containerColor,
        borderColor = style.borderColor,
    )
}

@Composable
private fun SettingsGlyph(
    glyph: SpinIconGlyph,
    style: SettingsRowStyle,
) {
    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(shape)
            .background(style.iconBackground)
            .border(1.dp, style.borderColor, shape),
        contentAlignment = Alignment.Center,
    ) {
        SpinIcon(
            glyph = glyph,
            tint = style.iconTint,
            modifier = Modifier.size(26.dp),
        )
    }
}

private data class SettingsRowStyle(
    val iconTint: Color,
    val iconBackground: Color,
    val containerColor: Color,
    val borderColor: Color,
)

private val SettingsBackground = Brush.verticalGradient(
    colors = listOf(
        SpinColors.Background,
        Color(0xFF302A48),
        SpinColors.BackgroundDeep,
    ),
)

private val SettingsRowStyles = listOf(
    SettingsRowStyle(
        iconTint = Color(0xFF33E3FF),
        iconBackground = Color(0xFF33E3FF).copy(alpha = 0.16f),
        containerColor = Color.White.copy(alpha = 0.085f),
        borderColor = Color(0xFF33E3FF).copy(alpha = 0.18f),
    ),
    SettingsRowStyle(
        iconTint = Color(0xFFFFD84A),
        iconBackground = Color(0xFFFFD84A).copy(alpha = 0.16f),
        containerColor = Color.White.copy(alpha = 0.078f),
        borderColor = Color(0xFFFFD84A).copy(alpha = 0.18f),
    ),
    SettingsRowStyle(
        iconTint = Color(0xFFFF7CB5),
        iconBackground = Color(0xFFFF7CB5).copy(alpha = 0.16f),
        containerColor = Color.White.copy(alpha = 0.085f),
        borderColor = Color(0xFFFF7CB5).copy(alpha = 0.18f),
    ),
    SettingsRowStyle(
        iconTint = Color(0xFF73F28E),
        iconBackground = Color(0xFF73F28E).copy(alpha = 0.14f),
        containerColor = Color.White.copy(alpha = 0.072f),
        borderColor = Color(0xFF73F28E).copy(alpha = 0.16f),
    ),
    SettingsRowStyle(
        iconTint = Color(0xFFFF9F43),
        iconBackground = Color(0xFFFF9F43).copy(alpha = 0.14f),
        containerColor = Color.White.copy(alpha = 0.078f),
        borderColor = Color(0xFFFF9F43).copy(alpha = 0.16f),
    ),
    SettingsRowStyle(
        iconTint = Color(0xFFA98BFF),
        iconBackground = Color(0xFFA98BFF).copy(alpha = 0.16f),
        containerColor = Color.White.copy(alpha = 0.072f),
        borderColor = Color(0xFFA98BFF).copy(alpha = 0.18f),
    ),
)
