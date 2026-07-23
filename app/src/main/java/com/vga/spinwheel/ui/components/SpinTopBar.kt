package com.vga.spinwheel.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun SpinTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: SpinIconGlyph? = null,
    navigationDescription: String = "Navigate",
    onNavigationClick: (() -> Unit)? = null,
    centerTitle: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(SpinColors.Background)
            .padding(
                horizontal = SpinSpacing.ScreenHorizontal,
                vertical = 8.dp,
            ),
    ) {
        if (navigationIcon != null && onNavigationClick != null) {
            SpinIconButton(
                glyph = navigationIcon,
                contentDescription = navigationDescription,
                onClick = onNavigationClick,
                modifier = Modifier.align(Alignment.CenterStart),
            )
        }

        Text(
            text = title,
            color = SpinColors.TextPrimary,
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = if (centerTitle) {
                Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 48.dp)
            } else {
                Modifier
                    .align(Alignment.CenterStart)
                    .padding(
                        start = if (navigationIcon != null && onNavigationClick != null) 48.dp else 0.dp,
                        end = 48.dp,
                    )
            },
        )

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
            content = actions,
        )
    }
}
