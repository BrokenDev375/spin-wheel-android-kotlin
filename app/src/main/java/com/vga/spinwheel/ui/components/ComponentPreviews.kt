package com.vga.spinwheel.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.theme.AppTheme

@Preview(showBackground = true, backgroundColor = 0xFF292640)
@Composable
private fun ComponentPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            SpinTopBar(
                title = "Spin Wheel",
                navigationIcon = SpinIconGlyph.Settings,
                onNavigationClick = {},
            ) {
                SpinIconButton(
                    glyph = SpinIconGlyph.Crown,
                    contentDescription = "Pro",
                    onClick = {},
                    tint = Color(0xFFFFD21E),
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SpinFeatureCard(
                    title = "Banh Xe",
                    style = SpinFeatureCardStyle(
                        backgroundRes = R.drawable.home_game_wheel,
                    ),
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1.18f),
                )
                SpinFeatureCard(
                    title = "Dong Xu",
                    style = SpinFeatureCardStyle(
                        backgroundRes = R.drawable.home_game_coin,
                        titleColor = Color(0xFFFFFF33),
                    ),
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1.18f),
                )
            }
            SpinSettingRow(
                title = "Nhac nen",
                subtitle = "Bat/tat am thanh nen",
                trailing = { SpinToggle(checked = true, onCheckedChange = {}) },
            )
            SpinBottomActionBar {
                SpinSecondaryButton(text = "Thu lai", onClick = {}, modifier = Modifier.weight(1f))
                SpinPrimaryButton(text = "Chia se", onClick = {}, modifier = Modifier.weight(1f))
            }
        }
    }
}
