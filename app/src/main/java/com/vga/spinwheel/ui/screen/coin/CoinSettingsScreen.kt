package com.vga.spinwheel.ui.screen.coin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinSettingRow
import com.vga.spinwheel.ui.components.SpinSettingStepper
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.nav.Screen
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun CoinSettingsScreen(
    navController: NavController,
    viewModel: CoinViewModel = hiltViewModel()
) {
    val duration by viewModel.duration.collectAsState()

    Scaffold(
        topBar = {
            SpinTopBar(
                title = stringResource(R.string.customsize),
                centerTitle = false,
                titleStartPadding = 39.dp,
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = stringResource(R.string.content_description_back),
                onNavigationClick = { navController.popBackStack() },
            )
        },
        containerColor = SpinColors.Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            SpinSettingRow(
                title = stringResource(R.string.duration),
                trailing = {
                    SpinSettingStepper(
                        value = "${duration}s",
                        onMinus = { viewModel.setDuration(duration - 1) },
                        onPlus = { viewModel.setDuration(duration + 1) },
                    )
                },
            )

            Spacer(modifier = Modifier.height(12.dp))

            SpinSettingRow(
                title = stringResource(R.string.coinsample),
                onClick = { navController.navigate(Screen.CoinLabel.route) },
                trailing = {
                    SpinIcon(
                        glyph = SpinIconGlyph.ChevronRight,
                        tint = SpinColors.IconMuted,
                        modifier = Modifier.size(24.dp)
                    )
                },
            )
        }
    }
}
