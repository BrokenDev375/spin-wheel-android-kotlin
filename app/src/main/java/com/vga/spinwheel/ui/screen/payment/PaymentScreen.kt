package com.vga.spinwheel.ui.screen.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vga.spinwheel.ui.components.SpinIcon
import com.vga.spinwheel.ui.components.SpinIconButton
import com.vga.spinwheel.ui.components.SpinIconGlyph
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun PaymentScreen(
    onClose: () -> Unit,
    onRestore: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaymentViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF210013))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = SpinSpacing.ScreenHorizontal)
            .padding(top = 34.dp, bottom = 22.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SpinIconButton(
                glyph = SpinIconGlyph.Close,
                contentDescription = "Close",
                onClick = onClose,
                tint = Color.White,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
            )
            Button(
                onClick = onRestore,
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.2f),
                    contentColor = Color.White,
                ),
            ) {
                Text("Restore", style = MaterialTheme.typography.labelLarge)
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Spin Wheel & Random Tools",
            color = SpinColors.Premium,
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = "Greatly Improve Work Efficiency",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(28.dp))
        FeatureCompareCard()
        Spacer(modifier = Modifier.height(28.dp))

        PaymentPlan.entries.forEach { plan ->
            PlanCard(
                plan = plan,
                selected = plan == state.selectedPlan,
                onClick = { viewModel.selectPlan(plan) },
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        Button(
            onClick = onClose,
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFFFF5B7E), Color(0xFFFF174D)),
                    ),
                    shape = RoundedCornerShape(22.dp),
                ),
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White,
            ),
        ) {
            Text(
                text = if (state.selectedPlan == PaymentPlan.ANNUAL) "3-Days Free Trial!" else "Continue",
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = "Cancel Any Time - 24 hours Before Trial Ends or Before Renewal",
            color = Color.White.copy(alpha = 0.4f),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Term of Use        |        Privacy Policy",
            color = Color.White.copy(alpha = 0.45f),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun FeatureCompareCard() {
    val features = listOf(
        "Remove all ads" to true,
        "Mở khóa tất cả trò chơi" to true,
        "Tạo vòng quay tùy chỉnh" to true,
        "Mở khóa giao diện cao cấp" to false,
        "Priority customer support" to false,
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Feature",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
            CompareHeader("Pro", selected = true)
            CompareHeader("Basic", selected = false)
        }

        features.forEach { (title, basicEnabled) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SpinColors.Action),
                    contentAlignment = Alignment.Center,
                ) {
                    SpinIcon(
                        glyph = SpinIconGlyph.Crown,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp),
                    )
                }
                Text(
                    text = title,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                )
                CheckCell(enabled = true)
                CheckCell(enabled = basicEnabled)
            }
        }
    }
}

@Composable
private fun CompareHeader(
    text: String,
    selected: Boolean,
) {
    Text(
        text = text,
        color = if (selected) SpinColors.Premium else Color.White,
        style = MaterialTheme.typography.titleSmall,
        textAlign = TextAlign.Center,
        modifier = Modifier.size(width = 72.dp, height = 34.dp),
    )
}

@Composable
private fun CheckCell(enabled: Boolean) {
    Box(
        modifier = Modifier.size(width = 72.dp, height = 34.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (enabled) Color(0xFFFFB457) else Color.White.copy(alpha = 0.9f)),
            contentAlignment = Alignment.Center,
        ) {
            SpinIcon(
                glyph = if (enabled) SpinIconGlyph.Check else SpinIconGlyph.Minus,
                tint = if (enabled) Color(0xFF331100) else Color(0xFF220014),
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun PlanCard(
    plan: PaymentPlan,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF5B3E4E))
            .border(
                width = if (selected) 2.dp else 0.dp,
                brush = Brush.horizontalGradient(
                    listOf(SpinColors.Premium, Color(0xFFFF1B61)),
                ),
                shape = RoundedCornerShape(18.dp),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 18.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = plan.title,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = plan.price,
                color = if (selected) SpinColors.Premium else Color.White,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold,
            )
        }
        if (plan.subtitle.isNotBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = plan.subtitle,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
