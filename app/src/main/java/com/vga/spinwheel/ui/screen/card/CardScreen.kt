package com.vga.spinwheel.ui.screen.card

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinResultScreen
import com.vga.spinwheel.ui.components.SpinRetryButton
import com.vga.spinwheel.ui.components.SpinShareButton
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun CardScreen(
    viewModel: CardViewModel,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    if (state.stage == CardStage.Result) {
        CardResultScreen(
            state = state,
            onShare = { shareCardResult(context, viewModel.shareText()) },
            onRetry = viewModel::retryFromResult,
            onHome = onHome,
            modifier = modifier,
        )
    } else {
        CardHomeScreen(
            state = state,
            onBack = onBack,
            onOpenSettings = onOpenSettings,
            onShuffle = viewModel::shuffleCards,
            onReset = viewModel::resetCards,
            onFlipCard = viewModel::flipCard,
            modifier = modifier,
        )
    }
}

@Composable
fun CardSettingsScreen(
    viewModel: CardViewModel,
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
            CardHeader(
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
                .padding(top = 22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            CardSettingRow(
                title = "Thời lượng hoạt hình",
                trailing = {
                    CardStepper(
                        value = "${state.settings.durationSeconds}s",
                        onMinus = { viewModel.updateDuration(state.settings.durationSeconds - 1) },
                        onPlus = { viewModel.updateDuration(state.settings.durationSeconds + 1) },
                    )
                },
            )

            CardSettingRow(
                title = "Total Cards:",
                trailing = {
                    CardStepper(
                        value = state.settings.totalCards.toString(),
                        onMinus = { viewModel.updateTotalCards(state.settings.totalCards - 1) },
                        onPlus = { viewModel.updateTotalCards(state.settings.totalCards + 1) },
                    )
                },
            )

            CardSettingRow(
                title = "Số người chiến thắng",
                trailing = {
                    CardStepper(
                        value = state.settings.winners.toString(),
                        onMinus = { viewModel.updateWinners(state.settings.winners - 1) },
                        onPlus = { viewModel.updateWinners(state.settings.winners + 1) },
                    )
                },
            )

            CardSettingRow(
                title = "Card Deck Theme",
                onClick = {
                    viewModel.beginThemeSelection()
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
fun CardLabelScreen(
    viewModel: CardViewModel,
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
            CardHeader(
                title = "Thẻ mẫu",
                onBack = onBack,
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.saveSelectedTheme()
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
                start = 14.dp,
                top = 16.dp,
                end = 14.dp,
                bottom = 24.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            itemsIndexed(CardThemes.all) { index, theme ->
                CardThemeLabelCard(
                    theme = theme,
                    selected = index == state.tempThemeIndex,
                    onClick = { viewModel.selectTempTheme(index) },
                )
            }
        }
    }
}

@Composable
private fun CardHomeScreen(
    state: CardUiState,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onShuffle: () -> Unit,
    onReset: () -> Unit,
    onFlipCard: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = CardThemes.get(state.settings.themeIndex)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            CardHeader(
                title = "Lật Thẻ",
                onBack = onBack,
            )
        },
        bottomBar = {
            CardBottomBar(
                onOpenSettings = onOpenSettings,
                onShuffle = onShuffle,
                onReset = onReset,
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

            WinnerCardPreview(
                theme = theme,
                animationMillis = cardAnimationMillis(state.settings.durationSeconds),
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(top = 72.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                items(state.cards, key = { it.id }) { card ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        FlipCardView(
                            theme = theme,
                            isWinner = card.isWinner,
                            isFaceUp = card.isFlipped,
                            animationMillis = cardAnimationMillis(state.settings.durationSeconds),
                            enabled = state.isShuffled && !card.isFlipped,
                            onClick = { onFlipCard(card.id) },
                            modifier = Modifier
                                .widthIn(max = 94.dp)
                                .fillMaxWidth()
                                .aspectRatio(CardAspectRatio),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardResultScreen(
    state: CardUiState,
    onShare: () -> Unit,
    onRetry: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = CardThemes.get(state.settings.themeIndex)

    SpinResultScreen(
        title = "Kết Quả",
        onHome = onHome,
        onShare = onShare,
        onRetry = onRetry,
        modifier = modifier,
        cardHeight = 454.dp,
        cardContentPadding = 0.dp,
        cardBackgroundColor = SpinColors.Background,
    ) {
        CardResultPanel(
            state = state,
            theme = theme,
        )
    }
}

@Composable
private fun CardHeader(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable (() -> Unit)? = null,
) {
    SpinTopBar(
        title = title,
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = "Quay lại",
        onNavigationClick = onBack,
        centerTitle = false,
        titleStartPadding = 39.dp,
        navigationTint = Color.White,
        actions = { actions?.invoke() },
        modifier = modifier,
    )
}

@Composable
private fun WinnerCardPreview(
    theme: CardTheme,
    animationMillis: Int,
) {
    Column(
        modifier = Modifier.height(140.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF393347))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Thẻ Thắng",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        FlipCardView(
            theme = theme,
            isWinner = true,
            isFaceUp = true,
            animationMillis = animationMillis,
            enabled = false,
            onClick = {},
            modifier = Modifier
                .width(56.dp)
                .aspectRatio(CardAspectRatio),
        )
    }
}

@Composable
private fun CardBottomBar(
    onOpenSettings: () -> Unit,
    onShuffle: () -> Unit,
    onReset: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SpinColors.Background)
            .padding(horizontal = SpinSpacing.ScreenHorizontal, vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CardToolButton(
            glyph = SpinIconGlyph.Sliders,
            contentDescription = "Tùy chỉnh",
            onClick = onOpenSettings,
        )
        CardPrimaryActionButton(
            text = "NHẤN ĐỂ XÁO TRỘN",
            onClick = onShuffle,
            modifier = Modifier.weight(1f),
        )
        CardToolButton(
            glyph = SpinIconGlyph.Reset,
            contentDescription = "Reset",
            onClick = onReset,
        )
    }
}

@Composable
private fun CardToolButton(
    glyph: SpinIconGlyph,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(width = 52.dp, height = 52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF393347))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        SpinIcon(
            glyph = glyph,
            tint = Color.White,
            modifier = Modifier.size(28.dp),
        )
    }
}

@Composable
private fun CardPrimaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF393347))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun CardSettingRow(
    title: String,
    trailing: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
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
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.width(12.dp))
        trailing()
    }
}

@Composable
private fun CardStepper(
    value: String,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp),
    ) {
        CardStepperButton(text = "-", onClick = onMinus)
        Text(
            text = value,
            modifier = Modifier.width(34.dp),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
        )
        CardStepperButton(text = "+", onClick = onPlus)
    }
}

@Composable
private fun CardStepperButton(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            lineHeight = 22.sp,
        )
    }
}

@Composable
private fun CardThemeLabelCard(
    theme: CardTheme,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(126.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(theme.labelBackground)
            .border(
                width = 2.dp,
                color = Color(0xFF4C5263),
                shape = RoundedCornerShape(12.dp),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 9.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CardThemePreviewColumn(
                face = theme.winner,
                label = "Người thắng",
                labelColor = theme.labelContent,
            )
            CardThemePreviewColumn(
                face = theme.loser,
                label = "Kẻ thua",
                labelColor = theme.labelContent,
            )
        }
    }
}

@Composable
private fun CardThemePreviewColumn(
    face: CardFaceStyle,
    label: String,
    labelColor: Color,
) {
    Column(
        modifier = Modifier.width(74.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StaticCardFace(
            face = face,
            modifier = Modifier
                .width(58.dp)
                .aspectRatio(CardAspectRatio),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = labelColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun CardResultPanel(
    state: CardUiState,
    theme: CardTheme,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 0.dp,
                top = 108.dp,
                end = 0.dp,
                bottom = 42.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(state.cards, key = { it.id }) { card ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    FlipCardView(
                        theme = theme,
                        isWinner = card.isWinner,
                        isFaceUp = true,
                        animationMillis = 240,
                        enabled = false,
                        onClick = {},
                        modifier = Modifier
                            .widthIn(max = 96.dp)
                            .fillMaxWidth()
                            .aspectRatio(CardAspectRatio),
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(24.dp)
                .background(CardResultChrome),
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(24.dp)
                .background(CardResultChrome),
        )
    }
}

@Composable
private fun FlipCardView(
    theme: CardTheme,
    isWinner: Boolean,
    isFaceUp: Boolean,
    animationMillis: Int,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current.density
    val rotation by animateFloatAsState(
        targetValue = if (isFaceUp) 180f else 0f,
        animationSpec = tween(
            durationMillis = animationMillis,
            easing = FastOutSlowInEasing,
        ),
        label = "card-flip",
    )
    val clickableModifier = if (enabled) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .then(clickableModifier),
        contentAlignment = Alignment.Center,
    ) {
        if (rotation <= 90f) {
            CardFront(modifier = Modifier.fillMaxSize())
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f },
            ) {
                StaticCardFace(
                    face = if (isWinner) theme.winner else theme.loser,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun CardFront(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(R.drawable.card_back_burgundy),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .fillMaxSize(),
    )
}

@Composable
private fun StaticCardFace(
    face: CardFaceStyle,
    modifier: Modifier = Modifier,
) {
    val drawableRes = face.drawableRes
    if (drawableRes != null) {
        Image(
            painter = painterResource(drawableRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = modifier.fillMaxSize(),
        )
    } else {
        when (face.fallback) {
            CardFaceFallback.MonochromeLoser -> MonochromeLoserCard(modifier = modifier)
            CardFaceFallback.None -> CardFront(modifier = modifier)
        }
    }
}

@Composable
private fun MonochromeLoserCard(
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(4.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(Color.White)
            .border(1.dp, Color.Black, shape)
            .padding(5.dp),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width * 0.5f, size.height * 0.48f)
            val ink = Color.Black
            repeat(13) { index ->
                val x = size.width * (index / 12f)
                drawLine(
                    color = ink.copy(alpha = 0.22f),
                    start = Offset(x, 0f),
                    end = center,
                    strokeWidth = size.minDimension * 0.012f,
                )
            }
            drawRoundRect(
                color = ink,
                topLeft = Offset(size.width * 0.16f, size.height * 0.18f),
                size = Size(size.width * 0.68f, size.height * 0.58f),
                cornerRadius = CornerRadius(size.minDimension * 0.06f),
                style = Stroke(width = size.minDimension * 0.045f),
            )
            drawCircle(
                color = ink,
                radius = size.minDimension * 0.17f,
                center = center,
                style = Stroke(width = size.minDimension * 0.045f),
            )
            drawCircle(
                color = ink,
                radius = size.minDimension * 0.035f,
                center = center,
            )
            drawCircle(
                color = ink,
                radius = size.minDimension * 0.04f,
                center = Offset(size.width * 0.32f, size.height * 0.5f),
            )
            drawCircle(
                color = ink,
                radius = size.minDimension * 0.04f,
                center = Offset(size.width * 0.68f, size.height * 0.5f),
            )
            drawLine(
                color = ink,
                start = Offset(size.width * 0.34f, size.height * 0.66f),
                end = Offset(size.width * 0.66f, size.height * 0.66f),
                strokeWidth = size.minDimension * 0.035f,
            )
        }
        Text(
            text = "A♠",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(1.dp),
            color = Color.Black,
            fontSize = 8.sp,
            fontWeight = FontWeight.Black,
            lineHeight = 8.sp,
        )
        Text(
            text = "A♠",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .graphicsLayer {
                    rotationZ = 180f
                }
                .padding(1.dp),
            color = Color.Black,
            fontSize = 8.sp,
            fontWeight = FontWeight.Black,
            lineHeight = 8.sp,
        )
    }
}

private fun shareCardResult(
    context: Context,
    text: String,
) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Kết quả lật thẻ")
        putExtra(Intent.EXTRA_TEXT, text)
    }
    try {
        context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"))
    } catch (_: ActivityNotFoundException) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("Kết quả lật thẻ", text))
        Toast.makeText(context, "Đã sao chép kết quả lật thẻ", Toast.LENGTH_SHORT).show()
    }
}

private fun cardAnimationMillis(durationSeconds: Int): Int =
    (durationSeconds * 180).coerceIn(240, 1_200)

private const val CardAspectRatio = 86f / 124f
private val CardResultChrome = Color(0xFF3D3D3C)
