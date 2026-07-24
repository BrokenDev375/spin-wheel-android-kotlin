package com.vga.spinwheel.ui.screen.drawing

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.ui.components.SpinResultScreen
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun DrawingResultScreen(
    wheelId: String,
    viewModel: DrawingViewModel,
    onRetry: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val wheel by viewModel.currentWheel.collectAsState()
    val winner by viewModel.lastResult.collectAsState()
    val themeIndex by viewModel.themeIndex.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(wheelId) {
        if (wheel?.id != wheelId) {
            viewModel.loadWheelForDrawing(wheelId)
        }
    }

    val winnerIndex = wheel?.items
        ?.indexOfFirst { it.id == winner?.id }
        ?.coerceAtLeast(0)
        ?: 0

    SpinResultScreen(
        title = "Kết Quả",
        onHome = onHome,
        onShare = {
            val shareText = "Kết quả Vẽ: ${winner?.name.orEmpty()} (${winnerIndex + 1})"
            context.startActivity(
                Intent.createChooser(
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    },
                    null,
                ),
            )
        },
        onRetry = onRetry,
        modifier = modifier
            .background(SpinColors.Background)
            .statusBarsPadding(),
        cardHeight = 450.dp,
        cardContentPadding = 0.dp,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(370.dp)
                    .background(SpinColors.Background),
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DrawingCardStack(
                    items = wheel?.items.orEmpty(),
                    winnerIndex = winnerIndex,
                    themeIndex = themeIndex,
                    modifier = Modifier.graphicsLayer {
                        scaleX = 0.92f
                        scaleY = 0.92f
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))
                DrawingResultNumber(value = winnerIndex + 1)
            }
        }
    }
}
