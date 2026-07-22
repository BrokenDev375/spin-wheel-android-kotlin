package com.vga.spinwheel.ui.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinSettingRow
import com.vga.spinwheel.ui.components.SpinToggle
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun SettingsSkeletonScreen(
    onBack: () -> Unit,
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = "Cai dat",
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Back",
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
                title = "Chia se ung dung",
            )
            SpinSettingRow(
                title = "Ngon ngu",
                onClick = onLanguageClick,
            )
            SpinSettingRow(
                title = "Danh gia ung dung",
            )
            SpinSettingRow(
                title = "Nhac nen",
                trailing = {
                    SpinToggle(
                        checked = false,
                        onCheckedChange = {},
                    )
                },
            )
            SpinSettingRow(
                title = "Am thanh tro choi",
                trailing = {
                    SpinToggle(
                        checked = false,
                        onCheckedChange = {},
                    )
                },
            )
            SpinSettingRow(
                title = "Rung",
                trailing = {
                    SpinToggle(
                        checked = false,
                        onCheckedChange = {},
                    )
                },
            )
        }
    }
}
