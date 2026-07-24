package com.vga.spinwheel.ui.screen.dice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.vga.spinwheel.ui.components.SpinScreen

@Composable
fun DiceSettingsScreen(
    viewModel: DiceViewModel,
    onBack: () -> Unit,
    onOpenLabel: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    SpinScreen(
        title = "Tùy chỉnh",
        navigationIcon = SpinIconGlyph.Back,
        onNavigationClick = onBack,
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        navigationTint = Color.White,
    ) { contentModifier ->
        Column(
            modifier = contentModifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            DurationSettingRow(
                duration = uiState.duration,
                onMinus = { viewModel.setDuration(uiState.duration - 1) },
                onPlus = { viewModel.setDuration(uiState.duration + 1) },
            )

            Spacer(modifier = Modifier.height(14.dp))

            DiceSkinSettingRow(onClick = onOpenLabel)
        }
    }
}

@Composable
private fun DurationSettingRow(
    duration: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DiceSettingsPanel)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Thời lượng hoạt hình",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DiceStepperButton(text = "-", onClick = onMinus)
            Text(
                text = "${duration}s",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
            )
            DiceStepperButton(text = "+", onClick = onPlus)
        }
    }
}

@Composable
private fun DiceSkinSettingRow(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DiceSettingsPanel)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Xúc Xắc",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
        )
        SpinIcon(
            glyph = SpinIconGlyph.ChevronRight,
            tint = Color.White.copy(alpha = 0.55f),
            modifier = Modifier.size(30.dp),
        )
    }
}

@Composable
private fun DiceStepperButton(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 23.sp,
            fontWeight = FontWeight.Black,
        )
    }
}

private val DiceSettingsPanel = Color(0xFF393347)
