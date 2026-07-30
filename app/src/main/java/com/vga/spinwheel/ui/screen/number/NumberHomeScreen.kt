package com.vga.spinwheel.ui.screen.number

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.audio.rememberGameSoundPlayer
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinScreen
import com.vga.spinwheel.ui.components.clickableWithSound
import com.vga.spinwheel.ui.components.rememberClickWithSound
import com.vga.spinwheel.ui.nav.Screen
import com.vga.spinwheel.ui.theme.SpinColors
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NumberHomeScreen(
    navController: NavController,
    viewModel: NumberViewModel = hiltViewModel(),
    onBack: () -> Unit = { navController.popBackStack() }
) {
    val duration by viewModel.duration.collectAsState()
    val min by viewModel.min.collectAsState()
    val max by viewModel.max.collectAsState()
    val history by viewModel.history.collectAsState()
    val gameSoundPlayer = rememberGameSoundPlayer()

    var isSpinning by remember { mutableStateOf(false) }
    var generatedNumbers by remember { mutableStateOf<List<Int>>(emptyList()) }
    var showBalls by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "shake")
    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(40, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shakeOffset"
    )

    LaunchedEffect(isSpinning) {
        if (isSpinning) {
            gameSoundPlayer.startNumberRoll()
            generatedNumbers = viewModel.generateNumbers()
            
            // Shake machine for duration
            val durationMs = duration * 1000L
            delay(durationMs)
            gameSoundPlayer.stopNumberRoll()
            
            showBalls = true
            // Đợi hiệu ứng rơi từng quả hoàn tất, bóng càng nhiều đợi càng lâu (chậm lại)
            delay(1800L + (generatedNumbers.size * 600L))
            
            viewModel.saveResultToHistory(generatedNumbers.joinToString(", "))
            navController.navigate(Screen.NumberResult.route)
            isSpinning = false
            showBalls = false
        } else {
            gameSoundPlayer.stopNumberRoll()
        }
    }

    DisposableEffect(gameSoundPlayer) {
        onDispose { gameSoundPlayer.stopNumberRoll() }
    }

    SpinScreen(
        title = stringResource(R.string.randerNum),
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = stringResource(R.string.content_description_back),
        onNavigationClick = { if (!isSpinning) onBack() },
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        confirmExitOnBack = true,
        actions = {
            SpinIconButton(
                glyph = SpinIconGlyph.History,
                contentDescription = stringResource(R.string.history),
                onClick = { if (!isSpinning) navController.navigate(Screen.NumberHistory.route) }
            )
        }
    ) { contentModifier ->
        Column(
            modifier = contentModifier.padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(horizontal = 30.dp, vertical = 9.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "$min - $max",
                    color = Color.Black,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column(
                    modifier = Modifier.padding(
                        top = if (history.isEmpty()) 98.dp else 46.dp,
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    NumberMachine(
                        modifier = Modifier
                            .width(210.dp)
                            .graphicsLayer {
                                if (isSpinning && !showBalls) {
                                    translationY = shakeOffset
                                }
                            },
                        isSpinning = isSpinning && !showBalls,
                        spreadBalls = showBalls || history.isNotEmpty(),
                        isDropped = isSpinning || showBalls,
                        minNumber = min,
                        maxNumber = max,
                        droppedNumbers = if (showBalls) generatedNumbers else emptyList()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        generatedNumbers.forEachIndexed { index, num ->
                            AnimatedVisibility(
                                visible = showBalls,
                                enter = slideInVertically(
                                    initialOffsetY = { it + 50 }, // Bắt đầu từ DƯỚI (it + 50) để trồi LÊN
                                    animationSpec = tween(durationMillis = 600, delayMillis = 150 + index * 500)
                                ) + fadeIn(animationSpec = tween(durationMillis = 600, delayMillis = 150 + index * 500))
                            ) {
                                NumberBall(
                                    number = num.toString(),
                                    size = 56.dp
                                )
                            }
                        }
                    }
                }
            }

            if (history.isNotEmpty()) {
                NumberRecentResults(
                    results = history.take(4).map { it.value },
                    modifier = Modifier.padding(bottom = 30.dp),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 60.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF393347))
                        .clickableWithSound(enabled = !isSpinning) {
                            navController.navigate(Screen.NumberSettings.route)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    SpinIcon(
                        glyph = SpinIconGlyph.Sliders,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Button(
                    onClick = rememberClickWithSound { isSpinning = true },
                    enabled = !isSpinning,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF393347),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF393347).copy(alpha = 0.62f),
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.playtapto).uppercase(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF393347))
                        .clickableWithSound(enabled = !isSpinning) {
                            viewModel.clearHistory()
                            viewModel.clearLastResult()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    SpinIcon(
                        glyph = SpinIconGlyph.Reset,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun NumberRecentResults(
    results: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SpinIcon(
                glyph = SpinIconGlyph.History,
                tint = Color(0xFFE64C3B),
                modifier = Modifier.size(22.dp),
            )
            Text(
                text = stringResource(R.string.recen),
                color = SpinColors.Action,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(end = 18.dp)
        ) {
            items(results.size) { index ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF393347))
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(10.dp),
                        )
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = results[index],
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                    )
                }
            }
        }
    }
}
