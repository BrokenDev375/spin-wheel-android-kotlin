package com.vga.spinwheel.ui.screen.language

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brian.base_application.language.LanguageRouter
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.components.SpinPrimaryButton
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun LanguageScreen(
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LanguageViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val state by viewModel.uiState.collectAsState()
    val languageLabels = viewModel.languages.associateWith { language ->
        stringResource(language.localNameRes) to stringResource(language.englishNameRes)
    }
    val filteredLanguages = viewModel.languages.filter { language ->
        val query = state.query.trim()
        val labels = languageLabels.getValue(language)
        query.isBlank() ||
            labels.first.contains(query, ignoreCase = true) ||
            labels.second.contains(query, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = SpinSpacing.ScreenHorizontal)
            .padding(top = 44.dp, bottom = 18.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.language_title),
                    color = Color(0xFF0C1020),
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.language_description),
                    color = Color(0xFF0C1020),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            SpinPrimaryButton(
                text = stringResource(R.string.language_done),
                onClick = {
                    val selectedCode = viewModel.selectedCode()
                    if (activity != null) {
                        LanguageRouter.confirmLanguageSelection(activity, selectedCode, null, false)
                    }
                    onDone()
                },
                modifier = Modifier.size(width = 112.dp, height = 64.dp),
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        OutlinedTextField(
            value = state.query,
            onValueChange = viewModel::setQuery,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.language_search_hint),
                    color = Color(0xFF9CA3AF),
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(SpinRadius.Control),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2A9FE8),
                unfocusedBorderColor = Color(0xFF2A9FE8),
                focusedTextColor = Color(0xFF0C1020),
                unfocusedTextColor = Color(0xFF0C1020),
                cursorColor = SpinColors.Action,
            ),
        )

        Spacer(modifier = Modifier.height(18.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(filteredLanguages, key = { it.code }) { language ->
                LanguageRow(
                    language = language,
                    localName = languageLabels.getValue(language).first,
                    englishName = languageLabels.getValue(language).second,
                    selected = language.code == state.selectedCode,
                    onClick = { viewModel.selectLanguage(language.code) },
                )
            }
        }
    }
}

@Composable
private fun LanguageRow(
    language: LanguageOption,
    localName: String,
    englishName: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) Color(0xFFE5F4FF) else Color(0xFFF1F3F7))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(flagColor(language.code)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = language.flag,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = localName,
                color = Color(0xFF0C1020),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = englishName,
                color = Color(0xFF8C8FA3),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = SpinColors.Action,
                unselectedColor = SpinColors.Action,
            ),
        )
    }
}

private fun flagColor(code: String): Color = when (code) {
    "vi" -> Color(0xFFE91D25)
    "ru" -> Color(0xFF2563EB)
    "hi" -> Color(0xFFF59E0B)
    "nl" -> Color(0xFFDC2626)
    "tr" -> Color(0xFFE11D48)
    "zh", "zh-TW" -> Color(0xFFEF2B19)
    "fa" -> Color(0xFF16A34A)
    "uk" -> Color(0xFF2563EB)
    else -> Color(0xFF0EA5E9)
}

private tailrec fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
