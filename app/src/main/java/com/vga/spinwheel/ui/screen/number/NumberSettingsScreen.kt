package com.vga.spinwheel.ui.screen.number

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.components.SpinScreen
import com.vga.spinwheel.ui.components.SpinSettingRow
import com.vga.spinwheel.ui.components.SpinToggle
import com.vga.spinwheel.ui.theme.SpinColors

@Composable
fun NumberSettingsScreen(
    navController: NavController,
    viewModel: NumberViewModel = hiltViewModel()
) {
    val persistedMin by viewModel.min.collectAsState()
    val persistedMax by viewModel.max.collectAsState()
    val persistedCount by viewModel.count.collectAsState()
    val persistedAllowDuplicates by viewModel.allowDuplicates.collectAsState()
    val persistedDuration by viewModel.duration.collectAsState()

    val tempMin by viewModel.tempMin.collectAsState()
    val tempMax by viewModel.tempMax.collectAsState()
    val tempCount by viewModel.tempCount.collectAsState()
    val tempAllowDuplicates by viewModel.tempAllowDuplicates.collectAsState()
    val tempDuration by viewModel.tempDuration.collectAsState()

    var minText by rememberSaveable { mutableStateOf(tempMin.toString()) }
    var maxText by rememberSaveable { mutableStateOf(tempMax.toString()) }

    LaunchedEffect(
        persistedMin,
        persistedMax,
        persistedCount,
        persistedAllowDuplicates,
        persistedDuration,
    ) {
        viewModel.initTempSettings()
    }

    LaunchedEffect(tempMin) {
        if (minText.toIntOrNull() != tempMin) {
            minText = tempMin.toString()
        }
    }
    LaunchedEffect(tempMax) {
        if (maxText.toIntOrNull() != tempMax) {
            maxText = tempMax.toString()
        }
    }

    val saveAndBack: () -> Unit = {
        minText.toIntOrNull()?.let { viewModel.updateTempSettings(min = it) }
        maxText.toIntOrNull()?.let { viewModel.updateTempSettings(max = it) }
        viewModel.saveSettings()
        navController.popBackStack()
        Unit
    }

    SpinScreen(
        title = stringResource(R.string.customsize),
        navigationIcon = SpinIconGlyph.Back,
        navigationDescription = stringResource(R.string.content_description_back),
        onNavigationClick = { navController.popBackStack() },
        centerTitle = false,
        topBarTitleStartPadding = 39.dp,
        actions = {
            TextButton(onClick = saveAndBack) {
                Text(
                    text = stringResource(R.string.save),
                    color = SpinColors.Action,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                )
            }
        },
    ) { contentModifier ->
        Column(
            modifier = contentModifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            SpinSettingRow(
                title = stringResource(R.string.form),
                trailing = {
                    NumberValueInput(
                        value = minText,
                        onValueChange = { value ->
                            if (value.matches(NumberInputPattern)) {
                                minText = value
                                value.toIntOrNull()?.let {
                                    viewModel.updateTempSettings(min = it.coerceIn(-999, 999))
                                }
                            }
                        },
                    )
                }
            )

            SpinSettingRow(
                title = stringResource(R.string.to),
                trailing = {
                    NumberValueInput(
                        value = maxText,
                        onValueChange = { value ->
                            if (value.matches(NumberInputPattern)) {
                                maxText = value
                                value.toIntOrNull()?.let {
                                    viewModel.updateTempSettings(max = it.coerceIn(-999, 999))
                                }
                            }
                        },
                    )
                }
            )

            SpinSettingRow(
                title = stringResource(R.string.number_generate_count),
                trailing = {
                    NumberStepper(
                        value = tempCount.toString(),
                        onMinus = { if (tempCount > 1) viewModel.updateTempSettings(count = tempCount - 1) },
                        onPlus = { if (tempCount < 99) viewModel.updateTempSettings(count = tempCount + 1) },
                    )
                }
            )

            SpinSettingRow(
                title = stringResource(R.string.number_allow_duplicates),
                trailing = {
                    SpinToggle(
                        checked = tempAllowDuplicates,
                        onCheckedChange = { viewModel.updateTempSettings(allowDuplicates = it) }
                    )
                }
            )

            SpinSettingRow(
                title = stringResource(R.string.duration),
                trailing = {
                    NumberStepper(
                        value = "${tempDuration}s",
                        onMinus = { if (tempDuration > 2) viewModel.updateTempSettings(duration = tempDuration - 1) },
                        onPlus = { if (tempDuration < 10) viewModel.updateTempSettings(duration = tempDuration + 1) },
                    )
                }
            )

            SpinSettingRow(
                title = stringResource(R.string.history),
                onClick = { navController.navigate(com.vga.spinwheel.ui.nav.NumberRoutes.HISTORY) },
                trailing = {
                    SpinIcon(
                        glyph = SpinIconGlyph.ChevronRight,
                        tint = SpinColors.IconMuted,
                        modifier = Modifier.size(24.dp),
                    )
                },
            )
        }
    }
}

@Composable
private fun NumberValueInput(
    value: String,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier.width(72.dp),
        horizontalAlignment = Alignment.End,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.width(72.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            cursorBrush = SolidColor(SpinColors.Action),
        )
        HorizontalDivider(
            modifier = Modifier.width(72.dp),
            thickness = 1.dp,
            color = Color.White.copy(alpha = 0.12f),
        )
    }
}

@Composable
private fun NumberStepper(
    value: String,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        NumberStepperButton(text = "−", onClick = onMinus)
        Text(
            text = value,
            modifier = Modifier.width(32.dp),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
        )
        NumberStepperButton(text = "+", onClick = onPlus)
    }
}

@Composable
private fun NumberStepperButton(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
        )
    }
}

private val NumberInputPattern = Regex("-?\\d{0,3}")
