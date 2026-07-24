package com.vga.spinwheel.ui.screen.bottle

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinResultScreen
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

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
    val resultTitle = stringResource(R.string.results)
    val bottleTitle = stringResource(R.string.spinBottle)
    val shareTitle = stringResource(R.string.sharereust)
    val shareSuccess = stringResource(R.string.share_success)

    if (state.stage == BottleStage.Result) {
        BottleResultScreen(
            state = state,
            onShare = {
                shareBottleResult(
                    context = context,
                    text = "$resultTitle $bottleTitle: ${state.lastAngle}°",
                    subject = "$resultTitle $bottleTitle",
                    chooserTitle = shareTitle,
                    fallbackToast = shareSuccess,
                )
            },
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
            .background(SpinColors.Background)
            .statusBarsPadding(),
        containerColor = SpinColors.Background,
        topBar = {
            BottleHeader(
                title = stringResource(R.string.customsize),
                onBack = onBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = SpinSpacing.ScreenHorizontal)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            BottleSettingRow(
                title = stringResource(R.string.duration),
                trailing = {
                    BottleStepper(
                        value = "${state.durationSeconds}s",
                        onMinus = { viewModel.updateDuration(state.durationSeconds - 1) },
                        onPlus = { viewModel.updateDuration(state.durationSeconds + 1) },
                    )
                },
            )

            BottleSettingRow(
                title = stringResource(R.string.spinBottle),
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
            .background(SpinColors.Background)
            .statusBarsPadding(),
        containerColor = SpinColors.Background,
        topBar = {
            BottleHeader(
                title = stringResource(R.string.Tembottle),
                onBack = onBack,
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.saveSelectedStyle()
                            onBack()
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.save),
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
                top = 14.dp,
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
            .background(SpinColors.Background)
            .statusBarsPadding(),
        containerColor = SpinColors.Background,
        topBar = {
            BottleHeader(
                title = stringResource(R.string.spinBottle),
                onBack = onBack,
            )
        },
        bottomBar = {
            BottleBottomBar(
                isSpinning = state.isRunning,
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
                isSettling = state.stage == BottleStage.Settled,
                settleDurationMillis = state.settleDurationMillis,
                modifier = Modifier.size(width = 120.dp, height = 300.dp),
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
    SpinResultScreen(
        onHome = onHome,
        onShare = onShare,
        onRetry = onRetry,
        modifier = modifier
            .background(SpinColors.Background)
            .statusBarsPadding(),
        cardHeight = 450.dp,
        cardContentPadding = 0.dp,
    ) {
        BottlePreviewCard(
            style = BottleStyles.get(state.styleIndex),
            angle = state.lastAngle,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun BottleHeader(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable (() -> Unit)? = null,
) {
    SpinTopBar(
        title = title,
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = stringResource(R.string.content_description_back),
        onNavigationClick = onBack,
        centerTitle = false,
        titleStartPadding = 39.dp,
        actions = { actions?.invoke() },
        modifier = modifier,
    )
}

@Composable
private fun BottleBottomBar(
    isSpinning: Boolean,
    onOpenSettings: () -> Unit,
    onStart: () -> Unit,
    onReset: () -> Unit,
) {
    val customizeLabel = stringResource(R.string.customsize)
    val startLabel = stringResource(R.string.start).uppercase()
    val spinningLabel = stringResource(R.string.spinning).uppercase()
    val restartLabel = stringResource(R.string.restart)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SpinColors.Background)
            .navigationBarsPadding()
            .padding(horizontal = SpinSpacing.ScreenHorizontal, vertical = 80.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BottleToolButton(
            glyph = SpinIconGlyph.Sliders,
            contentDescription = customizeLabel,
            enabled = !isSpinning,
            onClick = onOpenSettings,
        )
        BottlePrimaryActionButton(
            text = if (isSpinning) "$spinningLabel..." else startLabel,
            enabled = !isSpinning,
            onClick = onStart,
            modifier = Modifier.weight(1f),
        )
        BottleToolButton(
            glyph = SpinIconGlyph.Reset,
            contentDescription = restartLabel,
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
            .size(width = 58.dp, height = 30.dp)
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
            .height(30.dp)
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
            fontSize = if (text.length > 10) 16.sp else 21.sp,
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
            .height(60.dp)
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
            fontSize = 18.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.width(10.dp))
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
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        BottleStepperButton(text = "-", onClick = onMinus)
        Text(
            text = value,
            modifier = Modifier.width(36.dp),
            color = Color.White,
            fontSize = 18.sp,
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
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .background(SpinColors.Background),
        )

        BottleArt(
            style = style,
            angle = angle.toFloat(),
            modifier = Modifier.size(width = 100.dp, height = 300.dp),
        )
    }
}
@Composable
private fun AnimatedBottleArt(
    style: BottleStyle,
    angle: Int,
    isSpinning: Boolean,
    isSettling: Boolean,
    settleDurationMillis: Int,
    modifier: Modifier = Modifier,
) {
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(isSpinning, isSettling, angle, settleDurationMillis) {
        when {
            isSpinning -> {
                while (true) {
                    rotation.animateTo(
                        targetValue = rotation.value + BottleRoundRules.ANGLE_RANGE_DEGREES,
                        animationSpec = tween(
                            durationMillis = BOTTLE_FAST_SPIN_TURN_MILLIS,
                            easing = LinearEasing,
                        ),
                    )
                }
            }

            isSettling -> {
                val durationMillis = settleDurationMillis.coerceAtLeast(0)
                if (durationMillis == 0) {
                    rotation.snapTo(angle.toFloat())
                } else {
                    rotation.animateTo(
                        targetValue = settleTargetAngle(
                            currentAngle = rotation.value,
                            finalAngle = angle,
                            settleDurationMillis = durationMillis,
                        ),
                        animationSpec = tween(
                            durationMillis = durationMillis,
                            easing = LinearOutSlowInEasing,
                        ),
                    )
                    rotation.snapTo(normalizeBottleAngle(rotation.value))
                }
            }

            else -> rotation.snapTo(angle.toFloat())
        }
    }

    BottleArt(
        style = style,
        angle = rotation.value,
        modifier = modifier,
    )
}

private const val BOTTLE_FAST_SPIN_TURN_MILLIS = 300
private const val BOTTLE_SETTLE_TURN_SLICE_MILLIS = 700
private const val BOTTLE_MIN_SETTLE_TURNS = 2
private const val BOTTLE_MAX_SETTLE_TURNS = 10

private fun settleTargetAngle(
    currentAngle: Float,
    finalAngle: Int,
    settleDurationMillis: Int,
): Float {
    val angleRange = BottleRoundRules.ANGLE_RANGE_DEGREES.toFloat()
    val currentNormalized = normalizeBottleAngle(currentAngle)
    val finalNormalized = normalizeBottleAngle(finalAngle.toFloat())
    val remainingDegrees = normalizeBottleAngle(finalNormalized - currentNormalized)
    val extraTurns = (settleDurationMillis / BOTTLE_SETTLE_TURN_SLICE_MILLIS)
        .coerceIn(BOTTLE_MIN_SETTLE_TURNS, BOTTLE_MAX_SETTLE_TURNS)

    return currentAngle + remainingDegrees + extraTurns * angleRange
}

private fun normalizeBottleAngle(angle: Float): Float {
    val angleRange = BottleRoundRules.ANGLE_RANGE_DEGREES.toFloat()
    val normalized = angle % angleRange
    return if (normalized < 0f) normalized + angleRange else normalized
}

@Composable
private fun BottleArt(
    style: BottleStyle,
    angle: Float,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(style.drawableRes),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .graphicsLayer {
                rotationZ = angle
                transformOrigin = TransformOrigin(0.5f, 0.5f)
            },
    )
}

private fun shareBottleResult(
    context: Context,
    text: String,
    subject: String,
    chooserTitle: String,
    fallbackToast: String,
) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    try {
        context.startActivity(Intent.createChooser(shareIntent, chooserTitle))
    } catch (_: ActivityNotFoundException) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(subject, text))
        Toast.makeText(context, fallbackToast, Toast.LENGTH_SHORT).show()
    }
}
