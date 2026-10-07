package com.star.forge.kit.primitives

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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.star.forge.kit.theme.ForgeCheckboxSize
import com.star.forge.kit.theme.ForgeTheme

@Immutable
public data class ForgeCheckboxColors(
    public val checkedContainer: Color,
    public val uncheckedContainer: Color,
    public val checkedMark: Color,
    public val checkedBorder: Color,
    public val uncheckedBorder: Color,
    public val pressedContainer: Color,
    public val disabledContainer: Color,
    public val disabledMark: Color,
    public val disabledBorder: Color,
)

public object ForgeCheckboxDefaults {
    @Composable
    public fun colors(): ForgeCheckboxColors {
        val visual = ForgeTheme.components.checkbox.visuals
        return ForgeCheckboxColors(
            checkedContainer = visual.selectedContainer ?: ForgeTheme.colors.primary,
            uncheckedContainer = visual.unselectedContainer ?: Color.Transparent,
            checkedMark = visual.selectedContent ?: ForgeTheme.colors.onPrimary,
            checkedBorder = ForgeTheme.colors.primary,
            uncheckedBorder = visual.border ?: ForgeTheme.colors.borderStrong,
            pressedContainer = ForgeTheme.colors.primary.copy(alpha = visual.pressedOpacity ?: ForgeTheme.opacity.medium),
            disabledContainer = visual.disabledContainer ?: ForgeTheme.colors.onSurface.copy(alpha = ForgeTheme.opacity.disabledContainer),
            disabledMark = visual.disabledContent ?: ForgeTheme.colors.onSurface.copy(alpha = ForgeTheme.opacity.disabledContent),
            disabledBorder = ForgeTheme.colors.onSurface.copy(alpha = ForgeTheme.opacity.disabledContent),
        )
    }
}

@Composable
public fun ForgeCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ForgeCheckboxSize = ForgeTheme.components.checkbox.medium,
    colors: ForgeCheckboxColors = ForgeCheckboxDefaults.colors(),
    accessibilityLabel: String? = null,
    accessibilityStateDescription: String? = null,
) {
    require(size.visualSize.value > 0f && size.cornerRadius.value >= 0f && size.strokeWidth.value > 0f) {
        "checkbox dimensions must be valid"
    }
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val touchTarget = ForgeTheme.components.checkbox.minimumTouchTarget ?: ForgeTheme.touchTargets.minimum

    Box(
        modifier =
            modifier
                .defaultMinSize(minWidth = touchTarget, minHeight = touchTarget)
                .then(
                    if (onCheckedChange != null) {
                        Modifier.toggleable(
                            value = checked,
                            interactionSource = interactionSource,
                            indication = null,
                            enabled = enabled,
                            role = Role.Checkbox,
                            onValueChange = onCheckedChange,
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
            val strokeWidth = size.strokeWidth.toPx()
            val radius = size.cornerRadius.toPx()
            val canvasSize = Size(this.size.width, this.size.height)
            val container =
                when {
                    !enabled -> colors.disabledContainer
                    pressed -> colors.pressedContainer
                    checked -> colors.checkedContainer
                    else -> colors.uncheckedContainer
                }
            val border =
                when {
                    !enabled -> colors.disabledBorder
                    checked -> colors.checkedBorder
                    else -> colors.uncheckedBorder
                }
            drawRoundRect(container, size = canvasSize, cornerRadius = CornerRadius(radius, radius))
            drawRoundRect(
                color = border,
                topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
                size = Size(canvasSize.width - strokeWidth, canvasSize.height - strokeWidth),
                cornerRadius = CornerRadius(radius, radius),
                style = Stroke(strokeWidth),
            )
            if (checked) {
                val mark =
                    Path().apply {
                        moveTo(canvasSize.width * 0.25f, canvasSize.height * 0.52f)
                        lineTo(canvasSize.width * 0.43f, canvasSize.height * 0.70f)
                        lineTo(canvasSize.width * 0.76f, canvasSize.height * 0.32f)
                    }
                drawPath(mark, if (enabled) colors.checkedMark else colors.disabledMark, style = Stroke(strokeWidth, cap = StrokeCap.Round))
            }
        }
    }
}
