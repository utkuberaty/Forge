package com.star.forge.kit.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacing scale used by Forge primitives.
 *
 * Apps can replace this in [ForgeKitTheme] when they need a denser or more
 * spacious interface while keeping the same token names.
 */
@Immutable
public data class ForgeSpacing(
    /** No spacing. Useful when a primitive needs to explicitly remove a gap. */
    public val zero: Dp = 0.dp,
    /** Tiny optical adjustment, usually between an icon and a nearby edge. */
    public val nano: Dp = 2.dp,
    /** Smallest normal gap for compact controls. */
    public val xxs: Dp = 4.dp,
    /** Tight gap between related inline elements. */
    public val xs: Dp = 8.dp,
    /** Small gap inside compact rows, fields, and buttons. */
    public val sm: Dp = 12.dp,
    /** Default content padding and spacing between close groups. */
    public val md: Dp = 16.dp,
    /** Comfortable gap between independent elements in a screen section. */
    public val lg: Dp = 24.dp,
    /** Large section gap. */
    public val xl: Dp = 32.dp,
    /** Extra-large page or panel gap. */
    public val xxl: Dp = 48.dp,
    /** Largest standard gap for strong separation between page areas. */
    public val xxxl: Dp = 64.dp,
)
