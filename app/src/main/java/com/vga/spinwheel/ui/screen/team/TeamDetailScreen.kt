package com.vga.spinwheel.ui.screen.team

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun TeamDetailScreen(
    listId: String,
    viewModel: TeamViewModel,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onPreview: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()
    val list = state.currentList

    LaunchedEffect(listId) {
        viewModel.loadList(listId)
    }

    val members = remember(list?.items) {
        TeamRoundRules.memberNames(list?.items.orEmpty())
    }
    val showBoards = state.status != TeamMatchStatus.Idle || state.teams.isNotEmpty()
    val visibleTeams = remember(state.teams, members, state.groupSize) {
        state.teams.ifEmpty {
            TeamRoundRules.createTeams(members, state.groupSize)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = "Đội",
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Quay lại",
                onNavigationClick = {
                    if (state.status == TeamMatchStatus.Idle) {
                        onBack()
                    } else {
                        viewModel.resetMatching()
                    }
                },
            )
        },
        bottomBar = {
            TeamDetailBottomBar(
                primaryText = when (state.status) {
                    TeamMatchStatus.Idle -> "NHẤN ĐỂ GHÉP NỐI"
                    TeamMatchStatus.Matching -> "ĐANG GHÉP..."
                    TeamMatchStatus.ReadyForPreview -> "Next"
                },
                primaryEnabled = state.status != TeamMatchStatus.Matching && members.size >= 2,
                settingsEnabled = state.status != TeamMatchStatus.Matching,
                onSettings = onOpenSettings,
                onPrimary = {
                    when (state.status) {
                        TeamMatchStatus.Idle -> viewModel.startMatching()
                        TeamMatchStatus.ReadyForPreview -> onPreview()
                        TeamMatchStatus.Matching -> Unit
                    }
                },
                onReset = viewModel::resetMatching,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(46.dp))

            TeamNameChip(name = list?.name ?: "Đội")

            Spacer(modifier = Modifier.height(48.dp))

            when {
                list == null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Đang tải...",
                            color = SpinColors.TextMuted,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }

                showBoards -> {
                    TeamBoardStrip(
                        teams = visibleTeams,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        boardWidth = 240.dp,
                        contentPadding = PaddingValues(horizontal = SpinSpacing.ScreenHorizontal),
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(horizontal = SpinSpacing.ScreenHorizontal),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        itemsIndexed(members, key = { index, name -> "$index-$name" }) { index, name ->
                            TeamMemberRow(index = index, name = name)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamNameChip(
    name: String,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SpinColors.Action)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = name,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun TeamMemberRow(
    index: Int,
    name: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF393347))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(12.dp),
            )
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = "${index + 1}. $name",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun TeamDetailBottomBar(
    primaryText: String,
    primaryEnabled: Boolean,
    settingsEnabled: Boolean,
    onSettings: () -> Unit,
    onPrimary: () -> Unit,
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
        TeamToolButton(
            glyph = SpinIconGlyph.Sliders,
            contentDescription = "Tùy chỉnh",
            enabled = settingsEnabled,
            onClick = onSettings,
        )
        TeamPrimaryActionButton(
            text = primaryText,
            enabled = primaryEnabled,
            onClick = onPrimary,
            modifier = Modifier.weight(1f),
        )
        TeamToolButton(
            glyph = SpinIconGlyph.Reset,
            contentDescription = "Reset",
            enabled = settingsEnabled,
            onClick = onReset,
        )
    }
}

@Composable
private fun TeamToolButton(
    glyph: SpinIconGlyph,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(width = 58.dp, height = 58.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .clip(RoundedCornerShape(16.dp))
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
private fun TeamPrimaryActionButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(58.dp)
            .alpha(if (enabled) 1f else 0.65f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF49425F))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = if (text == "Next") 24.sp else 17.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
