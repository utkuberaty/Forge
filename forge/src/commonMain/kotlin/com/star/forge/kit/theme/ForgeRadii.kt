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
public data class ForgeRadii(
    /** Square corners. */
    public val none: Dp = 0.dp,
    /** Small rounding for tiny controls or dense containers. */
    public val xs: Dp = 4.dp,
    /** Compact rounding for chips and small surfaces. */
    public val sm: Dp = 8.dp,
    /** Default rounding for buttons, fields, and normal surfaces. */
    public val md: Dp = 12.dp,
    /** Larger rounding for prominent containers. */
    public val lg: Dp = 16.dp,
    /** Soft rounding for large panels and sheets. */
    public val xl: Dp = 24.dp,
    /** Fully rounded controls such as pill buttons and search fields. */
    public val pill: Dp = 999.dp,
)

/**
 * Forge-owned shape scale derived from [ForgeRadii].
 */
@Immutable
public data class ForgeShapes(
    public val extraSmall: Shape,
    public val small: Shape,
    public val medium: Shape,
    public val large: Shape,
    public val extraLarge: Shape,
) {
    public companion object {
        /** Converts Forge radius tokens into Forge shape tokens. */
        public fun from(radii: ForgeRadii): ForgeShapes =
            ForgeShapes(
                extraSmall = RoundedCornerShape(radii.xs),
                small = RoundedCornerShape(radii.sm),
                medium = RoundedCornerShape(radii.md),
                large = RoundedCornerShape(radii.lg),
                extraLarge = RoundedCornerShape(radii.xl),
            )
    }
}
