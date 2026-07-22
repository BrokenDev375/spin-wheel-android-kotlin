package com.vga.spinwheel.ui.screen.number

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vga.spinwheel.ui.components.SpinBottomActionBar
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinPrimaryButton
import com.vga.spinwheel.ui.components.SpinSettingRow
import com.vga.spinwheel.ui.components.SpinStepper
import com.vga.spinwheel.ui.components.SpinToggle
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun NumberSettingsScreen(
    navController: NavController,
    viewModel: NumberViewModel = hiltViewModel()
) {
    val tempMin by viewModel.tempMin.collectAsState()
    val tempMax by viewModel.tempMax.collectAsState()
    val tempCount by viewModel.tempCount.collectAsState()
    val tempAllowDuplicates by viewModel.tempAllowDuplicates.collectAsState()
    val tempDuration by viewModel.tempDuration.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initTempSettings()
    }

    Scaffold(
        topBar = {
            SpinTopBar(
                title = "Tùy chỉnh",
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Back",
                onNavigationClick = { navController.popBackStack() },
            )
        },
        bottomBar = {
            SpinBottomActionBar {
                SpinPrimaryButton(
                    text = "Lưu",
                    onClick = {
                        viewModel.saveSettings()
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        },
        containerColor = SpinColors.Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            SpinSettingRow(
                title = "Minimum Value (From)",
                trailing = {
                    SpinStepper(
                        value = tempMin.toString(),
                        onMinus = { if (tempMin > -999) viewModel.updateTempSettings(min = tempMin - 1) },
                        onPlus = { if (tempMin < 999) viewModel.updateTempSettings(min = tempMin + 1) }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SpinSettingRow(
                title = "Maximum Value (To)",
                trailing = {
                    SpinStepper(
                        value = tempMax.toString(),
                        onMinus = { if (tempMax > -999) viewModel.updateTempSettings(max = tempMax - 1) },
                        onPlus = { if (tempMax < 999) viewModel.updateTempSettings(max = tempMax + 1) }
                    )
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            SpinSettingRow(
                title = "Generate Count",
                trailing = {
                    SpinStepper(
                        value = tempCount.toString(),
                        onMinus = { if (tempCount > 1) viewModel.updateTempSettings(count = tempCount - 1) },
                        onPlus = { if (tempCount < 100) viewModel.updateTempSettings(count = tempCount + 1) }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SpinSettingRow(
                title = "Allow Duplicates",
                trailing = {
                    SpinToggle(
                        checked = tempAllowDuplicates,
                        onCheckedChange = { viewModel.updateTempSettings(allowDuplicates = it) }
                    )
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            SpinSettingRow(
                title = "Thời lượng hoạt hình",
                trailing = {
                    SpinStepper(
                        value = "${tempDuration}s",
                        onMinus = { if (tempDuration > 1) viewModel.updateTempSettings(duration = tempDuration - 1) },
                        onPlus = { if (tempDuration < 10) viewModel.updateTempSettings(duration = tempDuration + 1) }
                    )
                }
            )
        }
    }
}
