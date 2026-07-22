package com.vga.spinwheel.ui.screen.team

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius

@Composable
fun TeamBoardStrip(
    teams: List<TeamGroup>,
    modifier: Modifier = Modifier,
    boardWidth: Dp = 240.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 18.dp),
    showEditIcon: Boolean = false,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        items(teams, key = { it.index }) { team ->
            TeamBoard(
                team = team,
                modifier = Modifier.width(boardWidth),
                showEditIcon = showEditIcon,
            )
        }
    }
}

@Composable
private fun TeamBoard(
    team: TeamGroup,
    showEditIcon: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(SpinRadius.Control))
            .background(Color(0xFF393347))
            .border(
                width = 1.dp,
                color = SpinColors.Action.copy(alpha = 0.50f),
                shape = RoundedCornerShape(SpinRadius.Control),
            ),
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(SpinColors.Action)
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = team.title,
                color = Color.White,
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (showEditIcon) {
                Spacer(modifier = Modifier.width(8.dp))
                SpinIcon(
                    glyph = SpinIconGlyph.Sliders,
                    tint = Color.White,
                    modifier = Modifier.width(22.dp).height(22.dp),
                )
            }
        }

        team.members.forEachIndexed { index, member ->
            Text(
                text = member,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 13.dp),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (index != team.members.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.White.copy(alpha = 0.07f),
                    thickness = 1.dp,
                )
            }
        }
    }
}
