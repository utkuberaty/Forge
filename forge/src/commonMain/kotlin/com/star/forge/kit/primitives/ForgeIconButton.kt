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
import androidx.compose.runtime.CompositionLocalProvider
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
import com.star.forge.kit.theme.LocalForgeContentColor
import com.star.forge.kit.theme.ForgeTheme

/** Visual treatment for [ForgeIconButton]. */
enum class ForgeIconButtonVariant {
    /** Low-emphasis icon-only action. */
    Ghost,

    /** Main icon-only action. */
    Primary,

    /** Supporting filled icon-only action. */
    Tonal,

    /** Transparent icon-only action with an outline. */
    Outline,

    /** Destructive icon-only action. */
    Danger,
}

/** Touch target scale for [ForgeIconButton]. */
enum class ForgeIconButtonSize(
    val touchTarget: Dp,
    val iconSize: Dp,
) {
    Small(touchTarget = 36.dp, iconSize = 16.dp),
    Medium(touchTarget = 44.dp, iconSize = 20.dp),
    Large(touchTarget = 52.dp, iconSize = 24.dp),
}

/** Color state model for custom Forge icon buttons. */
@Immutable
data class ForgeIconButtonColors(
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

object ForgeIconButtonDefaults {
    /** Default Forge-owned icon button colors for each [ForgeIconButtonVariant]. */
    @Composable
    fun colors(variant: ForgeIconButtonVariant): ForgeIconButtonColors {
        val disabledContainer = ForgeTheme.colors.onSurface.copy(alpha = 0.10f)
        val disabledContent = ForgeTheme.colors.onSurface.copy(alpha = 0.38f)

        return when (variant) {
            ForgeIconButtonVariant.Ghost -> ForgeIconButtonColors(
                container = Color.Transparent,
                content = ForgeTheme.colors.onSurfaceVariant,
                pressedContainer = ForgeTheme.colors.onSurface.copy(alpha = 0.10f),
                disabledContainer = Color.Transparent,
                disabledContent = disabledContent,
            )

            ForgeIconButtonVariant.Primary -> ForgeIconButtonColors(
                container = ForgeTheme.colors.primary,
                content = ForgeTheme.colors.onPrimary,
                disabledContainer = disabledContainer,
                disabledContent = disabledContent,
            )

            ForgeIconButtonVariant.Tonal -> ForgeIconButtonColors(
                container = ForgeTheme.colors.secondaryContainer,
                content = ForgeTheme.colors.onSecondaryContainer,
                disabledContainer = disabledContainer,
                disabledContent = disabledContent,
            )

            ForgeIconButtonVariant.Outline -> ForgeIconButtonColors(
                container = Color.Transparent,
                content = ForgeTheme.colors.primary,
                pressedContainer = ForgeTheme.colors.primary.copy(alpha = 0.10f),
                disabledContainer = Color.Transparent,
                disabledContent = disabledContent,
                border = ForgeTheme.colors.borderStrong,
                pressedBorder = ForgeTheme.colors.primary,
            )

            ForgeIconButtonVariant.Danger -> ForgeIconButtonColors(
                container = ForgeTheme.colors.error,
                content = ForgeTheme.colors.onError,
                disabledContainer = disabledContainer,
                disabledContent = disabledContent,
            )
        }
    }
}

/**
 * Standard Forge icon button.
 *
 * This is a custom icon-only button. Pass icon props through [icon] so the
 * primitive can control sizing, tint, state colors, and accessibility.
 * Use [ForgeIconSpec.contentDescription] for the button label.
 */
@Composable
fun ForgeIconButton(
    onClick: () -> Unit,
    icon: ForgeIconSpec,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ForgeIconButtonVariant = ForgeIconButtonVariant.Ghost,
    size: ForgeIconButtonSize = ForgeIconButtonSize.Medium,
    iconSize: Dp = size.iconSize,
    shape: Shape = RoundedCornerShape(ForgeTheme.radii.md),
    colors: ForgeIconButtonColors = ForgeIconButtonDefaults.colors(variant),
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val containerColor = colors.container(enabled = enabled, pressed = pressed)
    val contentColor = colors.content(enabled = enabled)
    val borderColor = colors.border(enabled = enabled, pressed = pressed)
    val border = borderColor?.let { BorderStroke(ForgeTheme.borders.thin, it) }

    CompositionLocalProvider(LocalForgeContentColor provides contentColor) {
        Box(
            modifier = modifier
                .size(size.touchTarget)
                .clip(shape)
                .background(containerColor, shape)
                .then(if (border != null) Modifier.border(border, shape) else Modifier)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                )
                .semantics(mergeDescendants = true) {
                    if (!enabled) {
                        disabled()
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            ForgeIcon(
                spec = icon.copy(size = iconSize),
                modifier = Modifier.size(iconSize),
                tint = icon.tint.takeIfSpecified(contentColor),
            )
        }
    }
}

private fun Color.takeIfSpecified(fallback: Color): Color =
    if (this == Color.Unspecified) fallback else this
