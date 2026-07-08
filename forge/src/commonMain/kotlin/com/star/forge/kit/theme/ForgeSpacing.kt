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
data class ForgeSpacing(
    /** No spacing. Useful when a primitive needs to explicitly remove a gap. */
    val zero: Dp = 0.dp,

    /** Tiny optical adjustment, usually between an icon and a nearby edge. */
    val nano: Dp = 2.dp,

    /** Smallest normal gap for compact controls. */
    val xxs: Dp = 4.dp,

    /** Tight gap between related inline elements. */
    val xs: Dp = 8.dp,

    /** Small gap inside compact rows, fields, and buttons. */
    val sm: Dp = 12.dp,

    /** Default content padding and spacing between close groups. */
    val md: Dp = 16.dp,

    /** Comfortable gap between independent elements in a screen section. */
    val lg: Dp = 24.dp,

    /** Large section gap. */
    val xl: Dp = 32.dp,

    /** Extra-large page or panel gap. */
    val xxl: Dp = 48.dp,

    /** Largest standard gap for strong separation between page areas. */
    val xxxl: Dp = 64.dp,
)
