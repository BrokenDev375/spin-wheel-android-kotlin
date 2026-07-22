package com.vga.spinwheel.ui.screen.number

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.nav.NumberRoutes
import com.vga.spinwheel.ui.theme.SpinColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun NumberHomeScreen(
    navController: NavController,
    viewModel: NumberViewModel = hiltViewModel()
) {
    val duration by viewModel.duration.collectAsState()
    val min by viewModel.min.collectAsState()
    val max by viewModel.max.collectAsState()
    val count by viewModel.count.collectAsState()

    var isSpinning by remember { mutableStateOf(false) }
    var generatedNumbers by remember { mutableStateOf<List<Int>>(emptyList()) }
    var showBalls by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "shake")
    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(50, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shakeOffset"
    )

    LaunchedEffect(isSpinning) {
        if (isSpinning) {
            generatedNumbers = viewModel.generateNumbers()
            
            // Shake machine for duration
            val durationMs = duration * 1000L
            delay(durationMs)
            
            // Show balls dropping
            showBalls = true
            delay(800L) // Wait for balls to drop
            
            viewModel.saveResultToHistory(generatedNumbers.joinToString(", "))
            navController.navigate(NumberRoutes.RESULT)
            isSpinning = false
            showBalls = false
        }
    }

    Scaffold(
        topBar = {
            SpinTopBar(
                title = "Số Ngẫu Nhiên",
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Back",
                onNavigationClick = { if (!isSpinning) navController.popBackStack() },
                actions = {
                    SpinIconButton(
                        glyph = SpinIconGlyph.History,
                        contentDescription = "History",
                        onClick = { if (!isSpinning) navController.navigate(NumberRoutes.HISTORY) }
                    )
                }
            )
        },
        containerColor = SpinColors.Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Range Display Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Min: $min",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = "-",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Max: $max",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Machine Animation Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                // The Machine
                Box(
                    modifier = Modifier
                        .size(180.dp, 220.dp)
                        .offset {
                            IntOffset(
                                x = if (isSpinning && !showBalls) shakeOffset.roundToInt() else 0,
                                y = if (isSpinning && !showBalls) (shakeOffset / 2).roundToInt() else 0
                            )
                        }
                        .clip(RoundedCornerShape(40.dp))
                        .background(Color(0xFF39a9f2)),
                    contentAlignment = Alignment.TopCenter
                ) {
                    // Glass dome
                    Box(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF8be0ff).copy(alpha = 0.6f))
                    )
                    
                    // Dispenser hole
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 20.dp)
                            .size(40.dp, 20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.DarkGray)
                    )
                }

                // Dropping Balls
                if (showBalls) {
                    // Limit number of visual balls to avoid crowding if count is huge
                    val displayBalls = generatedNumbers.take(10)
                    displayBalls.forEachIndexed { index, number ->
                        var ballY by remember { mutableStateOf(0f) }
                        var ballX by remember { mutableStateOf(0f) }
                        
                        LaunchedEffect(Unit) {
                            val targetX = Random.nextInt(-100, 100).toFloat()
                            val targetY = Random.nextInt(150, 250).toFloat()
                            launch {
                                Animatable(0f).animateTo(targetX, tween(600, easing = FastOutSlowInEasing)) { ballX = value }
                            }
                            launch {
                                Animatable(0f).animateTo(targetY, tween(600, easing = FastOutSlowInEasing)) { ballY = value }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .offset { IntOffset(ballX.roundToInt(), ballY.roundToInt()) }
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFec9213)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = number.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))

            // Bottom Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF393347))
                        .clickable { if (!isSpinning) navController.navigate(NumberRoutes.SETTINGS) },
                    contentAlignment = Alignment.Center
                ) {
                    SpinIcon(
                        glyph = SpinIconGlyph.Sliders,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Button(
                    onClick = { if (!isSpinning) isSpinning = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(58.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF393347),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "NHẤN ĐỂ NGẪU NHIÊN",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF393347))
                        .clickable { if (!isSpinning) viewModel.clearHistory() },
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
