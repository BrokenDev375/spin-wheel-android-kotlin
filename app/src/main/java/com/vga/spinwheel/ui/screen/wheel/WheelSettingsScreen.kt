package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
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
                title = "Tùy chỉnh",
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Quay lại",
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(SpinRadius.Card))
                        .background(Color(0xFF3B3754))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Thời lượng hoạt hình",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        // Square minus button
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .clickable { viewModel.updateDuration(duration - 1) },
                            contentAlignment = Alignment.Center,
                        ) {
                            SpinIcon(glyph = SpinIconGlyph.Minus, tint = Color.Black, modifier = Modifier.size(16.dp))
                        }

                        Text(
                            text = "${duration}s",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )

                        // Square plus button
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .clickable { viewModel.updateDuration(duration + 1) },
                            contentAlignment = Alignment.Center,
                        ) {
                            SpinIcon(glyph = SpinIconGlyph.Plus, tint = Color.Black, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            // Row 2: Bảng màu (Navigates to Palette screen)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(SpinRadius.Card))
                        .background(Color(0xFF3B3754))
                        .clickable(onClick = onOpenPalette)
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Bảng màu",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                    )
                    SpinIcon(
                        glyph = SpinIconGlyph.ChevronRight,
                        tint = SpinColors.TextMuted,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            // Row 3: Xóa người thắng (Switch toggle)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(SpinRadius.Card))
                        .background(Color(0xFF3B3754))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Xóa người thắng",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                    )
                    Switch(
                        checked = removeWinner,
                        onCheckedChange = viewModel::toggleRemoveWinner,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF00E676),
                            uncheckedThumbColor = Color(0xFF888888),
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f),
                        ),
                    )
                }
            }
        }
    }
}
