package com.vga.spinwheel.ui.screen.coin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun CoinLabelScreen(
    navController: NavController,
    viewModel: CoinViewModel = hiltViewModel()
) {
    val tempSkinIndex by viewModel.tempSkinIndex.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initTempSkinIndex()
    }

    Scaffold(
        topBar = {
            SpinTopBar(
                title = stringResource(R.string.coinsample),
                centerTitle = false,
                titleStartPadding = 39.dp,
                navigationIcon = com.vga.spinwheel.ui.components.SpinIconGlyph.Back,
                navigationDescription = stringResource(R.string.content_description_back),
                onNavigationClick = { navController.popBackStack() },
                actions = {
                    Text(
                        text = stringResource(R.string.save),
                        color = SpinColors.Action,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable {
                                viewModel.saveSkinIndex()
                                navController.popBackStack()
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            )
        },
        containerColor = SpinColors.Background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(CoinSkins.AllSkins) { index, skin ->
                val isSelected = index == tempSkinIndex
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(156.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF393347))
                        .border(
                            width = if (isSelected) 1.5.dp else 0.dp,
                            color = if (isSelected) SpinColors.Action else Color.Transparent,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { viewModel.setTempSkinIndex(index) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = skin.headDrawable),
                            contentDescription = stringResource(R.string.heads),
                            modifier = Modifier.size(136.dp)
                        )
                        
                        Image(
                            painter = painterResource(id = skin.tailDrawable),
                            contentDescription = stringResource(R.string.tails),
                            modifier = Modifier.size(136.dp)
                        )
                    }

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp)
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(SpinColors.Success),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = stringResource(R.string.choose),
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
