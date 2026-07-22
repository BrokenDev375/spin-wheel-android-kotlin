package com.vga.spinwheel.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

enum class SpinIconGlyph {
    Back,
    Close,
    Home,
    Settings,
    Crown,
    Plus,
    Minus,
    ChevronRight,
    Check,
    Wheel,
    Trash,
    History,
    More,
    Sparkles,
    Sliders,
    Shuffle,
    Reset,
    Share,
    AddCircle,
    Layers,
}

@Composable
fun SpinIcon(
    glyph: SpinIconGlyph,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = (size.minDimension * 0.1f).coerceAtLeast(2.dp.toPx())

        when (glyph) {
            SpinIconGlyph.Back -> {
                drawLine(
                    color = tint,
                    start = Offset(w * 0.64f, h * 0.2f),
                    end = Offset(w * 0.32f, h * 0.5f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color = tint,
                    start = Offset(w * 0.32f, h * 0.5f),
                    end = Offset(w * 0.64f, h * 0.8f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round,
                )
            }

            SpinIconGlyph.Close -> {
                drawLine(tint, Offset(w * 0.26f, h * 0.26f), Offset(w * 0.74f, h * 0.74f), stroke, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.74f, h * 0.26f), Offset(w * 0.26f, h * 0.74f), stroke, StrokeCap.Round)
            }

            SpinIconGlyph.Home -> {
                val roof = Path().apply {
                    moveTo(w * 0.18f, h * 0.48f)
                    lineTo(w * 0.5f, h * 0.2f)
                    lineTo(w * 0.82f, h * 0.48f)
                }
                drawPath(roof, tint, style = Stroke(width = stroke, cap = StrokeCap.Round))
                val body = Path().apply {
                    moveTo(w * 0.28f, h * 0.48f)
                    lineTo(w * 0.28f, h * 0.78f)
                    lineTo(w * 0.72f, h * 0.78f)
                    lineTo(w * 0.72f, h * 0.48f)
                }
                drawPath(body, tint, style = Stroke(width = stroke, cap = StrokeCap.Round))
            }

            SpinIconGlyph.Settings -> {
                drawCircle(
                    color = tint,
                    radius = w * 0.15f,
                    center = Offset(w * 0.5f, h * 0.5f),
                    style = Stroke(width = stroke * 0.8f),
                )
                drawCircle(
                    color = tint,
                    radius = w * 0.31f,
                    center = Offset(w * 0.5f, h * 0.5f),
                    style = Stroke(width = stroke * 0.75f),
                )
                repeat(8) { index ->
                    rotate(index * 45f, Offset(w * 0.5f, h * 0.5f)) {
                        drawLine(
                            color = tint,
                            start = Offset(w * 0.5f, h * 0.08f),
                            end = Offset(w * 0.5f, h * 0.22f),
                            strokeWidth = stroke,
                            cap = StrokeCap.Round,
                        )
                    }
                }
            }

            SpinIconGlyph.Crown -> {
                val crown = Path().apply {
                    moveTo(w * 0.12f, h * 0.38f)
                    lineTo(w * 0.28f, h * 0.68f)
                    lineTo(w * 0.72f, h * 0.68f)
                    lineTo(w * 0.88f, h * 0.38f)
                    lineTo(w * 0.66f, h * 0.52f)
                    lineTo(w * 0.5f, h * 0.18f)
                    lineTo(w * 0.34f, h * 0.52f)
                    close()
                }
                drawPath(crown, tint)
                drawLine(
                    color = tint,
                    start = Offset(w * 0.26f, h * 0.78f),
                    end = Offset(w * 0.74f, h * 0.78f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round,
                )
            }

            SpinIconGlyph.Plus -> {
                drawLine(tint, Offset(w * 0.5f, h * 0.24f), Offset(w * 0.5f, h * 0.76f), stroke, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.24f, h * 0.5f), Offset(w * 0.76f, h * 0.5f), stroke, StrokeCap.Round)
            }

            SpinIconGlyph.Minus -> {
                drawLine(tint, Offset(w * 0.24f, h * 0.5f), Offset(w * 0.76f, h * 0.5f), stroke, StrokeCap.Round)
            }

            SpinIconGlyph.ChevronRight -> {
                drawLine(tint, Offset(w * 0.36f, h * 0.22f), Offset(w * 0.64f, h * 0.5f), stroke, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.64f, h * 0.5f), Offset(w * 0.36f, h * 0.78f), stroke, StrokeCap.Round)
            }

            SpinIconGlyph.Check -> {
                drawLine(tint, Offset(w * 0.2f, h * 0.52f), Offset(w * 0.42f, h * 0.72f), stroke, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.42f, h * 0.72f), Offset(w * 0.82f, h * 0.28f), stroke, StrokeCap.Round)
            }

            SpinIconGlyph.Wheel -> {
                drawCircle(tint, radius = w * 0.38f, center = Offset(w * 0.5f, h * 0.5f), style = Stroke(width = stroke))
                drawCircle(tint, radius = w * 0.1f, center = Offset(w * 0.5f, h * 0.5f))
                repeat(4) { idx ->
                    rotate(idx * 45f, Offset(w * 0.5f, h * 0.5f)) {
                        drawLine(tint, Offset(w * 0.5f, h * 0.12f), Offset(w * 0.5f, h * 0.88f), stroke * 0.7f, StrokeCap.Round)
                    }
                }
            }

            SpinIconGlyph.Trash -> {
                drawLine(tint, Offset(w * 0.2f, h * 0.28f), Offset(w * 0.8f, h * 0.28f), stroke, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.38f, h * 0.2f), Offset(w * 0.62f, h * 0.2f), stroke, StrokeCap.Round)
                val body = Path().apply {
                    moveTo(w * 0.28f, h * 0.28f)
                    lineTo(w * 0.32f, h * 0.8f)
                    lineTo(w * 0.68f, h * 0.8f)
                    lineTo(w * 0.72f, h * 0.28f)
                }
                drawPath(body, tint, style = Stroke(width = stroke, cap = StrokeCap.Round))
            }

            SpinIconGlyph.History -> {
                drawCircle(tint, radius = w * 0.36f, center = Offset(w * 0.5f, h * 0.5f), style = Stroke(width = stroke))
                drawLine(tint, Offset(w * 0.5f, h * 0.26f), Offset(w * 0.5f, h * 0.5f), stroke, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.5f, h * 0.5f), Offset(w * 0.7f, h * 0.5f), stroke, StrokeCap.Round)
            }

            SpinIconGlyph.More -> {
                drawCircle(tint, radius = stroke, center = Offset(w * 0.5f, h * 0.26f))
                drawCircle(tint, radius = stroke, center = Offset(w * 0.5f, h * 0.5f))
                drawCircle(tint, radius = stroke, center = Offset(w * 0.5f, h * 0.74f))
            }

            SpinIconGlyph.Sparkles -> {
                val p = Path().apply {
                    moveTo(w * 0.4f, h * 0.15f)
                    quadraticTo(w * 0.4f, h * 0.4f, w * 0.15f, h * 0.4f)
                    quadraticTo(w * 0.4f, h * 0.4f, w * 0.4f, h * 0.65f)
                    quadraticTo(w * 0.4f, h * 0.4f, w * 0.65f, h * 0.4f)
                    quadraticTo(w * 0.4f, h * 0.4f, w * 0.4f, h * 0.15f)
                }
                drawPath(p, tint)
                val p2 = Path().apply {
                    moveTo(w * 0.75f, h * 0.55f)
                    quadraticTo(w * 0.75f, h * 0.7f, w * 0.6f, h * 0.7f)
                    quadraticTo(w * 0.75f, h * 0.7f, w * 0.75f, h * 0.85f)
                    quadraticTo(w * 0.75f, h * 0.7f, w * 0.9f, h * 0.7f)
                    quadraticTo(w * 0.75f, h * 0.7f, w * 0.75f, h * 0.55f)
                }
                drawPath(p2, tint)
            }

            SpinIconGlyph.Sliders -> {
                drawLine(tint, Offset(w * 0.25f, h * 0.2f), Offset(w * 0.25f, h * 0.8f), stroke, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.5f, h * 0.2f), Offset(w * 0.5f, h * 0.8f), stroke, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.75f, h * 0.2f), Offset(w * 0.75f, h * 0.8f), stroke, StrokeCap.Round)
                drawCircle(tint, radius = stroke * 1.5f, center = Offset(w * 0.25f, h * 0.35f))
                drawCircle(tint, radius = stroke * 1.5f, center = Offset(w * 0.5f, h * 0.65f))
                drawCircle(tint, radius = stroke * 1.5f, center = Offset(w * 0.75f, h * 0.4f))
            }

            SpinIconGlyph.Shuffle -> {
                val p1 = Path().apply {
                    moveTo(w * 0.2f, h * 0.3f)
                    lineTo(w * 0.5f, h * 0.7f)
                    lineTo(w * 0.8f, h * 0.7f)
                }
                drawPath(p1, tint, style = Stroke(width = stroke, cap = StrokeCap.Round))
                val p2 = Path().apply {
                    moveTo(w * 0.2f, h * 0.7f)
                    lineTo(w * 0.5f, h * 0.3f)
                    lineTo(w * 0.8f, h * 0.3f)
                }
                drawPath(p2, tint, style = Stroke(width = stroke, cap = StrokeCap.Round))
            }

            SpinIconGlyph.Reset -> {
                drawCircle(tint, radius = w * 0.32f, center = Offset(w * 0.5f, h * 0.5f), style = Stroke(width = stroke))
                val arrow = Path().apply {
                    moveTo(w * 0.45f, h * 0.15f)
                    lineTo(w * 0.3f, h * 0.28f)
                    lineTo(w * 0.55f, h * 0.35f)
                }
                drawPath(arrow, tint)
            }

            SpinIconGlyph.Share -> {
                val p = Path().apply {
                    moveTo(w * 0.3f, h * 0.65f)
                    quadraticTo(w * 0.45f, h * 0.3f, w * 0.8f, h * 0.3f)
                }
                drawPath(p, tint, style = Stroke(width = stroke, cap = StrokeCap.Round))
                val arrow = Path().apply {
                    moveTo(w * 0.8f, h * 0.18f)
                    lineTo(w * 0.85f, h * 0.32f)
                    lineTo(w * 0.7f, h * 0.4f)
                }
                drawPath(arrow, tint)
            }

            SpinIconGlyph.AddCircle -> {
                drawCircle(tint, radius = w * 0.36f, center = Offset(w * 0.5f, h * 0.5f), style = Stroke(width = stroke))
                drawLine(tint, Offset(w * 0.5f, h * 0.3f), Offset(w * 0.5f, h * 0.7f), stroke, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.3f, h * 0.5f), Offset(w * 0.7f, h * 0.5f), stroke, StrokeCap.Round)
            }

            SpinIconGlyph.Layers -> {
                val diamond = Path().apply {
                    moveTo(w * 0.5f, h * 0.25f)
                    lineTo(w * 0.78f, h * 0.4f)
                    lineTo(w * 0.5f, h * 0.55f)
                    lineTo(w * 0.22f, h * 0.4f)
                    close()
                }
                drawPath(diamond, tint)
                val bottomLayer = Path().apply {
                    moveTo(w * 0.22f, h * 0.55f)
                    lineTo(w * 0.5f, h * 0.7f)
                    lineTo(w * 0.78f, h * 0.55f)
                }
                drawPath(bottomLayer, tint, style = Stroke(width = stroke, cap = StrokeCap.Round))
            }
        }
    }
}
