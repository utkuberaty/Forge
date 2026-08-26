package com.star.forge.kit.primitives

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.star.forge.kit.theme.ForgeRadioButtonSize
import com.star.forge.kit.theme.ForgeTheme

/** Colors used by [ForgeRadioButton]. */
@Immutable
public data class ForgeRadioButtonColors(
    public val selected: Color,
    public val unselected: Color,
    public val pressed: Color,
    public val disabled: Color,
)

/** Defaults for [ForgeRadioButton]. */
public object ForgeRadioButtonDefaults {
    @Composable
    public fun colors(): ForgeRadioButtonColors {
        val visual = ForgeTheme.components.radioButton.visuals
        return ForgeRadioButtonColors(
            selected = visual.selectedContent ?: ForgeTheme.colors.primary,
            unselected = visual.unselectedContent ?: visual.border ?: ForgeTheme.colors.borderStrong,
            pressed = ForgeTheme.colors.primary.copy(alpha = visual.pressedOpacity ?: ForgeTheme.opacity.medium),
            disabled = visual.disabledContent ?: ForgeTheme.colors.onSurface.copy(alpha = ForgeTheme.opacity.disabledContent),
        )
    }
}

/** Forge-owned radio control with a visual size independent from its touch target. */
@Composable
public fun ForgeRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ForgeRadioButtonSize = ForgeTheme.components.radioButton.medium,
    colors: ForgeRadioButtonColors = ForgeRadioButtonDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    accessibilityLabel: String? = null,
    accessibilityStateDescription: String? = null,
) {
    require(size.visualSize.value > 0f && size.dotSize.value > 0f && size.strokeWidth.value > 0f) {
        "radio dimensions must be positive"
    }
    require(size.dotSize <= size.visualSize) { "radio dot must fit inside its visual size" }
    val pressed by interactionSource.collectIsPressedAsState()
    val touchTarget = ForgeTheme.components.radioButton.minimumTouchTarget ?: ForgeTheme.touchTargets.minimum
    val color =
        when {
            !enabled -> colors.disabled
            pressed -> colors.pressed
            selected -> colors.selected
            else -> colors.unselected
        }

    Box(
        modifier =
            modifier
                .defaultMinSize(minWidth = touchTarget, minHeight = touchTarget)
                .then(
                    if (onClick != null) {
                        Modifier.selectable(
                            selected = selected,
                            interactionSource = interactionSource,
                            indication = null,
                            enabled = enabled,
                            role = Role.RadioButton,
                            onClick = onClick,
                        )
                    } else {
                        Modifier
                    },
                ).semantics {
                    accessibilityLabel?.let { contentDescription = it }
                    accessibilityStateDescription?.let { stateDescription = it }
                    if (!enabled) disabled()
                },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.size(size.visualSize)) {
            drawCircle(
                color = color,
                radius = (this.size.minDimension - size.strokeWidth.toPx()) / 2f,
                style = Stroke(size.strokeWidth.toPx()),
            )
            if (selected) drawCircle(color, radius = size.dotSize.toPx() / 2f)
        }
    }
}
