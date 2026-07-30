package com.vga.spinwheel.ui.screen.drawing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinScreen
import com.vga.spinwheel.ui.components.SpinSettingRow
import com.vga.spinwheel.ui.components.SpinSettingStepper
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun DrawingSettingsScreen(
    viewModel: DrawingViewModel,
    onBack: () -> Unit,
    onOpenPalette: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val duration by viewModel.duration.collectAsState()
    val themeIndex by viewModel.themeIndex.collectAsState()
    val tempDuration by viewModel.tempDuration.collectAsState()

    LaunchedEffect(duration, themeIndex) {
        viewModel.initTempSettings()
    }

    val saveAndBack = {
        viewModel.saveSettings()
        onBack()
    }

    SpinScreen(
        title = stringResource(R.string.customsize),
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = stringResource(R.string.content_description_back),
        onNavigationClick = saveAndBack,
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        modifier = modifier,
    ) { contentModifier ->
        Column(
            modifier = contentModifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            SpinSettingRow(
                title = stringResource(R.string.duration),
                trailing = {
                    SpinSettingStepper(
                        value = "${tempDuration}s",
                        onMinus = {
                            if (tempDuration > 2) {
                                viewModel.setTempDuration(tempDuration - 1)
                            }
                        },
                        onPlus = {
                            if (tempDuration < 10) {
                                viewModel.setTempDuration(tempDuration + 1)
                            }
                        },
                    )
                },
            )

            SpinSettingRow(
                title = stringResource(R.string.drawn),
                onClick = onOpenPalette,
                trailing = {
                    SpinIcon(
                        glyph = SpinIconGlyph.ChevronRight,
                        tint = SpinColors.IconMuted,
                        modifier = Modifier.size(26.dp),
                    )
                },
            )
        }
    }
}
