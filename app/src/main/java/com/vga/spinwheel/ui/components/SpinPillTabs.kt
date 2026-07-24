package com.vga.spinwheel.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing

/**
 * Reusable pill-style tab bar selector for mode/filter switching.
 */
@Composable
fun SpinPillTabs(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = SpinColors.Action,
    inactiveColor: Color = SpinColors.Card,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = SpinSpacing.ScreenHorizontal,
                vertical = 8.dp,
            ),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        options.forEachIndexed { index, label ->
            val isSelected = index == selectedIndex
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) activeColor else inactiveColor,
                label = "PillTabBackground",
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) SpinColors.TextPrimary else SpinColors.TextMuted,
                label = "PillTabText",
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(SpinSpacing.ControlHeight)
                    .clip(RoundedCornerShape(SpinRadius.Button))
                    .background(backgroundColor)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) activeColor else SpinColors.CardBorder,
                        shape = RoundedCornerShape(SpinRadius.Button),
                    )
                    .clickable { onSelect(index) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    color = textColor,
                    fontSize = 15.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
