package com.star.forge.kit.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Corner radius scale used by Forge primitives.
 */
@Immutable
data class ForgeRadii(
    /** Square corners. */
    val none: Dp = 0.dp,

    /** Small rounding for tiny controls or dense containers. */
    val xs: Dp = 4.dp,

    /** Compact rounding for chips and small surfaces. */
    val sm: Dp = 8.dp,

    /** Default rounding for buttons, fields, and normal surfaces. */
    val md: Dp = 12.dp,

    /** Larger rounding for prominent containers. */
    val lg: Dp = 16.dp,

    /** Soft rounding for large panels and sheets. */
    val xl: Dp = 24.dp,

    /** Fully rounded controls such as pill buttons and search fields. */
    val pill: Dp = 999.dp,
)

/**
 * Forge-owned shape scale derived from [ForgeRadii].
 */
@Immutable
data class ForgeShapes(
    val extraSmall: Shape,
    val small: Shape,
    val medium: Shape,
    val large: Shape,
    val extraLarge: Shape,
) {
    companion object {
        /** Converts Forge radius tokens into Forge shape tokens. */
        fun from(radii: ForgeRadii): ForgeShapes = ForgeShapes(
            extraSmall = RoundedCornerShape(radii.xs),
            small = RoundedCornerShape(radii.sm),
            medium = RoundedCornerShape(radii.md),
            large = RoundedCornerShape(radii.lg),
            extraLarge = RoundedCornerShape(radii.xl),
        )
    }
}
