package com.vga.spinwheel.ui.screen.bottle

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BottleScreen(
    viewModel: BottleViewModel,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    if (state.stage == BottleStage.Result) {
        BottleResultScreen(
            state = state,
            onShare = { shareBottleResult(context, viewModel.shareText()) },
            onRetry = viewModel::retryFromResult,
            onHome = {
                viewModel.resetSpin()
                onHome()
            },
            modifier = modifier,
        )
    } else {
        BottleHomeScreen(
            state = state,
            onBack = {
                viewModel.cancelOngoingSpin()
                onBack()
            },
            onOpenSettings = onOpenSettings,
            onStart = viewModel::startSpin,
            onReset = viewModel::resetSpin,
            modifier = modifier,
        )
    }
}

@Composable
fun BottleSettingsScreen(
    viewModel: BottleViewModel,
    onBack: () -> Unit,
    onOpenLabels: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            BottleHeader(
                title = "Tùy chỉnh",
                onBack = onBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = SpinSpacing.ScreenHorizontal)
                .padding(top = 60.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            BottleSettingRow(
                title = "Thời lượng hoạt hình",
                trailing = {
                    BottleStepper(
                        value = "${state.durationSeconds}s",
                        onMinus = { viewModel.updateDuration(state.durationSeconds - 1) },
                        onPlus = { viewModel.updateDuration(state.durationSeconds + 1) },
                    )
                },
            )

            BottleSettingRow(
                title = "Quay Chai",
                onClick = {
                    viewModel.beginStyleSelection()
                    onOpenLabels()
                },
                trailing = {
                    SpinIcon(
                        glyph = SpinIconGlyph.ChevronRight,
                        tint = SpinColors.IconMuted,
                        modifier = Modifier.size(30.dp),
                    )
                },
            )
        }
    }
}

@Composable
fun BottleLabelScreen(
    viewModel: BottleViewModel,
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
            BottleHeader(
                title = "Mẫu Chai",
                onBack = onBack,
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.saveSelectedStyle()
                            onBack()
                        },
                    ) {
                        Text(
                            text = "Lưu",
                            color = SpinColors.Action,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = SpinSpacing.ScreenHorizontal,
                top = 42.dp,
                end = SpinSpacing.ScreenHorizontal,
                bottom = 24.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            itemsIndexed(BottleStyles.all) { index, style ->
                BottleLabelCard(
                    style = style,
                    selected = index == state.tempStyleIndex,
                    onClick = { viewModel.selectTempStyle(index) },
                )
            }
        }
    }
}

@Composable
private fun BottleHomeScreen(
    state: BottleUiState,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onStart: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            BottleHeader(
                title = "Quay Chai",
                onBack = onBack,
            )
        },
        bottomBar = {
            BottleBottomBar(
                isSpinning = state.isSpinning,
                onOpenSettings = onOpenSettings,
                onStart = onStart,
                onReset = onReset,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            AnimatedBottleArt(
                style = BottleStyles.get(state.styleIndex),
                angle = state.lastAngle,
                isSpinning = state.isSpinning,
                modifier = Modifier.size(width = 132.dp, height = 342.dp),
            )
        }
    }
}

@Composable
private fun BottleResultScreen(
    state: BottleUiState,
    onShare: () -> Unit,
    onRetry: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = "Kết Quả",
                actions = {
                    SpinIconButton(
                        glyph = SpinIconGlyph.Home,
                        contentDescription = "Về trang chủ",
                        onClick = onHome,
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
            Spacer(modifier = Modifier.height(34.dp))

            BottlePreviewCard(
                style = BottleStyles.get(state.styleIndex),
                angle = state.lastAngle,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 410.dp)
                    .weight(1f),
            )

            Spacer(modifier = Modifier.height(34.dp))

            BottleShareButton(onClick = onShare)

            Spacer(modifier = Modifier.height(40.dp))

            BottleRetryButton(
                text = "Thử lại",
                onClick = onRetry,
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun BottleHeader(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(82.dp)
            .padding(
                horizontal = SpinSpacing.ScreenHorizontal,
                vertical = 8.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SpinIconButton(
            glyph = SpinIconGlyph.Back,
            contentDescription = "Quay lại",
            onClick = onBack,
            tint = Color.White,
        )
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 14.dp),
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        actions?.invoke()
    }
}

@Composable
private fun BottleBottomBar(
    isSpinning: Boolean,
    onOpenSettings: () -> Unit,
    onStart: () -> Unit,
    onReset: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SpinColors.Background)
            .padding(horizontal = SpinSpacing.ScreenHorizontal, vertical = 28.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BottleToolButton(
            glyph = SpinIconGlyph.Sliders,
            contentDescription = "Tùy chỉnh",
            enabled = !isSpinning,
            onClick = onOpenSettings,
        )
        BottlePrimaryActionButton(
            text = if (isSpinning) "ĐANG QUAY..." else "BẮT ĐẦU",
            enabled = !isSpinning,
            onClick = onStart,
            modifier = Modifier.weight(1f),
        )
        BottleToolButton(
            glyph = SpinIconGlyph.Reset,
            contentDescription = "Reset",
            enabled = !isSpinning,
            onClick = onReset,
        )
    }
}

@Composable
private fun BottleToolButton(
    glyph: SpinIconGlyph,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(width = 58.dp, height = 58.dp)
            .alpha(if (enabled) 1f else 0.62f)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF393347))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        SpinIcon(
            glyph = glyph,
            tint = Color.White,
            modifier = Modifier.size(30.dp),
        )
    }
}

@Composable
private fun BottlePrimaryActionButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(58.dp)
            .alpha(if (enabled) 1f else 0.62f)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF393347))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = if (isSpinningText(text)) 16.sp else 21.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun BottleSettingRow(
    title: String,
    trailing: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF393347))
            .then(if (onClick == null) Modifier else Modifier.clickable(onClick = onClick))
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = Color.White,
            fontSize = 21.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.width(12.dp))
        trailing()
    }
}

@Composable
private fun BottleStepper(
    value: String,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        BottleStepperButton(text = "-", onClick = onMinus)
        Text(
            text = value,
            modifier = Modifier.width(42.dp),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
        )
        BottleStepperButton(text = "+", onClick = onPlus)
    }
}

@Composable
private fun BottleStepperButton(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(36.dp)
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
            lineHeight = 24.sp,
        )
    }
}

@Composable
private fun BottleLabelCard(
    style: BottleStyle,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(208.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Brush.linearGradient(listOf(style.cardStart, style.cardEnd)))
            .border(
                width = if (selected) 3.dp else 2.dp,
                color = if (selected) SpinColors.Action else Color.White,
                shape = RoundedCornerShape(14.dp),
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        BottleArt(
            style = style,
            angle = 0f,
            modifier = Modifier.size(width = 64.dp, height = 166.dp),
        )

        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF28E982)),
                contentAlignment = Alignment.Center,
            ) {
                SpinIcon(
                    glyph = SpinIconGlyph.Check,
                    tint = Color.White,
                    modifier = Modifier.size(21.dp),
                )
            }
        }
    }
}

@Composable
private fun BottlePreviewCard(
    style: BottleStyle,
    angle: Int,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF3D3D3C))
            .border(
                width = 1.5.dp,
                color = Color.White.copy(alpha = 0.62f),
                shape = RoundedCornerShape(18.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        val radians = Math.toRadians(angle.toDouble())
        val sinValue = abs(sin(radians)).toFloat()
        val cosValue = abs(cos(radians)).toFloat()
        val availableWidth = maxOf(maxWidth - 56.dp, 120.dp)
        val availableHeight = maxOf(maxHeight - 56.dp, 180.dp)
        val widthFactor = (BottleAspectRatio * cosValue + sinValue).coerceAtLeast(0.1f)
        val heightFactor = (BottleAspectRatio * sinValue + cosValue).coerceAtLeast(0.1f)
        val bottleHeight = minOf(
            availableWidth / widthFactor,
            availableHeight / heightFactor,
            380.dp,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .background(SpinColors.Background)
                .align(Alignment.Center),
        )
        BottleArt(
            style = style,
            angle = angle.toFloat(),
            modifier = Modifier.size(
                width = bottleHeight * BottleAspectRatio,
                height = bottleHeight,
            ),
        )
    }
}

@Composable
private fun BottleShareButton(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(222.dp)
            .height(62.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(Color(0xFF39A9F2))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            SpinIcon(
                glyph = SpinIconGlyph.Share,
                tint = Color.White,
                modifier = Modifier.size(30.dp),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Chia sẻ kết quả",
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun BottleRetryButton(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFDE3D2D))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}

@Composable
private fun AnimatedBottleArt(
    style: BottleStyle,
    angle: Int,
    isSpinning: Boolean,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bottle-spin")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 220, easing = LinearEasing),
        ),
        label = "bottle-spin-angle",
    )
    val settledAngle by animateFloatAsState(
        targetValue = angle.toFloat(),
        animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing),
        label = "bottle-settle-angle",
    )

    BottleArt(
        style = style,
        angle = if (isSpinning) spinAngle else settledAngle,
        modifier = modifier,
    )
}

@Composable
private fun BottleArt(
    style: BottleStyle,
    angle: Float,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .aspectRatio(132f / 342f)
            .graphicsLayer {
                rotationZ = angle
                transformOrigin = TransformOrigin(0.5f, 0.58f)
            },
    ) {
        val sx = size.width / BottleBaseWidth
        val sy = size.height / BottleBaseHeight
        fun x(value: Float) = value * sx
        fun y(value: Float) = value * sy

        drawRoundRect(
            color = style.cap,
            topLeft = Offset(x(41f), y(0f)),
            size = Size(x(50f), y(16f)),
            cornerRadius = CornerRadius(x(5f), y(5f)),
        )

        drawRoundRect(
            brush = Brush.horizontalGradient(
                colors = listOf(BottleCreamLight, BottleCream, BottleCreamDark),
                startX = x(37f),
                endX = x(95f),
            ),
            topLeft = Offset(x(37f), y(12f)),
            size = Size(x(58f), y(132f)),
            cornerRadius = CornerRadius(x(18f), y(18f)),
        )

        drawRoundRect(
            color = Color(0xFF7E5721).copy(alpha = 0.18f),
            topLeft = Offset(x(31f), y(34f)),
            size = Size(x(70f), y(12f)),
            cornerRadius = CornerRadius(x(7f), y(7f)),
        )

        val shoulder = Path().apply {
            moveTo(x(36.5f), y(124f))
            lineTo(x(95.5f), y(124f))
            lineTo(x(112f), y(186f))
            lineTo(x(20f), y(186f))
            close()
        }
        drawPath(
            path = shoulder,
            brush = Brush.horizontalGradient(
                colors = listOf(style.glassDark, style.glass, style.glassDark),
                startX = x(20f),
                endX = x(112f),
            ),
        )

        drawRoundRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    style.glassDark,
                    style.glass,
                    BottleCreamLight,
                    style.glass,
                    style.glassDark,
                ),
                startX = x(18f),
                endX = x(114f),
            ),
            topLeft = Offset(x(18f), y(164f)),
            size = Size(x(96f), y(178f)),
            cornerRadius = CornerRadius(x(22f), y(24f)),
        )

        drawRect(
            color = style.label,
            topLeft = Offset(x(18f), y(232f)),
            size = Size(x(96f), y(68f)),
        )

        drawRoundRect(
            color = Color.White.copy(alpha = 0.22f),
            topLeft = Offset(x(33f), y(182f)),
            size = Size(x(12f), y(132f)),
            cornerRadius = CornerRadius(x(10f), y(10f)),
        )

        drawRect(
            color = Color.White.copy(alpha = 0.11f),
            topLeft = Offset(x(53f), y(0f)),
            size = Size(x(18f), y(338f)),
        )

        drawRect(
            color = Color.Black.copy(alpha = 0.11f),
            topLeft = Offset(x(72f), y(128f)),
            size = Size(x(20f), y(208f)),
        )

        drawOval(
            color = Color.Black.copy(alpha = 0.14f),
            topLeft = Offset(x(30f), y(318f)),
            size = Size(x(72f), y(34f)),
        )
    }
}

private fun isSpinningText(text: String): Boolean =
    text.contains("QUAY", ignoreCase = true)

private fun shareBottleResult(
    context: Context,
    text: String,
) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Kết quả quay chai")
        putExtra(Intent.EXTRA_TEXT, text)
    }
    try {
        context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"))
    } catch (_: ActivityNotFoundException) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("Kết quả quay chai", text))
        Toast.makeText(context, "Đã sao chép kết quả quay chai", Toast.LENGTH_SHORT).show()
    }
}

private const val BottleBaseWidth = 132f
private const val BottleBaseHeight = 342f
private const val BottleAspectRatio = BottleBaseWidth / BottleBaseHeight

private val BottleCreamLight = Color(0xFFFFF2C5)
private val BottleCream = Color(0xFFF8DDA0)
private val BottleCreamDark = Color(0xFFF2CC76)
