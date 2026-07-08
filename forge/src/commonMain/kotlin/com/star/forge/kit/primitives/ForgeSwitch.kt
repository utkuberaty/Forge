package com.star.forge.kit.primitives

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.star.forge.kit.theme.ForgeTheme

/** Size scale for [ForgeSwitch]. */
enum class ForgeSwitchSize(
    val width: Dp,
    val height: Dp,
    val thumbSize: Dp,
    val thumbPadding: Dp,
) {
    Small(width = 42.dp, height = 24.dp, thumbSize = 18.dp, thumbPadding = 3.dp),
    Medium(width = 52.dp, height = 30.dp, thumbSize = 22.dp, thumbPadding = 4.dp),
    Large(width = 62.dp, height = 36.dp, thumbSize = 28.dp, thumbPadding = 4.dp),
}

/** Color state model for [ForgeSwitch]. */
@Immutable
data class ForgeSwitchColors(
    val checkedTrack: Color,
    val uncheckedTrack: Color,
    val checkedThumb: Color,
    val uncheckedThumb: Color,
    val pressedTrack: Color,
    val disabledTrack: Color,
    val disabledThumb: Color,
) {
    fun track(checked: Boolean, enabled: Boolean, pressed: Boolean): Color = when {
        !enabled -> disabledTrack
        pressed -> pressedTrack
        checked -> checkedTrack
        else -> uncheckedTrack
    }

    fun thumb(checked: Boolean, enabled: Boolean): Color = when {
        !enabled -> disabledThumb
        checked -> checkedThumb
        else -> uncheckedThumb
    }
}

object ForgeSwitchDefaults {
    /** Default Forge-owned switch colors. */
    @Composable
    fun colors(): ForgeSwitchColors = ForgeSwitchColors(
        checkedTrack = ForgeTheme.colors.primary,
        uncheckedTrack = ForgeTheme.colors.surfaceVariant,
        checkedThumb = ForgeTheme.colors.onPrimary,
        uncheckedThumb = ForgeTheme.colors.onSurfaceVariant,
        pressedTrack = ForgeTheme.colors.primary.copy(alpha = 0.72f),
        disabledTrack = ForgeTheme.colors.onSurface.copy(alpha = 0.10f),
        disabledThumb = ForgeTheme.colors.onSurface.copy(alpha = 0.34f),
    )
}

/**
 * Forge-owned switch.
 *
 * Use this for binary settings. The control is drawn by Forge and does not wrap
 * Material switch components.
 *
 * @param accessibilityLabel optional label when the switch is not paired with
 * visible text in the same accessible row.
 */
@Composable
fun ForgeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ForgeSwitchSize = ForgeSwitchSize.Medium,
    colors: ForgeSwitchColors = ForgeSwitchDefaults.colors(),
    accessibilityLabel: String? = null,
    accessibilityStateDescription: String? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .size(width = size.width, height = size.height)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = null,
                onValueChange = onCheckedChange,
            )
            .semantics {
                if (accessibilityLabel != null) {
                    contentDescription = accessibilityLabel
                }
                stateDescription = accessibilityStateDescription ?: if (checked) "On" else "Off"
                if (!enabled) {
                    disabled()
                }
            },
    ) {
        Canvas(modifier = Modifier.size(width = size.width, height = size.height)) {
            val trackHeight = this.size.height
            val trackWidth = this.size.width
            val radius = trackHeight / 2f
            val thumbRadius = size.thumbSize.toPx() / 2f
            val thumbPadding = size.thumbPadding.toPx()
            val minX = thumbPadding + thumbRadius
            val maxX = trackWidth - thumbPadding - thumbRadius
            val thumbX = if (checked) maxX else minX
            val thumbY = trackHeight / 2f

            drawRoundRect(
                color = colors.track(checked = checked, enabled = enabled, pressed = pressed),
                size = Size(width = trackWidth, height = trackHeight),
                cornerRadius = CornerRadius(radius, radius),
            )
            drawCircle(
                color = colors.thumb(checked = checked, enabled = enabled),
                radius = thumbRadius,
                center = Offset(thumbX, thumbY),
            )
        }
    }
}
