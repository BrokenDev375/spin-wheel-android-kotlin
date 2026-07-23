package com.vga.spinwheel.ui.screen.intro

import android.app.Activity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.annotation.StringRes
import androidx.hilt.navigation.compose.hiltViewModel
import com.vga.spinwheel.advertisement.AdManager
import com.vga.spinwheel.advertisement.AdPositions
import com.vga.spinwheel.advertisement.NativeAdSlot
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinPrimaryButton
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing
import kotlin.math.sin
import kotlinx.coroutines.delay

@Composable
fun IntroScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IntroViewModel = hiltViewModel(),
) {
    val activity = LocalContext.current as? Activity
    val adPositions = remember { AdPositions.current() }
    var pageIndex by remember { mutableIntStateOf(0) }
    var continueEnabled by remember { mutableStateOf(false) }
    var advancePending by remember { mutableStateOf(false) }
    val page = introPagesI18n[pageIndex]
    val slideNumber = pageIndex + 1
    val hasInlineAd = adPositions.hasInline(slideNumber)

    LaunchedEffect(pageIndex, hasInlineAd) {
        continueEnabled = !hasInlineAd
        advancePending = false
        if (hasInlineAd) {
            delay(INTRO_AD_TIMEOUT_MS)
            continueEnabled = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background)
            .padding(horizontal = SpinSpacing.ScreenHorizontal)
            .padding(top = 24.dp, bottom = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(SpinRadius.Sheet))
                .background(SpinColors.BackgroundDeep),
            contentAlignment = Alignment.Center,
        ) {
            if (hasInlineAd) {
                NativeAdSlot(
                    placement = "native_intro$slideNumber",
                    onResolved = { continueEnabled = true },
                )
            } else {
                IntroVisual(
                    type = page.type,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = stringResource(page.titleRes),
            color = SpinColors.Action,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(page.descriptionRes),
            color = SpinColors.TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(22.dp))

        PageIndicator(
            pageCount = introPagesI18n.size,
            activeIndex = pageIndex,
            onPageClick = { pageIndex = it },
        )

        Spacer(modifier = Modifier.height(28.dp))

        SpinPrimaryButton(
            text = stringResource(
                if (pageIndex == introPagesI18n.lastIndex) {
                    R.string.intro_start
                } else {
                    R.string.intro_continue
                }
            ),
            onClick = {
                if (!advancePending) {
                    advancePending = true
                    val advance = {
                        if (pageIndex == introPagesI18n.lastIndex) {
                            viewModel.markIntroDone(onSaved = onFinished)
                        } else {
                            pageIndex += 1
                        }
                    }
                    if (adPositions.hasModalAfter(slideNumber)) {
                        AdManager.showNativeInter(
                            activity = activity,
                            placement = "native_inter_intro",
                            onNext = advance,
                        )
                    } else {
                        advance()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = continueEnabled && !advancePending,
        )
    }
}

@Composable
private fun PageIndicator(
    pageCount: Int,
    activeIndex: Int,
    onPageClick: (Int) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(width = if (index == activeIndex) 34.dp else 14.dp, height = 14.dp)
                    .clip(CircleShape)
                    .background(if (index == activeIndex) SpinColors.Action else Color(0xFF45515D))
                    .clickable { onPageClick(index) },
            )
        }
    }
}

@Composable
private fun IntroVisual(
    type: IntroVisualType,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.aspectRatio(0.72f)) {
        when (type) {
            IntroVisualType.WHEEL -> {
                val center = Offset(size.width * 0.5f, size.height * 0.54f)
                val radius = size.minDimension * 0.43f
                val colors = listOf(
                    Color(0xFFFF6B63),
                    Color(0xFFFFD21E),
                    Color(0xFF36D399),
                    Color(0xFF697586),
                )
                repeat(10) { index ->
                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = index * 36f - 90f,
                        sweepAngle = 36f,
                        useCenter = true,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2f, radius * 2f),
                    )
                }
                drawCircle(Color.White, radius, center, style = Stroke(width = 9.dp.toPx()))
                drawCircle(Color.White, radius * 0.17f, center)
                val pointer = Path().apply {
                    moveTo(center.x, center.y - radius - 6.dp.toPx())
                    lineTo(center.x - 18.dp.toPx(), center.y - radius + 42.dp.toPx())
                    lineTo(center.x + 18.dp.toPx(), center.y - radius + 42.dp.toPx())
                    close()
                }
                drawPath(pointer, Color.White)
            }

            IntroVisualType.GRID -> {
                val cardWidth = size.width * 0.38f
                val cardHeight = size.height * 0.15f
                val colors = listOf(
                    Color(0xFF9255FF),
                    Color(0xFFECA63A),
                    Color(0xFFFF7EB6),
                    Color(0xFF3ED8E0),
                    Color(0xFF222222),
                    Color(0xFFAEDC65),
                )
                repeat(6) { index ->
                    val col = index % 2
                    val row = index / 2
                    val left = size.width * 0.08f + col * size.width * 0.5f
                    val top = size.height * 0.12f + row * size.height * 0.21f
                    drawRoundRect(
                        color = colors[index],
                        topLeft = Offset(left, top),
                        size = Size(cardWidth, cardHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx()),
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.24f),
                        radius = cardHeight * 0.35f,
                        center = Offset(left + cardWidth * 0.72f, top + cardHeight * 0.45f),
                    )
                }
            }

            IntroVisualType.FINGER -> {
                val circles = listOf(
                    Offset(size.width * 0.25f, size.height * 0.35f) to Color(0xFFFFD83D),
                    Offset(size.width * 0.72f, size.height * 0.22f) to Color(0xFFFF7A2F),
                    Offset(size.width * 0.18f, size.height * 0.7f) to Color(0xFFFF666A),
                    Offset(size.width * 0.66f, size.height * 0.78f) to Color(0xFF10B8B8),
                )
                circles.forEach { (center, color) ->
                    drawCircle(color, size.minDimension * 0.12f, center)
                    drawCircle(
                        color = color.copy(alpha = 0.72f),
                        radius = size.minDimension * 0.15f,
                        center = center,
                        style = Stroke(
                            width = 5.dp.toPx(),
                            cap = StrokeCap.Round,
                        ),
                    )
                }
                drawLine(
                    color = Color.White.copy(alpha = 0.84f),
                    start = Offset(size.width * 0.5f, size.height * 0.36f),
                    end = Offset(size.width * 0.5f, size.height * 0.44f),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }

            IntroVisualType.AI -> {
                val panel = Size(size.width * 0.78f, size.height * 0.58f)
                val left = size.width * 0.11f
                val top = size.height * 0.15f
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(left, top),
                    size = Size(panel.width, panel.height * 0.18f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(14.dp.toPx()),
                )
                drawRoundRect(
                    color = Color(0xFFE04335),
                    topLeft = Offset(left, top + panel.height * 0.24f),
                    size = Size(panel.width, panel.height * 0.18f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(14.dp.toPx()),
                )
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.12f),
                    topLeft = Offset(left, top + panel.height * 0.5f),
                    size = Size(panel.width, panel.height * 0.42f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx()),
                )
                repeat(8) { index ->
                    val y = top + panel.height * 0.56f + index * 16.dp.toPx()
                    val x = left + panel.width * (0.36f + 0.04f * sin(index.toFloat()))
                    drawCircle(Color(0xFFFFD21E), 4.dp.toPx(), Offset(x, y))
                    drawLine(
                        color = Color.White.copy(alpha = 0.72f),
                        start = Offset(x + 12.dp.toPx(), y),
                        end = Offset(left + panel.width * 0.72f, y),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round,
                    )
                }
                val fingerBase = Offset(size.width * 0.78f, size.height * 0.72f)
                drawCircle(Color(0xFFFFD4BE), size.minDimension * 0.16f, fingerBase)
                drawLine(
                    color = Color(0xFFFFE0CC),
                    start = Offset(fingerBase.x, fingerBase.y),
                    end = Offset(size.width * 0.7f, size.height * 0.42f),
                    strokeWidth = size.minDimension * 0.16f,
                    cap = StrokeCap.Round,
                )
            }
        }
    }
}

private enum class IntroVisualType {
    WHEEL,
    GRID,
    FINGER,
    AI,
}

private data class IntroPageI18n(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    val type: IntroVisualType,
)

private val introPagesI18n = listOf(
    IntroPageI18n(
        titleRes = R.string.intro_title_1,
        descriptionRes = R.string.intro_description_1,
        type = IntroVisualType.WHEEL,
    ),
    IntroPageI18n(
        titleRes = R.string.intro_title_2,
        descriptionRes = R.string.intro_description_2,
        type = IntroVisualType.GRID,
    ),
    IntroPageI18n(
        titleRes = R.string.intro_title_3,
        descriptionRes = R.string.intro_description_3,
        type = IntroVisualType.FINGER,
    ),
    IntroPageI18n(
        titleRes = R.string.intro_title_4,
        descriptionRes = R.string.intro_description_4,
        type = IntroVisualType.AI,
    ),
)

private const val INTRO_AD_TIMEOUT_MS = 5_000L
