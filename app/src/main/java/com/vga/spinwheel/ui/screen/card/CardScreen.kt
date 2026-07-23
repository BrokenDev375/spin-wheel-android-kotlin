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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinResultCard
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
                .padding(top = 58.dp),
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
                start = SpinSpacing.ScreenHorizontal,
                top = 30.dp,
                end = SpinSpacing.ScreenHorizontal,
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

            if (!state.isShuffled) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE29C32).copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Nhấn 'NHẤN ĐỂ XÁO TRỘN' để bắt đầu trò chơi",
                        color = Color(0xFFFFD88A),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            WinnerCardPreview(
                theme = theme,
                animationMillis = cardAnimationMillis(state.settings.durationSeconds),
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
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
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 4.dp),
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
                            .widthIn(max = 84.dp)
                            .fillMaxWidth()
                            .aspectRatio(CardAspectRatio),
                    )
                }
            }
        }
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
        centerTitle = true,
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
            modifier = Modifier.size(width = 72.dp, height = 104.dp),
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
            .padding(horizontal = SpinSpacing.ScreenHorizontal, vertical = 28.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
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
            .size(width = 58.dp, height = 58.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF393347))
            .clickable(onClick = onClick),
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
private fun CardPrimaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(58.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF393347))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            maxLines = 2,
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
            fontSize = 19.sp,
            lineHeight = 23.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 2,
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
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        CardStepperButton(text = "-", onClick = onMinus)
        Text(
            text = value,
            modifier = Modifier.width(42.dp),
            color = Color.White,
            fontSize = 22.sp,
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
private fun CardThemeLabelCard(
    theme: CardTheme,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(156.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(theme.labelBackground)
            .border(
                width = if (selected) 3.dp else 2.dp,
                color = if (selected) SpinColors.Action else Color.White,
                shape = RoundedCornerShape(12.dp),
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StaticCardFace(
                    face = theme.winner,
                    modifier = Modifier.size(width = 44.dp, height = 64.dp),
                )
                StaticCardFace(
                    face = theme.loser,
                    modifier = Modifier.size(width = 44.dp, height = 64.dp),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Người thắng",
                    modifier = Modifier.weight(1f),
                    color = theme.labelContent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "Kẻ thua",
                    modifier = Modifier.weight(1f),
                    color = theme.labelContent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF28E982)),
                contentAlignment = Alignment.Center,
            ) {
                SpinIcon(
                    glyph = SpinIconGlyph.Check,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
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
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(CardCreamLight, CardCream, CardCreamDark),
                ),
            )
            .border(2.dp, Color(0xFFFFD88A), shape)
            .padding(7.dp),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension * 0.1f
            drawRoundRect(
                color = Color(0xFF8C2626),
                size = size,
                cornerRadius = CornerRadius(radius, radius),
            )
            drawRoundRect(
                color = Color(0xFFFFE8C2),
                topLeft = Offset(size.width * 0.12f, size.height * 0.1f),
                size = Size(size.width * 0.76f, size.height * 0.8f),
                cornerRadius = CornerRadius(radius * 0.7f, radius * 0.7f),
                style = Stroke(width = size.minDimension * 0.035f),
            )
            drawCircle(
                color = Color(0xFFFFE8C2),
                radius = size.minDimension * 0.16f,
                center = Offset(size.width * 0.5f, size.height * 0.5f),
                style = Stroke(width = size.minDimension * 0.04f),
            )
            drawCircle(
                color = Color(0xFFFFE8C2),
                radius = size.minDimension * 0.035f,
                center = Offset(size.width * 0.5f, size.height * 0.5f),
            )
            repeat(4) { row ->
                repeat(2) { column ->
                    drawCircle(
                        color = Color(0xFFFFE8C2).copy(alpha = 0.7f),
                        radius = size.minDimension * 0.025f,
                        center = Offset(
                            x = size.width * (0.28f + column * 0.44f),
                            y = size.height * (0.22f + row * 0.18f),
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun StaticCardFace(
    face: CardFaceStyle,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(face.background)
            .border(2.dp, face.border, shape)
            .padding(7.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = face.mark,
                color = face.content,
                fontSize = 29.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = face.text.uppercase(),
                color = face.content,
                fontSize = 9.sp,
                lineHeight = 10.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
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

private val CardCreamLight = Color(0xFFFFF2C5)
private val CardCream = Color(0xFFF8DDA0)
private val CardCreamDark = Color(0xFFF2CC76)
