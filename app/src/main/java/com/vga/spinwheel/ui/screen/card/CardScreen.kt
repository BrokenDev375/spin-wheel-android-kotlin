package com.vga.spinwheel.ui.screen.card

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.audio.rememberGameSoundPlayer
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinSettingRow
import com.vga.spinwheel.ui.components.SpinSettingStepper
import com.vga.spinwheel.ui.components.SpinResultScreen
import com.vga.spinwheel.ui.components.SpinRetryButton
import com.vga.spinwheel.ui.components.SpinShareButton
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.components.clickableWithSound
import com.vga.spinwheel.ui.components.rememberClickWithSound
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun CardScreen(
    viewModel: CardViewModel,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()
    val gameSoundPlayer = rememberGameSoundPlayer()
    val context = LocalContext.current
    val resultTitle = stringResource(R.string.results)
    val cardTitle = stringResource(R.string.card)
    val winningCardTitle = stringResource(R.string.cardwin)
    val shareTitle = stringResource(R.string.sharereust)
    val shareSuccess = stringResource(R.string.share_success)

    LaunchedEffect(state.stage) {
        if (state.stage == CardStage.Result) {
            gameSoundPlayer.stopCardShuffle()
        }
    }

    LaunchedEffect(state.isShuffleAnimating) {
        if (state.isShuffleAnimating) {
            gameSoundPlayer.startCardShuffle()
        } else {
            gameSoundPlayer.stopCardShuffle()
        }
    }

    DisposableEffect(gameSoundPlayer) {
        onDispose { gameSoundPlayer.stopCardShuffle() }
    }

    if (state.stage == CardStage.Result) {
        CardResultScreen(
            state = state,
            onShare = {
                val winningPositions = state.cards
                    .withIndex()
                    .filter { it.value.isWinner }
                    .joinToString(", ") { (index, _) -> (index + 1).toString() }
                shareCardResult(
                    context = context,
                    text = "$resultTitle $cardTitle: ${state.settings.winners}/${state.settings.totalCards}. $winningCardTitle: $winningPositions.",
                    subject = "$resultTitle $cardTitle",
                    chooserTitle = shareTitle,
                    fallbackToast = shareSuccess,
                )
            },
            onRetry = viewModel::retryFromResult,
            onHome = onHome,
            modifier = modifier,
        )
    } else {
        CardHomeScreen(
            state = state,
            onBack = onBack,
            onOpenSettings = onOpenSettings,
            onShuffle = {
                viewModel.shuffleCards()
            },
            onReset = {
                gameSoundPlayer.stopCardShuffle()
                viewModel.resetCards()
            },
            onFlipCard = { cardId ->
                gameSoundPlayer.stopCardShuffle()
                gameSoundPlayer.playCardFlip()
                viewModel.flipCard(cardId)
            },
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
                .padding(top = 22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            SpinSettingRow(
                title = stringResource(R.string.duration),
                trailing = {
                    SpinSettingStepper(
                        value = "${state.settings.durationSeconds}s",
                        onMinus = { viewModel.updateDuration(state.settings.durationSeconds - 1) },
                        onPlus = { viewModel.updateDuration(state.settings.durationSeconds + 1) },
                    )
                },
            )

            SpinSettingRow(
                title = stringResource(R.string.numbercard),
                trailing = {
                    SpinSettingStepper(
                        value = state.settings.totalCards.toString(),
                        onMinus = { viewModel.updateTotalCards(state.settings.totalCards - 1) },
                        onPlus = { viewModel.updateTotalCards(state.settings.totalCards + 1) },
                    )
                },
            )

            SpinSettingRow(
                title = stringResource(R.string.numberwin),
                trailing = {
                    SpinSettingStepper(
                        value = state.settings.winners.toString(),
                        onMinus = { viewModel.updateWinners(state.settings.winners - 1) },
                        onPlus = { viewModel.updateWinners(state.settings.winners + 1) },
                    )
                },
            )

            val currentTheme = CardThemes.get(state.settings.themeIndex)
            SpinSettingRow(
                title = stringResource(R.string.Temlatecard),
                onClick = {
                    viewModel.beginThemeSelection()
                    onOpenLabels()
                },
                trailing = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currentTheme.name,
                            color = Color(0xFFFFA726),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        SpinIcon(
                            glyph = SpinIconGlyph.ChevronRight,
                            tint = SpinColors.IconMuted,
                            modifier = Modifier.size(30.dp),
                        )
                    }
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
                title = stringResource(R.string.Temlatecard),
                onBack = onBack,
                actions = {
                    TextButton(
                        onClick = rememberClickWithSound {
                            viewModel.saveSelectedTheme()
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
    val density = LocalDensity.current.density
    val shuffleDurationMillis = cardShuffleAnimationMillis(state.settings.durationSeconds)
    val shuffleProgress = remember { Animatable(1f) }

    LaunchedEffect(state.isShuffleAnimating, state.runId, state.settings.durationSeconds) {
        if (state.isShuffleAnimating) {
            shuffleProgress.snapTo(0f)
            shuffleProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = shuffleDurationMillis,
                    easing = LinearEasing,
                ),
            )
        } else {
            shuffleProgress.snapTo(1f)
        }
    }

    val activeShuffleProgress = if (state.isShuffleAnimating) shuffleProgress.value else 1f

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            CardHeader(
                title = stringResource(R.string.card),
                onBack = onBack,
            )
        },
        bottomBar = {
            CardBottomBar(
                onOpenSettings = onOpenSettings,
                onShuffle = onShuffle,
                onReset = onReset,
                enabled = !state.isShuffleAnimating,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 14.dp),
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
                itemsIndexed(
                    items = state.cards,
                    key = { _, card -> card.id },
                ) { index, card ->
                    val shuffleMotion = cardShuffleMotion(
                        index = index,
                        totalCards = state.cards.size,
                        progress = activeShuffleProgress,
                        shuffleDurationMillis = shuffleDurationMillis,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .zIndex(shuffleMotion.zIndex),
                        contentAlignment = Alignment.Center,
                    ) {
                        FlipCardView(
                            theme = theme,
                            isWinner = card.isWinner,
                            isFaceUp = card.isFlipped,
                            animationMillis = cardAnimationMillis(state.settings.durationSeconds),
                            enabled = state.isShuffled && !state.isShuffleAnimating && !card.isFlipped,
                            eliminated = card.isFlipped && !card.isWinner,
                            eliminationTilt = if (index % 2 == 0) -5f else 5f,
                            onClick = { onFlipCard(card.id) },
                            modifier = Modifier
                                .widthIn(max = 94.dp)
                                .fillMaxWidth()
                                .aspectRatio(CardAspectRatio)
                                .graphicsLayer {
                                    translationX = shuffleMotion.translationXDp * density
                                    translationY = shuffleMotion.translationYDp * density
                                    rotationZ = shuffleMotion.rotationZ
                                    scaleX = shuffleMotion.scale
                                    scaleY = shuffleMotion.scale
                                    alpha = shuffleMotion.alpha
                                },
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
        navigationDescription = stringResource(R.string.content_description_back),
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
                text = stringResource(R.string.cardwin),
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
    enabled: Boolean,
) {
    val customizeLabel = stringResource(R.string.customsize)
    val shuffleLabel = stringResource(R.string.TaptoShuffle).uppercase()
    val restartLabel = stringResource(R.string.restart)

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
            contentDescription = customizeLabel,
            onClick = onOpenSettings,
            enabled = enabled,
        )
        CardPrimaryActionButton(
            text = shuffleLabel,
            onClick = onShuffle,
            enabled = enabled,
            modifier = Modifier.weight(1f),
        )
        CardToolButton(
            glyph = SpinIconGlyph.Reset,
            contentDescription = restartLabel,
            onClick = onReset,
            enabled = enabled,
        )
    }
}

@Composable
private fun CardToolButton(
    glyph: SpinIconGlyph,
    contentDescription: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Box(
        modifier = Modifier
            .size(width = 52.dp, height = 36.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF393347))
            .clickableWithSound(onClick = onClick),
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
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF393347))
            .clickableWithSound(onClick = onClick)
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
            .height(36.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF393347))
            .then(if (onClick == null) Modifier else Modifier.clickableWithSound(onClick = onClick))
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
            modifier = Modifier.width(46.dp),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip,
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
            .clickableWithSound(onClick = onClick),
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
    val borderColor = if (selected) Color(0xFFFFA726) else Color(0xFF4C5263)
    val borderWidth = if (selected) 3.5.dp else 1.5.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(theme.labelBackground)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(14.dp),
            )
            .clickableWithSound(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 9.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CardThemePreviewColumn(
                face = theme.winner,
                label = stringResource(R.string.Winner),
                labelColor = theme.labelContent,
            )
            CardThemePreviewColumn(
                face = theme.loser,
                label = stringResource(R.string.Lose),
                labelColor = theme.labelContent,
            )
        }

        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(24.dp)
                    .background(Color(0xFFFFA726), CircleShape)
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp),
                )
            }
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
    eliminated: Boolean = false,
    eliminationTilt: Float = 0f,
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
    val eliminationProgress by animateFloatAsState(
        targetValue = if (eliminated) 1f else 0f,
        animationSpec = tween(
            durationMillis = (animationMillis * 0.75f).toInt().coerceAtLeast(220),
            delayMillis = (animationMillis * 0.35f).toInt().coerceAtLeast(90),
            easing = FastOutSlowInEasing,
        ),
        label = "card-elimination",
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
                alpha = 1f - 0.52f * eliminationProgress
                scaleX = 1f - 0.09f * eliminationProgress
                scaleY = 1f - 0.09f * eliminationProgress
                translationY = 14f * density * eliminationProgress
                rotationZ = eliminationTilt * eliminationProgress
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

private data class CardShuffleMotion(
    val translationXDp: Float,
    val translationYDp: Float,
    val rotationZ: Float,
    val scale: Float,
    val alpha: Float,
    val zIndex: Float,
)

private fun cardShuffleMotion(
    index: Int,
    totalCards: Int,
    progress: Float,
    shuffleDurationMillis: Int,
): CardShuffleMotion {
    val p = progress.coerceIn(0f, 1f)
    if (p >= 0.999f || totalCards <= 0) {
        return CardShuffleMotion(
            translationXDp = 0f,
            translationYDp = 0f,
            rotationZ = 0f,
            scale = 1f,
            alpha = 1f,
            zIndex = index.toFloat(),
        )
    }

    val durationMillis = shuffleDurationMillis.coerceAtLeast(1).toFloat()
    val timeMillis = p * durationMillis
    val gatherEnd = CardGatherMillis / durationMillis
    val remainingMillis = (durationMillis - CardGatherMillis).coerceAtLeast(1f)
    val dealWindowMillis = (remainingMillis * CardDealDurationRatio)
        .coerceIn(CardDealMinMillis, CardDealMaxMillis)
        .coerceAtMost(remainingMillis * 0.45f)
    val dealWindow = dealWindowMillis / durationMillis
    val dealStartBase = 1f - dealWindow
    val shuffleStart = gatherEnd
    val shuffleEnd = dealStartBase
    val shuffleSpan = (shuffleEnd - shuffleStart).coerceAtLeast(0.01f)
    val horizontalStart = shuffleStart
    val horizontalSplitEnd = shuffleStart + shuffleSpan * 0.48f
    val verticalStart = shuffleStart + shuffleSpan * 0.52f
    val verticalEnd = shuffleEnd
    val verticalEase = (CardVerticalShuffleEaseMillis / durationMillis)
        .coerceIn(0.018f, shuffleSpan * 0.16f)

    val column = index % 3
    val row = index / 3
    val deckX = (1f - column) * CardShuffleColumnPitchDp
    val deckY = (0.10f - row) * CardShuffleRowPitchDp
    val stackDepth = (index - totalCards / 2f) / totalCards.coerceAtLeast(1)
    val gather = smoothStep(0f, gatherEnd, p)

    val halfSize = (totalCards + 1) / 2
    val isLeftPacket = index < halfSize
    val side = if (isLeftPacket) -1f else 1f
    val halfIndex = if (isLeftPacket) index else index - halfSize
    val halfCount = if (isLeftPacket) halfSize else totalCards - halfSize
    val halfOrder = halfIndex / (halfCount - 1).coerceAtLeast(1).toFloat()

    val horizontalSplit = smoothStep(horizontalStart, horizontalStart + shuffleSpan * 0.16f, p) *
        (1f - smoothStep(horizontalStart + shuffleSpan * 0.34f, horizontalSplitEnd, p))
    val horizontalShuffle = smoothStep(horizontalStart + shuffleSpan * 0.10f, horizontalStart + shuffleSpan * 0.20f, p) *
        (1f - smoothStep(horizontalStart + shuffleSpan * 0.30f, horizontalSplitEnd, p))
    val horizontalElapsed = (timeMillis - horizontalStart * durationMillis).coerceAtLeast(0f)
    val horizontalWave = sin(
        horizontalElapsed / CardHorizontalShuffleCycleMillis * PI.toFloat() * 2f +
            halfOrder * PI.toFloat()
    )
    val packetX = side * (48f + halfOrder * 12f) * horizontalSplit
    val packetY = (-8f + halfOrder * 5f) * horizontalSplit
    val horizontalX = side * horizontalWave * 12f * horizontalShuffle
    val horizontalRotation = side * (12f - halfOrder * 4f) * horizontalSplit +
        side * horizontalWave * 5f * horizontalShuffle

    val verticalShuffle = smoothStep(verticalStart, verticalStart + verticalEase, p) *
        (1f - smoothStep(verticalEnd - verticalEase, verticalEnd, p))
    val alternateDirection = if (index % 2 == 0) -1f else 1f
    val verticalElapsed = (timeMillis - verticalStart * durationMillis).coerceAtLeast(0f)
    val verticalCycle = sin(
        verticalElapsed / CardVerticalShuffleCycleMillis * PI.toFloat() * 2f +
            index * 0.28f
    )
    val verticalOffset = alternateDirection * verticalCycle * (34f + (index % 3) * 4f) * verticalShuffle
    val verticalRotation = alternateDirection * verticalCycle * 4.5f * verticalShuffle
    val deckSettle = smoothStep(verticalStart, verticalEnd, p)

    val dealStep = dealWindow / totalCards.coerceAtLeast(1)
    val dealStart = dealStartBase + index * dealStep
    val dealDuration = dealStep * 0.9f
    val deal = smoothStep(dealStart, dealStart + dealDuration, p)
    val deckWeight = gather * (1f - deal)
    val dealArc = -42f * sin(deal * PI.toFloat())
    val dealSway = (if (index % 2 == 0) -1f else 1f) * 7f * sin(deal * PI.toFloat())
    val squaredDeckOffsetX = stackDepth * 2f * deckSettle * (1f - deal)
    val squaredDeckOffsetY = -stackDepth * 2.4f * deckSettle * (1f - deal)

    return CardShuffleMotion(
        translationXDp = deckX * deckWeight +
            packetX * (1f - deal) +
            horizontalX * (1f - deal) +
            squaredDeckOffsetX +
            dealSway,
        translationYDp = deckY * deckWeight +
            packetY * (1f - deal) +
            verticalOffset * (1f - deal) +
            squaredDeckOffsetY +
            dealArc,
        rotationZ = (-8f * stackDepth * deckWeight) +
            horizontalRotation * (1f - deal) +
            verticalRotation * (1f - deal),
        scale = 1f - 0.035f * deckWeight + 0.025f * verticalShuffle + 0.035f * sin(deal * PI.toFloat()),
        alpha = 1f,
        zIndex = if (deal < 1f) (totalCards - index).toFloat() else index.toFloat(),
    )
}

private fun smoothStep(
    start: Float,
    end: Float,
    value: Float,
): Float {
    if (end <= start) return if (value >= end) 1f else 0f
    val t = ((value - start) / (end - start)).coerceIn(0f, 1f)
    return t * t * (3f - 2f * t)
}

private fun cardAnimationMillis(durationSeconds: Int): Int =
    (durationSeconds * 180).coerceIn(240, 1_200)

private fun cardShuffleAnimationMillis(durationSeconds: Int): Int =
    durationSeconds * 1_000

private const val CardAspectRatio = 86f / 124f
private const val CardShuffleColumnPitchDp = 108f
private const val CardShuffleRowPitchDp = 150f
private const val CardGatherMillis = 260f
private const val CardDealDurationRatio = 0.28f
private const val CardDealMinMillis = 520f
private const val CardDealMaxMillis = 2_600f
private const val CardHorizontalShuffleCycleMillis = 190f
private const val CardVerticalShuffleCycleMillis = 170f
private const val CardVerticalShuffleEaseMillis = 220f
private val CardResultChrome = Color(0xFF3D3D3C)
