package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinScreen
import com.vga.spinwheel.ui.components.WheelSelectionList
import com.vga.spinwheel.ui.components.rememberClickWithSound
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun WheelHomeScreen(
    viewModel: WheelViewModel,
    onBack: () -> Unit,
    onAddWheel: () -> Unit,
    onEditWheel: (String) -> Unit,
    onSpinWheel: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val wheels by viewModel.wheels.collectAsState()
    val showAiModal by viewModel.showAiModal.collectAsState()
    var deleteTargetId by remember { mutableStateOf<String?>(null) }

    SpinScreen(
        title = stringResource(R.string.spinwheel),
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = stringResource(R.string.content_description_back),
        onNavigationClick = onBack,
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        modifier = modifier,
    ) { contentModifier ->
        WheelSelectionList(
            wheels = wheels,
            onAiGenerate = { viewModel.showAiModal(true) },
            onCreateWheel = onAddWheel,
            onOpenWheel = { wheel -> onSpinWheel(wheel.id) },
            onEditWheel = { wheel -> onEditWheel(wheel.id) },
            onDuplicateWheel = { wheel -> viewModel.duplicateWheel(wheel.id) },
            onDeleteWheel = { wheel -> deleteTargetId = wheel.id },
            modifier = contentModifier,
        )
    }

    if (deleteTargetId != null) {
        AlertDialog(
            onDismissRequest = { deleteTargetId = null },
            title = { Text(stringResource(R.string.confirm), color = SpinColors.TextPrimary) },
            text = { Text(stringResource(R.string.confirm_delete), color = SpinColors.TextMuted) },
            confirmButton = {
                TextButton(
                    onClick = rememberClickWithSound {
                        deleteTargetId?.let { viewModel.deleteWheel(it) }
                        deleteTargetId = null
                    }
                ) {
                    Text(stringResource(R.string.delete), color = Color(0xFFFF5252))
                }
            },
            dismissButton = {
                TextButton(onClick = rememberClickWithSound { deleteTargetId = null }) {
                    Text(stringResource(R.string.cancel), color = SpinColors.TextPrimary)
                }
            },
            containerColor = Color(0xFF2D2845),
        )
    }

    if (showAiModal) {
        WheelAiGenerateDialog(
            topics = viewModel.aiTopics,
            onSelectTopic = { topic ->
                viewModel.generateAiWheel(topic)
                onAddWheel()
            },
            onCustomPrompt = { prompt ->
                viewModel.generateAiCustom(prompt)
                onAddWheel()
            },
            onDismiss = { viewModel.showAiModal(false) },
        )
    }
}
