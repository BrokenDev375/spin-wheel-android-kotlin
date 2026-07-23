package com.vga.spinwheel.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        onClick = onClick,
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
        onClick = onClick,
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
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun SpinSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(SpinSpacing.ControlHeight)
            .defaultMinSize(minHeight = SpinSpacing.ControlHeight),
        shape = RoundedCornerShape(SpinRadius.Button),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = SpinColors.TextPrimary),
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
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
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SpinRadius.Control))
            .background(Color.White.copy(alpha = 0.08f))
            .then(
                if (onClick == null) Modifier else Modifier.border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(SpinRadius.Control),
                )
            )
            .then(if (onClick == null) Modifier else Modifier.clickable(onClick = onClick))
            .padding(horizontal = 14.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leading?.invoke()
        Box(modifier = Modifier.weight(1f)) {
            androidx.compose.foundation.layout.Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SpinColors.TextPrimary,
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = SpinColors.TextMuted,
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
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(SpinRadius.Control))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        SpinIconButton(
            glyph = SpinIconGlyph.Minus,
            contentDescription = "Decrease",
            onClick = onMinus,
            tint = SpinColors.TextPrimary,
            modifier = Modifier.size(36.dp),
        )
        Text(
            text = value,
            modifier = Modifier.defaultMinSize(minWidth = 44.dp),
            color = SpinColors.TextPrimary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge,
        )
        SpinIconButton(
            glyph = SpinIconGlyph.Plus,
            contentDescription = "Increase",
            onClick = onPlus,
            tint = SpinColors.TextPrimary,
            modifier = Modifier.size(36.dp),
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
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = SpinColors.Action,
            uncheckedThumbColor = SpinColors.TextMuted,
            uncheckedTrackColor = Color.White.copy(alpha = 0.16f),
        ),
    )
}

@Composable
fun SpinShareButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Chia sẻ kết quả",
    backgroundColor: Color = Color(0xFF39A9F2),
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
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
                text = text,
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
    text: String = "Thử lại",
    backgroundColor: Color = Color(0xFFDE3D2D),
) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun SpinResultCard(
    modifier: Modifier = Modifier,
    cardHeight: Dp = 450.dp,
    contentPadding: Dp = 18.dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF3D3D3C))
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
