package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.data.model.RandomResult
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WheelHistoryScreen(
    viewModel: WheelViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val historyList by viewModel.history.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = "Lịch sử quay",
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = "Quay lại",
                onNavigationClick = onBack,
                centerTitle = false,
                actions = {
                    SpinIconButton(
                        glyph = SpinIconGlyph.Trash,
                        contentDescription = "Xóa lịch sử",
                        onClick = viewModel::clearHistory,
                        enabled = historyList.isNotEmpty(),
                    )
                },
            )
        },
    ) { innerPadding ->
        if (historyList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                SpinIcon(
                    glyph = SpinIconGlyph.History,
                    tint = SpinColors.IconMuted,
                    modifier = Modifier.size(72.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Chưa có lịch sử quay",
                    style = MaterialTheme.typography.titleMedium,
                    color = SpinColors.TextMuted,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    horizontal = SpinSpacing.ScreenHorizontal,
                    vertical = 12.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(historyList, key = { it.id }) { item ->
                    HistoryItemCard(item = item)
                }
            }
        }
    }
}

@Composable
private fun HistoryItemCard(item: RandomResult) {
    val dateStr = remember(item.createdAt) {
        val sdf = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
        sdf.format(Date(item.createdAt))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SpinRadius.Card))
            .background(Color(0xFF3B3754))
            .padding(horizontal = 16.dp, vertical = 18.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = SpinColors.TextPrimary,
            )
            Text(
                text = dateStr,
                style = MaterialTheme.typography.bodySmall,
                color = SpinColors.TextMuted,
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Result: ${item.value}",
            style = MaterialTheme.typography.titleMedium,
            color = SpinColors.Action,
        )
    }
}
