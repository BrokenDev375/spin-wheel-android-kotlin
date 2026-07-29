package com.vga.spinwheel.ui.screen.drawing

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
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun DrawingHomeScreen(
    viewModel: DrawingViewModel,
    onBack: () -> Unit,
    onAddWheel: () -> Unit,
    onAiGenerate: () -> Unit,
    onEditWheel: (String) -> Unit,
    onSpinWheel: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val wheels by viewModel.wheels.collectAsState()
    var deleteTargetId by remember { mutableStateOf<String?>(null) }
    val displayWheels = wheels.ifEmpty { listOf(DrawingViewModel.DRAWING_FALLBACK_WHEEL) }

    SpinScreen(
        title = stringResource(R.string.drawn),
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = stringResource(R.string.content_description_back),
        onNavigationClick = onBack,
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        modifier = modifier,
    ) { contentModifier ->
        WheelSelectionList(
            wheels = displayWheels,
            onAiGenerate = onAiGenerate,
            onCreateWheel = onAddWheel,
            onOpenWheel = { wheel -> onSpinWheel(wheel.id) },
            onEditWheel = { wheel -> onEditWheel(wheel.id) },
            onDuplicateWheel = { wheel -> viewModel.cloneWheel(wheel.id) },
            onDeleteWheel = { wheel -> deleteTargetId = wheel.id },
            modifier = contentModifier,
            canEditWheel = { wheel -> wheel.id != DrawingViewModel.DRAWING_FALLBACK_WHEEL.id },
            canDeleteWheel = { wheel -> wheel.id != DrawingViewModel.DRAWING_FALLBACK_WHEEL.id },
        )
    }

    if (deleteTargetId != null) {
        AlertDialog(
            onDismissRequest = { deleteTargetId = null },
            title = { Text(stringResource(R.string.confirm), color = SpinColors.TextPrimary) },
            text = {
                Text(
                    stringResource(R.string.confirm_delete),
                    color = SpinColors.TextMuted,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteTargetId?.let(viewModel::deleteWheel)
                        deleteTargetId = null
                    },
                ) {
                    Text(stringResource(R.string.delete), color = Color(0xFFFF5252))
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTargetId = null }) {
                    Text(stringResource(R.string.cancel), color = SpinColors.TextPrimary)
                }
            },
            containerColor = Color(0xFF2D2845),
        )
    }
}
