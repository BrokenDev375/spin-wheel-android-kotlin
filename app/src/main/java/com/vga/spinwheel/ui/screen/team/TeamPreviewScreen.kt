package com.vga.spinwheel.ui.screen.team

import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinResultScreen
import com.vga.spinwheel.ui.theme.SpinColors

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
    val resultTitle = stringResource(R.string.results)
    val shareTitle = stringResource(R.string.sharereust)
    val fallbackTeamTitle = stringResource(R.string.homograft)
    val listTitle = state.currentList?.name ?: fallbackTeamTitle

    fun shareResult() {
        val teamText = teams.joinToString(separator = "\n\n") { team ->
            context.getString(R.string.team_group_title, team.index) +
                "\n" +
                team.members.joinToString(separator = "\n") { "- $it" }
        }
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, resultTitle)
            putExtra(Intent.EXTRA_TEXT, "$listTitle\n\n$teamText")
        }
        context.startActivity(Intent.createChooser(shareIntent, shareTitle))
    }

    SpinResultScreen(
        onHome = onHome,
        onShare = { shareResult() },
        onRetry = onRetry,
        modifier = modifier,
        cardHeight = 453.dp,
        cardContentPadding = 0.dp,
    ) {
        if (teams.isEmpty()) {
            Text(
                text = stringResource(R.string.noResults),
                color = SpinColors.TextMuted,
                style = MaterialTheme.typography.titleMedium,
            )
        } else {
            TeamBoardStrip(
                teams = teams,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 22.dp, bottom = 19.dp),
                boardWidth = 186.dp,
                contentPadding = PaddingValues(horizontal = 17.dp),
                showEditIcon = true,
                headerHeight = 30.dp,
                titleFontSize = 18.sp,
                memberFontSize = 18.sp,
            )
        }
    }
}
