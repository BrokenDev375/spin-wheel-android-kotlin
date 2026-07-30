package com.vga.spinwheel.ui.screen.team

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
import com.vga.spinwheel.ui.screen.wheel.WheelAiGenerateDialog
import com.vga.spinwheel.ui.screen.wheel.WheelViewModel
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun TeamHomeScreen(
    viewModel: TeamViewModel,
    wheelFormViewModel: WheelViewModel,
    onBack: () -> Unit,
    onAddList: () -> Unit,
    onOpenPreparedForm: () -> Unit,
    onEditList: (String) -> Unit,
    onOpenList: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val wheels by viewModel.wheels.collectAsState()
    val showAiModal by wheelFormViewModel.showAiModal.collectAsState()
    var deleteTargetId by remember { mutableStateOf<String?>(null) }

    SpinScreen(
        title = stringResource(R.string.homograft),
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = stringResource(R.string.content_description_back),
        onNavigationClick = onBack,
        modifier = modifier,
    ) { contentModifier ->
        WheelSelectionList(
            wheels = wheels,
            onAiGenerate = { wheelFormViewModel.showAiModal(true) },
            onCreateWheel = onAddList,
            onOpenWheel = { wheel -> onOpenList(wheel.id) },
            onEditWheel = { wheel -> onEditList(wheel.id) },
            onDuplicateWheel = { wheel -> viewModel.duplicateList(wheel.id) },
            onDeleteWheel = { wheel -> deleteTargetId = wheel.id },
            modifier = contentModifier,
        )
    }

    if (deleteTargetId != null) {
        AlertDialog(
            onDismissRequest = { deleteTargetId = null },
            title = { Text(stringResource(R.string.confirm), color = SpinColors.TextPrimary) },
            text = {
                Text(
                    text = stringResource(R.string.confirm_delete),
                    color = SpinColors.TextMuted,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = rememberClickWithSound {
                        deleteTargetId?.let { viewModel.deleteList(it) }
                        deleteTargetId = null
                    }
                ) {
                    Text(stringResource(R.string.delete), color = Color(0xFFFF3B30))
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
            topics = wheelFormViewModel.aiTopics,
            onSelectTopic = { topic ->
                wheelFormViewModel.generateAiWheel(topic)
                onOpenPreparedForm()
            },
            onCustomPrompt = { prompt ->
                wheelFormViewModel.generateAiCustom(prompt)
                onOpenPreparedForm()
            },
            onDismiss = { wheelFormViewModel.showAiModal(false) },
        )
    }
}
