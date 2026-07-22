package com.vga.spinwheel.ui.screen.team

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun TeamPreviewScreen(
    viewModel: TeamViewModel,
    onHome: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val members = remember(state.currentList?.items) {
        TeamRoundRules.memberNames(state.currentList?.items.orEmpty())
    }
    val teams = remember(state.teams, members, state.groupSize) {
        state.teams.ifEmpty {
            TeamRoundRules.createTeams(members, state.groupSize)
        }
    }

    fun shareResult() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Kết quả chia đội")
            putExtra(Intent.EXTRA_TEXT, viewModel.shareText())
        }
        context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"))
    }

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
            Spacer(modifier = Modifier.height(36.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 420.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(22.dp))
                    .border(
                        width = 1.5.dp,
                        color = Color.White.copy(alpha = 0.62f),
                        shape = RoundedCornerShape(22.dp),
                    )
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (teams.isEmpty()) {
                    Text(
                        text = "Chưa có kết quả",
                        color = SpinColors.TextMuted,
                        style = MaterialTheme.typography.titleMedium,
                    )
                } else {
                    TeamBoardStrip(
                        teams = teams,
                        modifier = Modifier.fillMaxSize(),
                        boardWidth = 234.dp,
                        contentPadding = PaddingValues(horizontal = 34.dp),
                        showEditIcon = true,
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            TeamShareButton(
                onClick = { shareResult() },
            )

            Spacer(modifier = Modifier.height(70.dp))

            TeamRetryButton(
                text = "Thử lại",
                onClick = onRetry,
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun TeamShareButton(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(210.dp)
            .height(62.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF32A9F7))
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
            )
        }
    }
}

@Composable
private fun TeamRetryButton(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(SpinRadius.Button))
            .background(Color(0xFFE3422E))
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
