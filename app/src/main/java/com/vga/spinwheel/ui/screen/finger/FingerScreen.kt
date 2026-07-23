package com.vga.spinwheel.ui.screen.finger

import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinResultScreen
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun FingerScreen(
    viewModel: FingerViewModel,
    onBack: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    DisposableEffect(viewModel) {
        onDispose { viewModel.cancelRound() }
    }

    if (state.stage == FingerStage.FinalResult) {
        FingerFinalResultScreen(
            state = state,
            onShare = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Ket qua chon ngon tay")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Spin Wheel da chon ngau nhien 1 nguoi thang trong ${state.points.size} ngon tay."
                    )
                }
                context.startActivity(Intent.createChooser(shareIntent, "Chia se ket qua"))
            },
            onRetry = viewModel::retry,
            onHome = {
                viewModel.cancelRound()
                onHome()
            },
            modifier = modifier,
        )
    } else {
        FingerPlayScreen(
            state = state,
            onBack = {
                viewModel.cancelRound()
                onBack()
            },
            onFingerCountSelected = viewModel::selectFingerCount,
            onTouchesChanged = viewModel::onTouchesChanged,
            modifier = modifier,
        )
    }
}

@Composable
private fun FingerPlayScreen(
    state: FingerUiState,
    onBack: () -> Unit,
    onFingerCountSelected: (Int) -> Unit,
    onTouchesChanged: (List<FingerTouchInput>, Float, Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(FingerPlayBackground),
        containerColor = FingerPlayBackground,
        topBar = {
            FingerHeader(
                title = "Chọn",
                fingerCount = state.fingerCount,
                onBack = onBack,
                onFingerCountSelected = onFingerCountSelected,
            )
        },
    ) { innerPadding ->
        FingerPlayArea(
            state = state,
            onTouchesChanged = onTouchesChanged,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}

@Composable
private fun FingerHeader(
    title: String,
    fingerCount: Int,
    onBack: () -> Unit,
    onFingerCountSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    SpinTopBar(
        title = title,
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = "Quay lại",
        onNavigationClick = onBack,
        centerTitle = false,
        titleStartPadding = 39.dp,
        modifier = modifier,
        actions = {
            Box(modifier = Modifier.offset(x = 6.dp)) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(FingerMenuGreen)
                        .clickable { expanded = true },
                    contentAlignment = Alignment.Center,
                ) {
                    SpinIcon(
                        glyph = SpinIconGlyph.ChevronDown,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp),
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(FingerMenuBackground)
                        .defaultMinSize(minWidth = 112.dp),
                ) {
                    (FingerRoundRules.MIN_FINGER_COUNT..FingerRoundRules.MAX_FINGER_COUNT).forEach { count ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = count.toString(),
                                    color = if (count == fingerCount) FingerMenuGreen else Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                )
                            },
                            onClick = {
                                expanded = false
                                onFingerCountSelected(count)
                            },
                            modifier = Modifier.height(64.dp),
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun FingerPlayArea(
    state: FingerUiState,
    onTouchesChanged: (List<FingerTouchInput>, Float, Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .background(FingerPlayBackground)
            .pointerInput(state.fingerCount) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val touches = event.changes
                            .filter { it.pressed }
                            .map { change ->
                                FingerTouchInput(
                                    id = change.id.value,
                                    x = change.position.x,
                                    y = change.position.y,
                                )
                            }
                        onTouchesChanged(
                            touches,
                            size.width.toFloat(),
                            size.height.toFloat(),
                        )
                    }
                }
            },
    ) {
        if (state.stage == FingerStage.Waiting && state.points.isEmpty()) {
            FingerHint(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp),
            )
        }

        if (state.stage is FingerStage.CountingDown) {
            Text(
                text = "${state.stage.secondsLeft}S",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 38.dp),
                color = Color.White,
                fontSize = 38.sp,
                lineHeight = 42.sp,
                fontWeight = FontWeight.Black,
            )
        }

        when (state.stage) {
            is FingerStage.CountingDown,
            FingerStage.Waiting -> {
                state.points.forEach { point ->
                    FingerTouchPulse(
                        point = point,
                        maxWidth = maxWidth,
                        maxHeight = maxHeight,
                    )
                }
            }

            FingerStage.QuickResult -> {
                FingerResultPoints(
                    points = state.points,
                    winnerId = state.winnerId,
                    maxWidth = maxWidth,
                    maxHeight = maxHeight,
                )
            }

            FingerStage.FinalResult -> Unit
        }
    }
}

@Composable
private fun FingerHint(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "ĐẶT NGÓN TAY LÊN MÀN\nHÌNH",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Black,
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Giữ nguyên trong 2 giây để bắt đầu",
            color = SpinColors.TextMuted,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun FingerFinalResultScreen(
    state: FingerUiState,
    onShare: () -> Unit,
    onRetry: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SpinResultScreen(
        title = "Kết Quả",
        onHome = onHome,
        onShare = onShare,
        onRetry = onRetry,
        modifier = modifier,
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
        ) {
            FingerResultPoints(
                points = state.points,
                winnerId = state.winnerId,
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                constrainToCard = true,
            )
        }
    }
}

@Composable
private fun FingerTouchPulse(
    point: FingerPoint,
    maxWidth: Dp,
    maxHeight: Dp,
    modifier: Modifier = Modifier,
) {
    val size = 76.dp
    val color = fingerColor(point.colorIndex)

    Canvas(
        modifier = modifier
            .size(size)
            .offset(
                x = maxWidth * point.xRatio - size / 2,
                y = maxHeight * point.yRatio - size / 2,
            ),
    ) {
        val center = Offset(this.size.width / 2f, this.size.height / 2f)
        drawCircle(color.copy(alpha = 0.11f), radius = this.size.minDimension * 0.49f, center = center)
        drawCircle(color.copy(alpha = 0.17f), radius = this.size.minDimension * 0.40f, center = center)
        drawCircle(color.copy(alpha = 0.34f), radius = this.size.minDimension * 0.32f, center = center)
        drawCircle(color, radius = this.size.minDimension * 0.265f, center = center)
    }
}

@Composable
private fun FingerResultPoints(
    points: List<FingerPoint>,
    winnerId: Long?,
    maxWidth: Dp,
    maxHeight: Dp,
    modifier: Modifier = Modifier,
    constrainToCard: Boolean = false,
) {
    Box(modifier = modifier.fillMaxSize()) {
        points.forEach { point ->
            val xRatio = if (constrainToCard) {
                point.xRatio.coerceIn(0.18f, 0.82f)
            } else {
                point.xRatio.coerceIn(0.12f, 0.88f)
            }
            val yRatio = if (constrainToCard) {
                point.yRatio.coerceIn(0.12f, 0.88f)
            } else {
                point.yRatio.coerceIn(0.10f, 0.90f)
            }
            FingerResultPoint(
                isWinner = point.id == winnerId,
                x = maxWidth * xRatio,
                y = maxHeight * yRatio,
                compact = constrainToCard,
            )
        }
    }
}

@Composable
private fun FingerResultPoint(
    isWinner: Boolean,
    x: Dp,
    y: Dp,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val pointSize = if (compact) 34.dp else 48.dp
    val haloSize = if (compact) 52.dp else 76.dp
    val calloutWidth = if (compact) 54.dp else 80.dp
    val calloutHeight = if (compact) 34.dp else 42.dp
    val calloutOffsetY = if (compact) 58.dp else 68.dp
    val calloutFontSize = if (compact) 13.sp else 20.sp

    Box(modifier = modifier.fillMaxSize()) {
        if (isWinner) {
            Box(
                modifier = Modifier
                    .width(calloutWidth)
                    .height(calloutHeight)
                    .offset(x = x - calloutWidth / 2, y = y - calloutOffsetY)
                    .clip(
                        RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp,
                            bottomEnd = 14.dp,
                            bottomStart = 0.dp,
                        )
                    )
                    .background(Color.White),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "THẮNG",
                    color = FingerWinRed,
                    fontSize = calloutFontSize,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                )
            }
        }

        Box(
            modifier = Modifier
                .size(haloSize)
                .offset(x = x - haloSize / 2, y = y - haloSize / 2),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                drawCircle(Color.Black.copy(alpha = 0.05f), radius = size.minDimension * 0.49f, center = center)
                drawCircle(Color.Black.copy(alpha = 0.10f), radius = size.minDimension * 0.36f, center = center)
                drawCircle(Color.Black.copy(alpha = 0.16f), radius = size.minDimension * 0.25f, center = center)
            }
            Box(
                modifier = Modifier
                    .size(pointSize)
                    .clip(CircleShape)
                    .background(FingerPointDark),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (isWinner) "🎉" else "😢",
                    fontSize = if (compact) 17.sp else 26.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

private fun fingerColor(index: Int): Color = when (index % 5) {
    0 -> Color(0xFFFF3B34)
    1 -> Color(0xFF34C85A)
    2 -> Color(0xFF39A9F2)
    3 -> Color(0xFFFFD21E)
    else -> Color(0xFFE95BFF)
}

private val FingerPlayBackground = Color(0xFF151126)
private val FingerMenuGreen = Color(0xFF21822F)
private val FingerMenuBackground = Color(0xFF393347)
private val FingerPointDark = Color(0xFF100D1F)
private val FingerWinRed = Color(0xFFF04B55)
