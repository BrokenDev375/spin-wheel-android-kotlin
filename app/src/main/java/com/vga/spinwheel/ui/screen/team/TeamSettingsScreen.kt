package com.vga.spinwheel.ui.screen.team

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinToggle
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun TeamSettingsScreen(
    viewModel: TeamViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()

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
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = SpinSpacing.ScreenHorizontal)
                .padding(top = 72.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TeamSettingRow(
                title = "Các mục của nhóm",
                trailing = {
                    TeamStepper(
                        value = state.groupSize.toString(),
                        onMinus = { viewModel.updateGroupSize(state.groupSize - 1) },
                        onPlus = { viewModel.updateGroupSize(state.groupSize + 1) },
                    )
                },
            )

            TeamSettingRow(
                title = "Thời lượng hoạt hình",
                trailing = {
                    TeamStepper(
                        value = "${state.durationSeconds}s",
                        onMinus = { viewModel.updateDuration(state.durationSeconds - 1) },
                        onPlus = { viewModel.updateDuration(state.durationSeconds + 1) },
                    )
                },
            )

            TeamSettingRow(
                title = "Gieo hạt của nhóm",
                trailing = {
                    SpinToggle(
                        checked = state.seedEnabled,
                        onCheckedChange = viewModel::toggleSeedEnabled,
                    )
                },
            )
        }
    }
}

@Composable
private fun TeamSettingRow(
    title: String,
    trailing: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(SpinRadius.Control))
            .background(Color(0xFF393347))
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
        )
        Spacer(modifier = Modifier.width(12.dp))
        trailing()
    }
}

@Composable
private fun TeamStepper(
    value: String,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TeamStepperButton(text = "-", onClick = onMinus)
        Text(
            text = value,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.width(44.dp),
        )
        TeamStepperButton(text = "+", onClick = onPlus)
    }
}

@Composable
private fun TeamStepperButton(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}
