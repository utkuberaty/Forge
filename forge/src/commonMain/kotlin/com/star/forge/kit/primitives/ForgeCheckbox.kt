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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.star.forge.kit.theme.ForgeTheme

/** Size scale for [ForgeCheckbox]. */
enum class ForgeCheckboxSize(
    val touchTarget: Dp,
    val boxSize: Dp,
    val cornerRadius: Dp,
    val strokeWidth: Dp,
) {
    Small(touchTarget = 36.dp, boxSize = 18.dp, cornerRadius = 5.dp, strokeWidth = 2.dp),
    Medium(touchTarget = 40.dp, boxSize = 22.dp, cornerRadius = 6.dp, strokeWidth = 2.dp),
    Large(touchTarget = 48.dp, boxSize = 26.dp, cornerRadius = 7.dp, strokeWidth = 2.5.dp),
}

/** Color state model for [ForgeCheckbox]. */
@Immutable
data class ForgeCheckboxColors(
    val checkedContainer: Color,
    val uncheckedContainer: Color,
    val checkedMark: Color,
    val checkedBorder: Color,
    val uncheckedBorder: Color,
    val pressedContainer: Color,
    val disabledContainer: Color,
    val disabledMark: Color,
    val disabledBorder: Color,
) {
    fun container(checked: Boolean, enabled: Boolean, pressed: Boolean): Color = when {
        !enabled -> disabledContainer
        pressed -> pressedContainer
        checked -> checkedContainer
        else -> uncheckedContainer
    }

    fun border(checked: Boolean, enabled: Boolean): Color = when {
        !enabled -> disabledBorder
        checked -> checkedBorder
        else -> uncheckedBorder
    }

    fun mark(enabled: Boolean): Color = if (enabled) checkedMark else disabledMark
}

object ForgeCheckboxDefaults {
    /** Default Forge-owned checkbox colors. */
    @Composable
    fun colors(): ForgeCheckboxColors = ForgeCheckboxColors(
        checkedContainer = ForgeTheme.colors.primary,
        uncheckedContainer = Color.Transparent,
        checkedMark = ForgeTheme.colors.onPrimary,
        checkedBorder = ForgeTheme.colors.primary,
        uncheckedBorder = ForgeTheme.colors.borderStrong,
        pressedContainer = ForgeTheme.colors.primary.copy(alpha = 0.18f),
        disabledContainer = ForgeTheme.colors.onSurface.copy(alpha = 0.08f),
        disabledMark = ForgeTheme.colors.onSurface.copy(alpha = 0.34f),
        disabledBorder = ForgeTheme.colors.onSurface.copy(alpha = 0.24f),
    )
}

/**
 * Forge-owned checkbox.
 *
 * Drawn directly with Compose canvas so the kit controls every visual state
 * instead of wrapping Material checkbox behavior.
 *
 * @param accessibilityLabel optional label when the checkbox is not paired
 * with visible text in the same accessible row.
 */
@Composable
fun ForgeCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ForgeCheckboxSize = ForgeCheckboxSize.Medium,
    colors: ForgeCheckboxColors = ForgeCheckboxDefaults.colors(),
    accessibilityLabel: String? = null,
    accessibilityStateDescription: String? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .size(size.touchTarget)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Checkbox,
                interactionSource = interactionSource,
                indication = null,
                onValueChange = onCheckedChange,
            )
            .semantics {
                if (accessibilityLabel != null) {
                    contentDescription = accessibilityLabel
                }
                stateDescription = accessibilityStateDescription ?: if (checked) "Checked" else "Unchecked"
                if (!enabled) {
                    disabled()
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(size.boxSize)) {
            val strokeWidth = size.strokeWidth.toPx()
            val radius = size.cornerRadius.toPx()
            val canvasSize = Size(this.size.width, this.size.height)
            val borderInset = strokeWidth / 2f

            drawRoundRect(
                color = colors.container(checked = checked, enabled = enabled, pressed = pressed),
                size = canvasSize,
                cornerRadius = CornerRadius(radius, radius),
            )
            drawRoundRect(
                color = colors.border(checked = checked, enabled = enabled),
                topLeft = Offset(borderInset, borderInset),
                size = Size(
                    width = canvasSize.width - strokeWidth,
                    height = canvasSize.height - strokeWidth,
                ),
                cornerRadius = CornerRadius(radius, radius),
                style = Stroke(width = strokeWidth),
            )

            if (checked) {
                val checkPath = Path().apply {
                    moveTo(canvasSize.width * 0.25f, canvasSize.height * 0.52f)
                    lineTo(canvasSize.width * 0.43f, canvasSize.height * 0.70f)
                    lineTo(canvasSize.width * 0.76f, canvasSize.height * 0.32f)
                }
                drawPath(
                    path = checkPath,
                    color = colors.mark(enabled),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )
            }
        }
    }
}
