package com.vga.spinwheel.ui.screen.coin

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.nav.CoinRoutes
import com.vga.spinwheel.ui.theme.SpinColors
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun CoinHomeScreen(
    navController: NavController,
    viewModel: CoinViewModel = hiltViewModel()
) {
    val duration by viewModel.duration.collectAsState()
    val skin by viewModel.currentSkin.collectAsState()

    var isFlipping by remember { mutableStateOf(false) }
    var rotationY by remember { mutableFloatStateOf(0f) }
    var currentSideIsHeads by remember { mutableStateOf(true) }
    var headScore by remember { mutableIntStateOf(0) }
    var tailScore by remember { mutableIntStateOf(0) }

    // This handles the smooth flipping animation
    val animatedRotation by animateFloatAsState(
        targetValue = rotationY,
        animationSpec = tween(durationMillis = if (isFlipping) duration * 1000 else 0, easing = FastOutSlowInEasing),
        label = "coinFlip"
    )

    // Calculate which side of the coin to show based on the rotation.
    // Every 180 degrees the side changes.
    val isHeadVisible = (animatedRotation / 90f).toInt() % 2 == 0

    LaunchedEffect(isFlipping) {
        if (isFlipping) {
            val isTargetHeads = Random.nextBoolean()
            // Ensure multiple flips to make it look like it's spinning fast.
            // Say we want it to flip about 5 times per second. 
            val flips = duration * 5
            val totalDegrees = (flips * 180f) + if (isTargetHeads != currentSideIsHeads) 180f else 0f
            
            rotationY += totalDegrees
            
            delay(duration * 1000L)
            
            currentSideIsHeads = isTargetHeads
            if (isTargetHeads) {
                headScore++
            } else {
                tailScore++
            }
            
            navController.navigate("${CoinRoutes.RESULT}/$isTargetHeads")
            isFlipping = false
        }
    }

    Scaffold(
        topBar = {
            SpinTopBar(
                title = "Đồng Xu",
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Back",
                onNavigationClick = { if (!isFlipping) navController.popBackStack() },
                actions = {
                    SpinIconButton(
                        glyph = SpinIconGlyph.More, // Adjust if you have a gear icon
                        contentDescription = "Settings",
                        onClick = { if (!isFlipping) navController.navigate(CoinRoutes.SETTINGS) }
                    )
                },
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
            
            // Scoreboard Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = skin.headDrawable),
                        contentDescription = "Heads",
                        modifier = Modifier.size(44.dp)
                    )
                    Text(
                        text = "$headScore",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
                
                Text(
                    text = "-",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$tailScore",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Image(
                        painter = painterResource(id = skin.tailDrawable),
                        contentDescription = "Tails",
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Main Coin Area
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .graphicsLayer {
                        this.rotationY = animatedRotation
                        this.cameraDistance = 12f * density
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isHeadVisible) {
                    Image(
                        painter = painterResource(id = skin.headDrawable),
                        contentDescription = "Coin Head",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = skin.tailDrawable),
                        contentDescription = "Coin Tail",
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                // Rotate the image so that when it's viewed from the back (180 deg), it's right-side up.
                                this.rotationY = 180f 
                            }
                    )
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
                        .clickable { if (!isFlipping) navController.navigate(CoinRoutes.SETTINGS) },
                    contentAlignment = Alignment.Center
                ) {
                    SpinIcon(
                        glyph = SpinIconGlyph.Sliders,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Button(
                    onClick = { if (!isFlipping) isFlipping = true },
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
                        text = "BẮT ĐẦU",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF393347))
                        .clickable {
                            if (!isFlipping) {
                                headScore = 0
                                tailScore = 0
                            }
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
