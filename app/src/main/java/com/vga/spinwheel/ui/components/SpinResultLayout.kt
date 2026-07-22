package com.vga.spinwheel.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun SpinResultLayout(
    title: String,
    result: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(SpinSpacing.ScreenHorizontal),
    actions: @Composable () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background)
            .padding(contentPadding),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = title,
                color = SpinColors.TextMuted,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = result,
                color = SpinColors.TextPrimary,
                style = MaterialTheme.typography.headlineLarge,
            )
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            actions()
        }
    }
}
