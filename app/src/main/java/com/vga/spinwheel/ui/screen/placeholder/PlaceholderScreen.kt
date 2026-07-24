package com.vga.spinwheel.ui.screen.placeholder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinScreen
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun PlaceholderScreen(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SpinScreen(
        title = title,
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = "Quay lại",
        onNavigationClick = onBack,
        modifier = modifier,
    ) { contentModifier ->
        Box(
            modifier = contentModifier.padding(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    color = SpinColors.TextPrimary,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = "Đang xây dựng",
                    color = SpinColors.TextMuted,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
