package com.vga.spinwheel.ui.screen.coin

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.nav.CoinRoutes
import com.vga.spinwheel.ui.theme.SpinColors

import com.vga.spinwheel.ui.components.SpinSettingRow
import com.vga.spinwheel.ui.components.SpinStepper
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph

@Composable
fun CoinSettingsScreen(
    navController: NavController,
    viewModel: CoinViewModel = hiltViewModel()
) {
    val duration by viewModel.duration.collectAsState()

    Scaffold(
        topBar = {
            SpinTopBar(
                title = "Tùy chỉnh",
                navigationIcon = com.vga.spinwheel.ui.components.SpinIconGlyph.Back,
                navigationDescription = "Back",
                onNavigationClick = { navController.popBackStack() },
            )
        },
        containerColor = SpinColors.Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            SpinSettingRow(
                title = "Thời lượng hoạt hình",
                trailing = {
                    SpinStepper(
                        value = "${duration}s",
                        onMinus = { viewModel.setDuration(duration - 1) },
                        onPlus = { viewModel.setDuration(duration + 1) }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SpinSettingRow(
                title = "Mẫu tiền xu",
                onClick = { navController.navigate(CoinRoutes.LABEL) },
                trailing = {
                    SpinIcon(
                        glyph = SpinIconGlyph.ChevronRight,
                        tint = SpinColors.IconMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        }
    }
}
