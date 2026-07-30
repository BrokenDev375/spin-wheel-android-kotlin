package com.vga.spinwheel.ui.screen.dice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinScreen
import com.vga.spinwheel.ui.components.SpinSettingRow
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun DiceSettingsScreen(
    viewModel: DiceViewModel,
    onBack: () -> Unit,
    onOpenLabel: () -> Unit,
) {
    SpinScreen(
        title = stringResource(R.string.customsize),
        navigationIcon = SpinIconGlyph.Back,
        onNavigationClick = onBack,
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        navigationTint = Color.White,
    ) { contentModifier ->
        Column(
            modifier = contentModifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            SpinSettingRow(
                title = stringResource(R.string.diceRoller),
                onClick = onOpenLabel,
                trailing = {
                    SpinIcon(
                        glyph = SpinIconGlyph.ChevronRight,
                        tint = SpinColors.IconMuted,
                        modifier = Modifier.size(24.dp),
                    )
                },
            )
        }
    }
}
