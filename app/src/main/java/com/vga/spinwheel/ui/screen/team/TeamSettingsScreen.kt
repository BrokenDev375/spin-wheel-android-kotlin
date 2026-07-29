package com.vga.spinwheel.ui.screen.team

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinSettingRow
import com.vga.spinwheel.ui.components.SpinSettingStepper
import com.vga.spinwheel.ui.components.SpinToggle
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors

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
                title = stringResource(R.string.customsize),
                centerTitle = false,
                titleStartPadding = 39.dp,
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = stringResource(R.string.content_description_back),
                onNavigationClick = onBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SpinSettingRow(
                title = stringResource(R.string.itemsgroup),
                trailing = {
                    SpinSettingStepper(
                        value = state.groupSize.toString(),
                        onMinus = { viewModel.updateGroupSize(state.groupSize - 1) },
                        onPlus = { viewModel.updateGroupSize(state.groupSize + 1) },
                    )
                },
            )

            SpinSettingRow(
                title = stringResource(R.string.duration),
                trailing = {
                    SpinSettingStepper(
                        value = "${state.durationSeconds}s",
                        onMinus = { viewModel.updateDuration(state.durationSeconds - 1) },
                        onPlus = { viewModel.updateDuration(state.durationSeconds + 1) },
                    )
                },
            )

            SpinSettingRow(
                title = stringResource(R.string.Seeding),
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
