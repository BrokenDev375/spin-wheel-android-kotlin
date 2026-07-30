package com.vga.spinwheel.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.R
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing

@Composable
fun SpinIconButton(
    glyph: SpinIconGlyph,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = SpinColors.IconMuted,
    enabled: Boolean = true,
) {
    IconButton(
        onClick = rememberClickWithSound(onClick),
        modifier = modifier.size(SpinSpacing.HeaderButton),
        enabled = enabled,
    ) {
        SpinIcon(
            glyph = glyph,
            tint = tint,
            modifier = Modifier.size(30.dp),
        )
    }
}

@Composable
fun SpinPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = rememberClickWithSound(onClick),
        modifier = modifier
            .height(SpinSpacing.ControlHeight)
            .defaultMinSize(minHeight = SpinSpacing.ControlHeight),
        enabled = enabled,
        shape = RoundedCornerShape(SpinRadius.Button),
        colors = ButtonDefaults.buttonColors(
            containerColor = SpinColors.Action,
            contentColor = Color.White,
            disabledContainerColor = SpinColors.IconMuted,
            disabledContentColor = SpinColors.TextMuted,
        ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun SpinSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = rememberClickWithSound(onClick),
        modifier = modifier
            .height(SpinSpacing.ControlHeight)
            .defaultMinSize(minHeight = SpinSpacing.ControlHeight),
        shape = RoundedCornerShape(SpinRadius.Button),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = SpinColors.TextPrimary),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun SpinBottomActionBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SpinColors.BackgroundDeep.copy(alpha = 0.94f))
            .padding(horizontal = SpinSpacing.ScreenHorizontal, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
fun SpinSettingRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    containerColor: Color = SpinSettingContainerColor,
    borderColor: Color = SpinSettingBorderColor,
) {
    val shape = RoundedCornerShape(SpinSettingRadius)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .clip(shape)
            .background(containerColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape,
            )
            .then(if (onClick == null) Modifier else Modifier.softClickableWithSound(onClick = onClick))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leading?.invoke()
        Box(modifier = Modifier.weight(1f)) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = SpinColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = SpinColors.TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
        trailing?.invoke()
    }
}

@Composable
fun SpinStepper(
    value: String,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SpinSettingStepper(
        value = value,
        onMinus = onMinus,
        onPlus = onPlus,
        modifier = modifier,
    )
}

@Composable
fun SpinSettingStepper(
    value: String,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    modifier: Modifier = Modifier,
    minusText: String = "-",
    plusText: String = "+",
) {
    val shape = RoundedCornerShape(14.dp)

    Row(
        modifier = modifier
            .clip(shape)
            .background(Color.White.copy(alpha = 0.075f))
            .border(1.dp, Color.White.copy(alpha = 0.055f), shape)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        SpinSettingStepperButton(
            text = minusText,
            onClick = onMinus,
        )
        Text(
            text = value,
            modifier = Modifier.defaultMinSize(minWidth = 38.dp),
            color = SpinColors.TextPrimary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge,
        )
        SpinSettingStepperButton(
            text = plusText,
            onClick = onPlus,
        )
    }
}

@Composable
fun SpinSettingStepperButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val shape = RoundedCornerShape(10.dp)

    Box(
        modifier = modifier
            .size(32.dp)
            .clip(shape)
            .background(Color.White.copy(alpha = if (enabled) 0.88f else 0.34f))
            .softClickableWithSound(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color(0xFF191624).copy(alpha = if (enabled) 1f else 0.46f),
            fontSize = 20.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun SpinToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Switch(
        checked = checked,
        onCheckedChange = rememberValueChangeWithSound(onCheckedChange),
        modifier = modifier,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color(0xFFFFF7EE),
            checkedTrackColor = SpinColors.Action.copy(alpha = 0.88f),
            checkedBorderColor = SpinColors.Action.copy(alpha = 0.34f),
            uncheckedThumbColor = Color(0xFFD0CEDF),
            uncheckedTrackColor = Color.White.copy(alpha = 0.12f),
            uncheckedBorderColor = Color.White.copy(alpha = 0.08f),
        ),
    )
}

@Composable
fun SpinShareButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    backgroundColor: Color = Color(0xFF39A9F2),
) {
    val resolvedText = text ?: stringResource(R.string.sharereust)

    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .clickableWithSound(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            SpinIcon(
                glyph = SpinIconGlyph.Share,
                tint = Color.White,
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = resolvedText,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun SpinRetryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    backgroundColor: Color = Color(0xFFDE3D2D),
) {
    val resolvedText = text ?: stringResource(R.string.try_again)

    Box(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickableWithSound(onClick = onClick)
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = resolvedText,
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private val SpinSettingRadius = 16.dp
private val SpinSettingContainerColor = Color.White.copy(alpha = 0.065f)
private val SpinSettingBorderColor = Color(0xFF33E3FF).copy(alpha = 0.22f)

@Composable
fun SpinResultCard(
    modifier: Modifier = Modifier,
    cardHeight: Dp = 450.dp,
    contentPadding: Dp = 18.dp,
    backgroundColor: Color = Color(0xFF3D3D3C),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clip(RoundedCornerShape(18.dp))
            .background(backgroundColor)
            .border(
                width = 1.5.dp,
                color = Color.White.copy(alpha = 0.62f),
                shape = RoundedCornerShape(18.dp),
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
