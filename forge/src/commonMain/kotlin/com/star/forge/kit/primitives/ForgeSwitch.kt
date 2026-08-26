package com.star.forge.kit.primitives

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.LayoutDirection
import com.star.forge.kit.theme.ForgeSwitchSize
import com.star.forge.kit.theme.ForgeTheme

@Immutable
public data class ForgeSwitchColors(
    public val checkedTrack: Color,
    public val uncheckedTrack: Color,
    public val checkedThumb: Color,
    public val uncheckedThumb: Color,
    public val pressedTrack: Color,
    public val disabledTrack: Color,
    public val disabledThumb: Color,
)

public object ForgeSwitchDefaults {
    @Composable
    public fun colors(): ForgeSwitchColors {
        val visual = ForgeTheme.components.switch.visuals
        return ForgeSwitchColors(
            checkedTrack = visual.selectedContainer ?: ForgeTheme.colors.primary,
            uncheckedTrack = visual.unselectedContainer ?: ForgeTheme.colors.surfaceVariant,
            checkedThumb = visual.selectedContent ?: ForgeTheme.colors.onPrimary,
            uncheckedThumb = visual.unselectedContent ?: ForgeTheme.colors.onSurfaceVariant,
            pressedTrack = ForgeTheme.colors.primary.copy(alpha = visual.pressedOpacity ?: ForgeTheme.opacity.pressed),
            disabledTrack = visual.disabledContainer ?: ForgeTheme.colors.onSurface.copy(alpha = ForgeTheme.opacity.disabledContainer),
            disabledThumb = visual.disabledContent ?: ForgeTheme.colors.onSurface.copy(alpha = ForgeTheme.opacity.disabledContent),
        )
    }
}

@Composable
public fun ForgeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ForgeSwitchSize = ForgeTheme.components.switch.medium,
    colors: ForgeSwitchColors = ForgeSwitchDefaults.colors(),
    accessibilityLabel: String? = null,
    accessibilityStateDescription: String? = null,
) {
    require(size.width.value > 0f && size.height.value > 0f && size.thumbSize.value > 0f && size.thumbPadding.value >= 0f) {
        "switch dimensions must be valid"
    }
    require(size.thumbSize + (size.thumbPadding * 2) <= size.height) { "switch thumb must fit inside the track" }
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val layoutDirection = LocalLayoutDirection.current
    val targetFraction = if (checked.xor(layoutDirection == LayoutDirection.Rtl)) 1f else 0f
    val fraction by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec =
            tween(
                ForgeTheme.components.switch.visuals.motionDurationMillis ?: ForgeTheme.motion.normalDurationMillis,
                easing = ForgeTheme.motion.standardEasing,
            ),
        label = "ForgeSwitchThumb",
    )
    val targetTrack =
        when {
            !enabled -> colors.disabledTrack
            pressed -> colors.pressedTrack
            checked -> colors.checkedTrack
            else -> colors.uncheckedTrack
        }
    val targetThumb =
        when {
            !enabled -> colors.disabledThumb
            checked -> colors.checkedThumb
            else -> colors.uncheckedThumb
        }
    val track by animateColorAsState(targetTrack, tween(ForgeTheme.motion.fastDurationMillis), label = "ForgeSwitchTrack")
    val thumb by animateColorAsState(targetThumb, tween(ForgeTheme.motion.fastDurationMillis), label = "ForgeSwitchThumbColor")
    val touchTarget = ForgeTheme.components.switch.minimumTouchTarget ?: ForgeTheme.touchTargets.minimum

    Box(
        modifier =
            modifier
                .defaultMinSize(minWidth = touchTarget, minHeight = touchTarget)
                .toggleable(
                    value = checked,
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    role = Role.Switch,
                    onValueChange = onCheckedChange,
                ).semantics {
                    accessibilityLabel?.let { contentDescription = it }
                    accessibilityStateDescription?.let { stateDescription = it }
                    if (!enabled) disabled()
                },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.size(size.width, size.height)) {
            val trackWidth = this.size.width
            val trackHeight = this.size.height
            val thumbRadius = size.thumbSize.toPx() / 2f
            val minX = size.thumbPadding.toPx() + thumbRadius
            val maxX = trackWidth - size.thumbPadding.toPx() - thumbRadius
            val thumbX = minX + ((maxX - minX) * fraction)
            drawRoundRect(track, size = Size(trackWidth, trackHeight), cornerRadius = CornerRadius(trackHeight / 2f))
            drawCircle(thumb, radius = thumbRadius, center = Offset(thumbX, trackHeight / 2f))
        }
    }
}
