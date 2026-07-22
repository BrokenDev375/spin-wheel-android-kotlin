package com.vga.spinwheel.ui.screen.finger

import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
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
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

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

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(82.dp)
            .background(SpinColors.Background)
            .padding(horizontal = SpinSpacing.ScreenHorizontal),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SpinIconButton(
            glyph = SpinIconGlyph.Back,
            contentDescription = "Quay lại",
            onClick = onBack,
            tint = SpinColors.TextPrimary,
        )
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            color = SpinColors.TextPrimary,
            style = MaterialTheme.typography.headlineSmall,
        )
        Box {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(FingerMenuGreen)
                    .clickable { expanded = true },
                contentAlignment = Alignment.Center,
            ) {
                SpinIcon(
                    glyph = SpinIconGlyph.ChevronDown,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp),
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(FingerMenuBackground)
                    .defaultMinSize(minWidth = 118.dp),
            ) {
                (FingerRoundRules.MIN_FINGER_COUNT..FingerRoundRules.MAX_FINGER_COUNT).forEach { count ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = count.toString(),
                                color = if (count == fingerCount) FingerMenuGreen else Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        },
                        onClick = {
                            expanded = false
                            onFingerCountSelected(count)
                        },
                    )
                }
            }
        }
    }
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
                    .padding(top = 56.dp),
                color = Color.White,
                fontSize = 64.sp,
                lineHeight = 68.sp,
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
            fontSize = 30.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Black,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Giữ nguyên trong 2 giây để bắt đầu",
            color = SpinColors.TextMuted,
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            lineHeight = 30.sp,
            fontWeight = FontWeight.Bold,
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background)
            .padding(horizontal = 20.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp),
        ) {
            Text(
                text = "Kết Quả",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
            )
            SpinIconButton(
                glyph = SpinIconGlyph.Home,
                contentDescription = "Về trang chủ",
                onClick = onHome,
                modifier = Modifier.align(Alignment.CenterEnd),
                tint = Color.White,
            )
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(18.dp))
                .background(FingerResultCard)
                .border(1.5.dp, Color.White.copy(alpha = 0.62f), RoundedCornerShape(18.dp)),
        ) {
            FingerResultPoints(
                points = state.points,
                winnerId = state.winnerId,
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                constrainToCard = true,
            )
        }

        Spacer(modifier = Modifier.height(34.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier
                    .height(58.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(FingerShareBlue)
                    .clickable(onClick = onShare)
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SpinIcon(
                    glyph = SpinIconGlyph.Share,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp),
                )
                Text(
                    text = "Chia sẻ kết\nquả",
                    color = Color.White,
                    fontSize = 20.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(FingerRetryRed)
                .clickable(onClick = onRetry),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Thử lại",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun FingerTouchPulse(
    point: FingerPoint,
    maxWidth: Dp,
    maxHeight: Dp,
    modifier: Modifier = Modifier,
) {
    val size = 106.dp
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
        drawCircle(color.copy(alpha = 0.17f), radius = this.size.minDimension * 0.34f, center = center)
        drawCircle(color.copy(alpha = 0.34f), radius = this.size.minDimension * 0.25f, center = center)
        drawCircle(color, radius = this.size.minDimension * 0.22f, center = center)
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
            )
        }
    }
}

@Composable
private fun FingerResultPoint(
    isWinner: Boolean,
    x: Dp,
    y: Dp,
    modifier: Modifier = Modifier,
) {
    val pointSize = 66.dp
    val haloSize = 118.dp

    Box(modifier = modifier.fillMaxSize()) {
        if (isWinner) {
            Box(
                modifier = Modifier
                    .offset(x = x - 72.dp, y = y - 104.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomEnd = 18.dp,
                            bottomStart = 0.dp,
                        )
                    )
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "THẮNG",
                    color = FingerWinRed,
                    fontSize = 25.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Black,
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
                    fontSize = 28.sp,
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
private val FingerResultCard = Color(0xFF3D3D3C)
private val FingerPointDark = Color(0xFF100D1F)
private val FingerShareBlue = Color(0xFF39A9F2)
private val FingerRetryRed = Color(0xFFDE3D2D)
private val FingerWinRed = Color(0xFFF04B55)
