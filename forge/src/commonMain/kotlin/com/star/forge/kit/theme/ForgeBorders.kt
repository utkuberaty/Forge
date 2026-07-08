package com.star.forge.kit.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Border width scale used for dividers, outlines, and selected states.
 */
@Immutable
data class ForgeBorders(
    /** Very subtle separator for high-density UI. */
    val hairline: Dp = 0.5.dp,

    /** Default border and divider width. */
    val thin: Dp = 1.dp,

    /** Stronger border for active, focused, or emphasized controls. */
    val medium: Dp = 1.5.dp,

    /** Heavy border for rare emphasis or debug/selection overlays. */
    val thick: Dp = 2.dp,
) {
    /**
     * Creates a [BorderStroke] using Forge border tokens.
     *
     * By default this uses [ForgeColors.border], the standard subtle
     * outline color for surfaces, inputs, and dividers.
     */
    @Composable
    fun stroke(
        width: Dp = thin,
        color: Color = ForgeTheme.colors.border,
    ): BorderStroke = BorderStroke(width, color)
}
