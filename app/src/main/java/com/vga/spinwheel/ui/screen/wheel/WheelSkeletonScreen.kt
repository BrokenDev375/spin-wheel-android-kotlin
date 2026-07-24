package com.vga.spinwheel.ui.screen.wheel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinPrimaryButton
import com.vga.spinwheel.ui.components.SpinSecondaryButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinTopBar
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun WheelSkeletonScreen(
    onBack: () -> Unit,
    onAddWheel: () -> Unit,
    onEditWheel: () -> Unit,
    onSpinWheel: () -> Unit,
    onWheelSettings: () -> Unit,
    onWheelResult: () -> Unit,
    onWheelHistory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background),
        containerColor = SpinColors.Background,
        topBar = {
            SpinTopBar(
                title = stringResource(R.string.spinwheel),
                navigationIcon = SpinIconGlyph.Back,
                navigationDescription = stringResource(R.string.content_description_back),
                onNavigationClick = onBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    PaddingValues(
                        start = SpinSpacing.ScreenHorizontal,
                        top = 18.dp,
                        end = SpinSpacing.ScreenHorizontal,
                        bottom = 28.dp,
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.mylist),
                color = SpinColors.TextPrimary,
            )
            SpinPrimaryButton(
                text = stringResource(R.string.addspin),
                onClick = onAddWheel,
                modifier = Modifier.fillMaxWidth(),
            )
            SpinSecondaryButton(
                text = stringResource(R.string.edit),
                onClick = onEditWheel,
                modifier = Modifier.fillMaxWidth(),
            )
            SpinSecondaryButton(
                text = stringResource(R.string.spin),
                onClick = onSpinWheel,
                modifier = Modifier.fillMaxWidth(),
            )
            SpinSecondaryButton(
                text = stringResource(R.string.setings),
                onClick = onWheelSettings,
                modifier = Modifier.fillMaxWidth(),
            )
            SpinSecondaryButton(
                text = stringResource(R.string.results),
                onClick = onWheelResult,
                modifier = Modifier.fillMaxWidth(),
            )
            SpinSecondaryButton(
                text = stringResource(R.string.history),
                onClick = onWheelHistory,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
