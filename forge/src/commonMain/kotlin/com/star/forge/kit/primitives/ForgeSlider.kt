package com.star.forge.kit.primitives

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.star.forge.kit.theme.ForgeTheme

/** Size scale for [ForgeSlider]. */
enum class ForgeSliderSize(
    val height: Dp,
    val trackHeight: Dp,
    val thumbRadius: Dp,
) {
    Small(height = 32.dp, trackHeight = 5.dp, thumbRadius = 8.dp),
    Medium(height = 40.dp, trackHeight = 6.dp, thumbRadius = 10.dp),
    Large(height = 48.dp, trackHeight = 8.dp, thumbRadius = 12.dp),
}

/** Color state model for [ForgeSlider]. */
@Immutable
data class ForgeSliderColors(
    val activeTrack: Color,
    val inactiveTrack: Color,
    val thumb: Color,
    val pressedThumb: Color,
    val disabledActiveTrack: Color,
    val disabledInactiveTrack: Color,
    val disabledThumb: Color,
) {
    fun active(enabled: Boolean): Color = if (enabled) activeTrack else disabledActiveTrack
    fun inactive(enabled: Boolean): Color = if (enabled) inactiveTrack else disabledInactiveTrack
    fun thumb(enabled: Boolean, pressed: Boolean): Color = when {
        !enabled -> disabledThumb
        pressed -> pressedThumb
        else -> thumb
    }
}

object ForgeSliderDefaults {
    /** Default Forge-owned slider colors. */
    @Composable
    fun colors(): ForgeSliderColors = ForgeSliderColors(
        activeTrack = ForgeTheme.colors.primary,
        inactiveTrack = ForgeTheme.colors.surfaceVariant,
        thumb = ForgeTheme.colors.primary,
        pressedThumb = ForgeTheme.colors.primaryContainer,
        disabledActiveTrack = ForgeTheme.colors.onSurface.copy(alpha = 0.24f),
        disabledInactiveTrack = ForgeTheme.colors.onSurface.copy(alpha = 0.10f),
        disabledThumb = ForgeTheme.colors.onSurface.copy(alpha = 0.34f),
    )
}

/**
 * Forge-owned slider.
 *
 * Use this for continuous numeric values. It is drawn and handled by Forge,
 * rather than wrapping Material slider behavior.
 *
 * @param accessibilityLabel optional label for the adjustable value.
 * @param accessibilityValueDescription optional readable value such as "42%".
 */
@Composable
fun ForgeSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ForgeSliderSize = ForgeSliderSize.Medium,
    colors: ForgeSliderColors = ForgeSliderDefaults.colors(),
    accessibilityLabel: String? = null,
    accessibilityValueDescription: String? = null,
) {
    val coercedValue = value.coerceIn(valueRange.start, valueRange.endInclusive)
    val range = valueRange.endInclusive - valueRange.start
    val fraction = if (range == 0f) {
        0f
    } else {
        ((coercedValue - valueRange.start) / range).coerceIn(0f, 1f)
    }
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    var widthPx by remember { mutableStateOf(0) }

    fun updateFromPosition(x: Float) {
        if (!enabled || widthPx <= 0) return
        val nextFraction = (x / widthPx).coerceIn(0f, 1f)
        onValueChange(valueRange.start + (range * nextFraction))
    }

    fun updateValue(nextValue: Float): Boolean {
        if (!enabled) return false
        onValueChange(nextValue.coerceIn(valueRange.start, valueRange.endInclusive))
        return true
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(size.height)
            .semantics {
                if (accessibilityLabel != null) {
                    contentDescription = accessibilityLabel
                }
                if (accessibilityValueDescription != null) {
                    stateDescription = accessibilityValueDescription
                }
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = coercedValue,
                    range = valueRange,
                    steps = 0,
                )
                setProgress { nextValue -> updateValue(nextValue) }
                if (!enabled) {
                    disabled()
                }
            }
            .onSizeChanged { widthPx = it.width }
            .pointerInput(enabled, valueRange, widthPx) {
                if (!enabled) return@pointerInput
                detectTapGestures { offset -> updateFromPosition(offset.x) }
            }
            .pointerInput(enabled, valueRange, widthPx) {
                if (!enabled) return@pointerInput
                detectDragGestures { change, _ ->
                    updateFromPosition(change.position.x)
                }
            },
    ) {
        val centerY = this.size.height / 2f
        val trackHeight = size.trackHeight.toPx()
        val trackTop = centerY - (trackHeight / 2f)
        val trackRadius = trackHeight / 2f
        val activeWidth = this.size.width * fraction
        val thumbRadius = size.thumbRadius.toPx()

        drawRoundRect(
            color = colors.inactive(enabled),
            topLeft = Offset(0f, trackTop),
            size = Size(width = this.size.width, height = trackHeight),
            cornerRadius = CornerRadius(trackRadius, trackRadius),
        )
        drawRoundRect(
            color = colors.active(enabled),
            topLeft = Offset(0f, trackTop),
            size = Size(width = activeWidth, height = trackHeight),
            cornerRadius = CornerRadius(trackRadius, trackRadius),
        )
        drawCircle(
            color = colors.thumb(enabled = enabled, pressed = pressed),
            radius = thumbRadius,
            center = Offset(activeWidth, centerY),
        )
    }
}
