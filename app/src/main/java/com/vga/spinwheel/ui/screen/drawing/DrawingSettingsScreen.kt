package com.vga.spinwheel.ui.screen.drawing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinScreen
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun DrawingSettingsScreen(
    viewModel: DrawingViewModel,
    onBack: () -> Unit,
    onOpenPalette: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val duration by viewModel.duration.collectAsState()
    val themeIndex by viewModel.themeIndex.collectAsState()
    val tempDuration by viewModel.tempDuration.collectAsState()

    LaunchedEffect(duration, themeIndex) {
        viewModel.initTempSettings()
    }

    val saveAndBack = {
        viewModel.saveSettings()
        onBack()
    }

    SpinScreen(
        title = "Tùy chỉnh",
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = "Quay lại",
        onNavigationClick = saveAndBack,
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        modifier = modifier,
    ) { contentModifier ->
        Column(
            modifier = contentModifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            DrawingSettingRow(
                title = "Thời lượng hoạt hình",
                trailing = {
                    DrawingStepper(
                        value = "${tempDuration}s",
                        onMinus = {
                            if (tempDuration > 1) {
                                viewModel.setTempDuration(tempDuration - 1)
                            }
                        },
                        onPlus = {
                            if (tempDuration < 15) {
                                viewModel.setTempDuration(tempDuration + 1)
                            }
                        },
                    )
                },
            )

            DrawingSettingRow(
                title = "Vẽ",
                onClick = onOpenPalette,
                trailing = {
                    SpinIcon(
                        glyph = SpinIconGlyph.ChevronRight,
                        tint = SpinColors.IconMuted,
                        modifier = Modifier.size(26.dp),
                    )
                },
            )
        }
    }
}

@Composable
private fun DrawingSettingRow(
    title: String,
    onClick: (() -> Unit)? = null,
    trailing: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF393347))
            .then(if (onClick == null) Modifier else Modifier.clickable(onClick = onClick))
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
        )
        trailing()
    }
}

@Composable
private fun DrawingStepper(
    value: String,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        DrawingStepperButton(text = "−", onClick = onMinus)
        Text(
            text = value,
            modifier = Modifier.width(36.dp),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
        )
        DrawingStepperButton(text = "+", onClick = onPlus)
    }
}

@Composable
private fun DrawingStepperButton(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
        )
    }
}
