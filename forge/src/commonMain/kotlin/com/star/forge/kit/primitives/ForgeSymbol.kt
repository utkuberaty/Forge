package com.star.forge.kit.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.star.forge.kit.theme.ForgeTheme

/** Visual treatment for icon-backed Forge symbols. */
enum class ForgeSymbolVariant {
    /** Transparent, low-emphasis symbol. */
    Ghost,

    /** Main filled symbol. */
    Primary,

    /** Supporting tonal symbol. */
    Secondary,

    /** Transparent symbol with an outline. */
    Outline,

    /** Destructive or warning-adjacent symbol. */
    Danger,
}

/**
 * Size model for [ForgeSymbol].
 *
 * [containerSize] controls the symbol's visual/touch box and [iconSize]
 * controls the icon inside it.
 */
@Immutable
data class ForgeSymbolSize(
    val containerSize: Dp,
    val iconSize: Dp,
) {
    companion object {
        /** Icon-sized symbol with no extra container space. */
        val Icon: ForgeSymbolSize = ForgeSymbolSize(containerSize = 18.dp, iconSize = 18.dp)

        /** Compact symbol for dense inputs and tool rows. */
        val Small: ForgeSymbolSize = ForgeSymbolSize(containerSize = 28.dp, iconSize = 16.dp)

        /** Default standalone symbol size. */
        val Medium: ForgeSymbolSize = ForgeSymbolSize(containerSize = 36.dp, iconSize = 18.dp)

        /** Larger symbol for prominent tool actions. */
        val Large: ForgeSymbolSize = ForgeSymbolSize(containerSize = 44.dp, iconSize = 22.dp)
    }
}

/** Color state model for [ForgeSymbol]. */
@Immutable
data class ForgeSymbolColors(
    val container: Color,
    val content: Color,
    val pressedContainer: Color = container.copy(alpha = 0.86f),
    val disabledContainer: Color,
    val disabledContent: Color,
    val border: Color? = null,
    val pressedBorder: Color? = border,
    val disabledBorder: Color? = border?.copy(alpha = 0.36f),
) {
    fun container(enabled: Boolean, pressed: Boolean): Color = when {
        !enabled -> disabledContainer
        pressed -> pressedContainer
        else -> container
    }

    fun content(enabled: Boolean): Color = if (enabled) content else disabledContent

    fun border(enabled: Boolean, pressed: Boolean): Color? = when {
        !enabled -> disabledBorder
        pressed -> pressedBorder
        else -> border
    }
}

object ForgeSymbolDefaults {
    /** Default Forge-owned symbol colors for each [ForgeSymbolVariant]. */
    @Composable
    fun colors(variant: ForgeSymbolVariant): ForgeSymbolColors {
        val disabledContainer = ForgeTheme.colors.onSurface.copy(alpha = 0.10f)
        val disabledContent = ForgeTheme.colors.onSurface.copy(alpha = 0.38f)

        return when (variant) {
            ForgeSymbolVariant.Ghost -> ForgeSymbolColors(
                container = Color.Transparent,
                content = ForgeTheme.colors.onSurfaceVariant,
                pressedContainer = ForgeTheme.colors.onSurface.copy(alpha = 0.10f),
                disabledContainer = Color.Transparent,
                disabledContent = disabledContent,
            )

            ForgeSymbolVariant.Primary -> ForgeSymbolColors(
                container = ForgeTheme.colors.primary,
                content = ForgeTheme.colors.onPrimary,
                disabledContainer = disabledContainer,
                disabledContent = disabledContent,
            )

            ForgeSymbolVariant.Secondary -> ForgeSymbolColors(
                container = ForgeTheme.colors.secondaryContainer,
                content = ForgeTheme.colors.onSecondaryContainer,
                disabledContainer = disabledContainer,
                disabledContent = disabledContent,
            )

            ForgeSymbolVariant.Outline -> ForgeSymbolColors(
                container = Color.Transparent,
                content = ForgeTheme.colors.primary,
                pressedContainer = ForgeTheme.colors.primary.copy(alpha = 0.10f),
                disabledContainer = Color.Transparent,
                disabledContent = disabledContent,
                border = ForgeTheme.colors.borderStrong,
                pressedBorder = ForgeTheme.colors.primary,
            )

            ForgeSymbolVariant.Danger -> ForgeSymbolColors(
                container = ForgeTheme.colors.errorContainer,
                content = ForgeTheme.colors.error,
                pressedContainer = ForgeTheme.colors.errorContainer.copy(alpha = 0.86f),
                disabledContainer = disabledContainer,
                disabledContent = disabledContent,
            )
        }
    }
}

/**
 * Forge-owned symbol primitive for compact icon-backed visuals.
 *
 * Use symbols for field adornments, compact tool actions, badges, and repeated
 * UI affordances where the app needs direct control over icon, size, shape,
 * background, border, and optional click behavior.
 *
 * Use [ForgeIconSpec.contentDescription] for clickable symbols. Decorative
 * symbols should pass null so accessibility services skip the icon label.
 */
@Composable
fun ForgeSymbol(
    icon: ForgeIconSpec,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    variant: ForgeSymbolVariant = ForgeSymbolVariant.Ghost,
    size: ForgeSymbolSize = ForgeSymbolSize.Medium,
    shape: Shape = RoundedCornerShape(ForgeTheme.radii.sm),
    colors: ForgeSymbolColors = ForgeSymbolDefaults.colors(variant),
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val clickable = onClick != null
    val containerColor = colors.container(enabled = enabled, pressed = clickable && pressed)
    val contentColor = colors.content(enabled = enabled)
    val borderColor = colors.border(enabled = enabled, pressed = clickable && pressed)
    val border = borderColor?.let { BorderStroke(ForgeTheme.borders.thin, it) }

    Box(
        modifier = modifier
            .size(size.containerSize)
            .clip(shape)
            .background(containerColor, shape)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = enabled,
                        role = Role.Button,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            )
            .semantics(mergeDescendants = true) {
                if (clickable && !enabled) {
                    disabled()
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        ForgeIcon(
            spec = icon.copy(size = size.iconSize),
            modifier = Modifier.size(size.iconSize),
            tint = icon.tint.takeIfSpecified(contentColor),
        )
    }
}

private fun Color.takeIfSpecified(fallback: Color): Color =
    if (this == Color.Unspecified) fallback else this
