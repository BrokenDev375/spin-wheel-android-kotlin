package com.vga.spinwheel.ui.screen.drawing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinSettingRow
import com.vga.spinwheel.ui.components.SpinStepper
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun DrawingSettingsScreen(
    viewModel: DrawingViewModel,
    onBack: () -> Unit,
    onOpenPalette: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tempDuration by viewModel.tempDuration.collectAsState()
    val tempThemeIndex by viewModel.tempThemeIndex.collectAsState()
    val themeColor = getThemeColor(tempThemeIndex)

    LaunchedEffect(Unit) {
        viewModel.initTempSettings()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = "Cài đặt",
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Quay lại",
                onNavigationClick = {
                    viewModel.saveSettings()
                    onBack()
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = SpinSpacing.ScreenHorizontal),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Duration Setting
            SpinSettingRow(
                title = "Thời lượng xáo thẻ",
                trailing = {
                    SpinStepper(
                        value = "${tempDuration}s",
                        onMinus = { if (tempDuration > 1) viewModel.setTempDuration(tempDuration - 1) },
                        onPlus = { if (tempDuration < 10) viewModel.setTempDuration(tempDuration + 1) }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Theme Setting
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(SpinRadius.Control))
                    .background(Color(0xFF3B3754))
                    .clickable(onClick = onOpenPalette)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Màu thẻ",
                    style = MaterialTheme.typography.titleMedium,
                    color = SpinColors.TextPrimary,
                    modifier = Modifier.weight(1f),
                )
                
                Spacer(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(themeColor)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                SpinIcon(
                    glyph = SpinIconGlyph.Back,
                    tint = SpinColors.TextMuted,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}
