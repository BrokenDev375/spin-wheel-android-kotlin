package com.vga.spinwheel.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

/**
 * Standard confirmation dialog when exiting a game in Spin Wheel.
 * Matches design spec from screenshot/Home/back.jpg
 */
@Composable
fun SpinConfirmExitDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String = "Thoát trò chơi",
    message: String = "Bạn chắc chắn muốn thoát trò chơi",
    cancelText: String = "Ở lại",
    confirmText: String = "Thoát",
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF2E293E),
            modifier = Modifier.fillMaxWidth(0.92f),
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = cancelText,
                            color = Color(0xFFF12966),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    TextButton(onClick = onConfirm) {
                        Text(
                            text = confirmText,
                            color = Color(0xFFF5A623),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Standard screen layout container for Spin Wheel application.
 * Automatically handles status bar padding, header bar rendering,
 * standard dark background color, and optional Exit confirmation dialog on Back press.
 */
@Composable
fun SpinScreen(
    modifier: Modifier = Modifier,
    title: String? = null,
    navigationIcon: SpinIconGlyph? = SpinIconGlyph.Back,
    navigationDescription: String = "Back",
    onNavigationClick: (() -> Unit)? = null,
    centerTitle: Boolean = true,
    topBarTitleStartPadding: Dp = 48.dp,
    confirmExitOnBack: Boolean = false,
    onConfirmExit: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (Modifier) -> Unit,
) {
    var showExitDialog by remember { mutableStateOf(false) }

    val triggerBack = {
        if (confirmExitOnBack) {
            showExitDialog = true
        } else {
            onNavigationClick?.invoke()
        }
    }

    if (confirmExitOnBack) {
        BackHandler(enabled = true) {
            showExitDialog = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background)
            .statusBarsPadding(),
    ) {
        if (title != null || onNavigationClick != null) {
            SpinTopBar(
                title = title ?: "",
                navigationIcon = if (onNavigationClick != null) navigationIcon else null,
                navigationDescription = navigationDescription,
                onNavigationClick = { triggerBack() },
                centerTitle = centerTitle,
                titleStartPadding = topBarTitleStartPadding,
                actions = actions,
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            content(Modifier.fillMaxSize())
        }
    }

    if (showExitDialog) {
        SpinConfirmExitDialog(
            onDismiss = { showExitDialog = false },
            onConfirm = {
                showExitDialog = false
                (onConfirmExit ?: onNavigationClick)?.invoke()
            }
        )
    }
}

/**
 * Common Component for ALL Result/Preview screens in Spin Wheel.
 * Layout structure:
 * - TopBar with Home action button (supports optional exit confirmation)
 * - Result Card (centered top)
 * - Share Button (centered in vertical middle space between Card & Retry)
 * - Retry Button (fixed at bottom)
 */
@Composable
fun SpinResultScreen(
    onHome: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Kết Quả",
    confirmExitOnBack: Boolean = false,
    onShare: (() -> Unit)? = null,
    shareText: String = "Chia sẻ kết quả",
    shareBackgroundColor: Color = Color(0xFF39A9F2),
    retryText: String = "Thử lại",
    retryBackgroundColor: Color = Color(0xFFDE3D2D),
    cardHeight: Dp = 450.dp,
    cardContentPadding: Dp = 18.dp,
    cardContent: @Composable () -> Unit,
) {
    var showExitDialog by remember { mutableStateOf(false) }

    val handleHome = {
        if (confirmExitOnBack) {
            showExitDialog = true
        } else {
            onHome()
        }
    }

    if (confirmExitOnBack) {
        BackHandler(enabled = true) {
            showExitDialog = true
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = title,
                actions = {
                    SpinIconButton(
                        glyph = SpinIconGlyph.Home,
                        contentDescription = "Trang chủ",
                        onClick = { handleHome() },
                        tint = Color.White,
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = SpinSpacing.ScreenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Result Card
            SpinResultCard(
                cardHeight = cardHeight,
                contentPadding = cardContentPadding,
            ) {
                cardContent()
            }

            // 2. Share button centered in vertical middle space
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                if (onShare != null) {
                    SpinShareButton(
                        text = shareText,
                        onClick = onShare,
                        backgroundColor = shareBackgroundColor,
                    )
                }
            }

            // 3. Retry button pinned at bottom
            SpinRetryButton(
                text = retryText,
                onClick = onRetry,
                backgroundColor = retryBackgroundColor,
                modifier = Modifier.padding(bottom = 32.dp),
            )
        }
    }

    if (showExitDialog) {
        SpinConfirmExitDialog(
            onDismiss = { showExitDialog = false },
            onConfirm = {
                showExitDialog = false
                onHome()
            }
        )
    }
}
