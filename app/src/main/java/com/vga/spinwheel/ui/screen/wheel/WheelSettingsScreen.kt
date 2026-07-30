package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinSettingRow
import com.vga.spinwheel.ui.components.SpinSettingStepper
import com.vga.spinwheel.ui.components.SpinToggle
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun WheelSettingsScreen(
    viewModel: WheelViewModel,
    onBack: () -> Unit,
    onOpenPalette: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val duration by viewModel.duration.collectAsState()
    val removeWinner by viewModel.removeWinner.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = stringResource(R.string.customsize),
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = stringResource(R.string.content_description_back),
                onNavigationClick = onBack,
                centerTitle = false,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                horizontal = SpinSpacing.ScreenHorizontal,
                vertical = 14.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Row 1: Thời lượng hoạt hình (with [-] 5s [+] white square controls)
            item {
                SpinSettingRow(
                    title = stringResource(R.string.duration),
                    trailing = {
                        SpinSettingStepper(
                            value = "${duration}s",
                            onMinus = { viewModel.updateDuration(duration - 1) },
                            onPlus = { viewModel.updateDuration(duration + 1) },
                        )
                    },
                )
            }

            // Row 2: Bảng màu (Navigates to Palette screen)
            item {
                SpinSettingRow(
                    title = stringResource(R.string.color),
                    onClick = onOpenPalette,
                    trailing = {
                        SpinIcon(
                            glyph = SpinIconGlyph.ChevronRight,
                            tint = SpinColors.IconMuted,
                            modifier = Modifier.size(24.dp),
                        )
                    },
                )
            }

            // Row 3: Xóa người thắng (Switch toggle)
            item {
                SpinSettingRow(
                    title = stringResource(R.string.removewin),
                    trailing = {
                        SpinToggle(
                            checked = removeWinner,
                            onCheckedChange = viewModel::toggleRemoveWinner,
                        )
                    },
                )
            }
        }
    }
}
