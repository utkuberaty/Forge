package com.star.forge.kit.primitives

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.LayoutDirection
import com.star.forge.kit.theme.ForgeSliderSize
import com.star.forge.kit.theme.ForgeTheme
import kotlin.math.round

/** Colors used by [ForgeSlider]. */
@Immutable
public data class ForgeSliderColors(
    public val activeTrack: Color,
    public val inactiveTrack: Color,
    public val thumb: Color,
    public val pressedThumb: Color,
    public val disabledActiveTrack: Color,
    public val disabledInactiveTrack: Color,
    public val disabledThumb: Color,
) {
    internal fun active(enabled: Boolean): Color = if (enabled) activeTrack else disabledActiveTrack

    internal fun inactive(enabled: Boolean): Color = if (enabled) inactiveTrack else disabledInactiveTrack

    internal fun thumb(
        enabled: Boolean,
        pressed: Boolean,
    ): Color =
        when {
            !enabled -> disabledThumb
            pressed -> pressedThumb
            else -> thumb
        }
}

/** Defaults for [ForgeSlider]. */
public object ForgeSliderDefaults {
    /** Default colors resolved from the active tokens. */
    @Composable
    public fun colors(): ForgeSliderColors {
        val visual = ForgeTheme.components.slider.visuals
        return ForgeSliderColors(
            activeTrack = visual.selectedContainer ?: ForgeTheme.colors.primary,
            inactiveTrack = visual.unselectedContainer ?: ForgeTheme.colors.surfaceVariant,
            thumb = visual.selectedContent ?: ForgeTheme.colors.primary,
            pressedThumb = ForgeTheme.colors.primaryContainer,
            disabledActiveTrack = visual.disabledContent ?: ForgeTheme.colors.onSurface.copy(alpha = ForgeTheme.opacity.disabledContent),
            disabledInactiveTrack =
                visual.disabledContainer ?: ForgeTheme.colors.onSurface.copy(alpha = ForgeTheme.opacity.disabledContainer),
            disabledThumb = visual.disabledContent ?: ForgeTheme.colors.onSurface.copy(alpha = ForgeTheme.opacity.disabledContent),
        )
    }
}

/** Forge-owned adjustable value control with shared pointer and accessibility stepping. */
@Composable
public fun ForgeSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    size: ForgeSliderSize = ForgeTheme.components.slider.medium,
    colors: ForgeSliderColors = ForgeSliderDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    accessibilityLabel: String? = null,
    accessibilityValueDescription: String? = null,
) {
    require(value.isFinite()) { "value must be finite" }
    require(valueRange.start.isFinite() && valueRange.endInclusive.isFinite()) {
        "valueRange endpoints must be finite"
    }
    require(valueRange.start < valueRange.endInclusive) { "valueRange must be increasing" }
    require(steps >= 0) { "steps must be nonnegative" }
    require(size.visualHeight.value > 0f && size.trackHeight.value > 0f && size.thumbRadius.value > 0f) {
        "slider dimensions must be positive"
    }

    val layoutDirection = LocalLayoutDirection.current
    val density = LocalDensity.current
    val coercedValue = snapSliderValue(value, valueRange, steps)
    val logicalFraction = sliderFraction(coercedValue, valueRange)
    val visualFraction = sliderVisualFraction(logicalFraction, layoutDirection == LayoutDirection.Rtl)
    val pressed by interactionSource.collectIsPressedAsState()
    var widthPx by remember { mutableIntStateOf(0) }

    fun updateFromPosition(x: Float) {
        if (!enabled || widthPx <= 0) return
        val thumbRadiusPx = with(density) { size.thumbRadius.toPx() }
        val trackWidth = (widthPx - (thumbRadiusPx * 2f)).coerceAtLeast(1f)
        val visual = ((x - thumbRadiusPx) / trackWidth).coerceIn(0f, 1f)
        val logical = if (layoutDirection == LayoutDirection.Rtl) 1f - visual else visual
        val candidate = valueRange.start + ((valueRange.endInclusive - valueRange.start) * logical)
        onValueChange(snapSliderValue(candidate, valueRange, steps))
    }

    fun updateValue(candidate: Float): Boolean {
        if (!enabled || !candidate.isFinite()) return false
        onValueChange(snapSliderValue(candidate, valueRange, steps))
        onValueChangeFinished?.invoke()
        return true
    }

    Canvas(
        modifier =
            modifier
                .fillMaxWidth()
                .height(size.visualHeight)
                .defaultMinSize(minHeight = ForgeTheme.components.slider.minimumTouchTarget ?: ForgeTheme.touchTargets.minimum)
                .semantics {
                    accessibilityLabel?.let { contentDescription = it }
                    accessibilityValueDescription?.let { stateDescription = it }
                    progressBarRangeInfo = ProgressBarRangeInfo(coercedValue, valueRange, steps)
                    setProgress { updateValue(it) }
                    if (!enabled) disabled()
                }.onSizeChanged { widthPx = it.width }
                .pointerInput(enabled, valueRange, steps, widthPx, layoutDirection) {
                    if (!enabled) return@pointerInput
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val press = PressInteraction.Press(down.position)
                        interactionSource.tryEmit(press)
                        updateFromPosition(down.position.x)
                        down.consume()

                        var released = false
                        var anyPressed: Boolean
                        do {
                            val event = awaitPointerEvent()
                            event.changes.forEach { change ->
                                if (change.pressed) {
                                    updateFromPosition(change.position.x)
                                    change.consume()
                                }
                                released = released || change.changedToUpIgnoreConsumed()
                            }
                            anyPressed = event.changes.any { it.pressed }
                        } while (anyPressed)

                        if (released) {
                            interactionSource.tryEmit(PressInteraction.Release(press))
                        } else {
                            interactionSource.tryEmit(PressInteraction.Cancel(press))
                        }
                        onValueChangeFinished?.invoke()
                    }
                },
    ) {
        val centerY = this.size.height / 2f
        val trackHeight = size.trackHeight.toPx()
        val thumbRadius = size.thumbRadius.toPx()
        val trackStart = thumbRadius
        val trackWidth = (this.size.width - (thumbRadius * 2f)).coerceAtLeast(0f)
        val thumbX = trackStart + (trackWidth * visualFraction)
        val trackTop = centerY - (trackHeight / 2f)
        val trackRadius = trackHeight / 2f

        drawRoundRect(
            color = colors.inactive(enabled),
            topLeft = Offset(trackStart, trackTop),
            size = Size(trackWidth, trackHeight),
            cornerRadius = CornerRadius(trackRadius, trackRadius),
        )
        val activeStart = if (layoutDirection == LayoutDirection.Rtl) thumbX else trackStart
        val activeWidth =
            if (layoutDirection == LayoutDirection.Rtl) {
                (trackStart + trackWidth) - thumbX
            } else {
                thumbX - trackStart
            }
        drawRoundRect(
            color = colors.active(enabled),
            topLeft = Offset(activeStart, trackTop),
            size = Size(activeWidth.coerceAtLeast(0f), trackHeight),
            cornerRadius = CornerRadius(trackRadius, trackRadius),
        )
        drawCircle(
            color = colors.thumb(enabled, pressed),
            radius = thumbRadius,
            center = Offset(thumbX, centerY),
        )
    }
}

internal fun sliderFraction(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
): Float = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)

internal fun sliderVisualFraction(
    logicalFraction: Float,
    rtl: Boolean,
): Float = if (rtl) 1f - logicalFraction else logicalFraction

internal fun snapSliderValue(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
): Float {
    val fraction = sliderFraction(value.coerceIn(valueRange.start, valueRange.endInclusive), valueRange)
    if (steps == 0) return valueRange.start + ((valueRange.endInclusive - valueRange.start) * fraction)
    val intervals = steps + 1
    val snappedFraction = round(fraction * intervals) / intervals
    return valueRange.start + ((valueRange.endInclusive - valueRange.start) * snappedFraction)
}
