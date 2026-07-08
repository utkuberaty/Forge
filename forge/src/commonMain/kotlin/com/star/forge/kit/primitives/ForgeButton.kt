package com.star.forge.kit.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.star.forge.kit.theme.LocalForgeContentColor
import com.star.forge.kit.theme.ForgeTheme

/** Visual treatment for [ForgeButton]. */
enum class ForgeButtonVariant {
    /** Main action on the screen or in a section. */
    Primary,

    /** Supporting action with lower emphasis than [Primary]. */
    Secondary,

    /** Transparent button with a visible outline. */
    Outline,

    /** Text-style button for the lowest-emphasis action. */
    Ghost,

    /** Destructive, dangerous, or irreversible action. */
    Danger,
}

/** Size scale for [ForgeButton]. */
enum class ForgeButtonSize(
    /** Minimum touch/control height. */
    val minHeight: Dp,

    /** Horizontal inner padding. */
    val horizontalPadding: Dp,

    /** Vertical inner padding. */
    val verticalPadding: Dp,

    /** Default icon size for button icon slots. */
    val iconSize: Dp,
) {
    Small(minHeight = 36.dp, horizontalPadding = 12.dp, verticalPadding = 6.dp, iconSize = 16.dp),
    Medium(minHeight = 44.dp, horizontalPadding = 16.dp, verticalPadding = 8.dp, iconSize = 18.dp),
    Large(minHeight = 52.dp, horizontalPadding = 20.dp, verticalPadding = 10.dp, iconSize = 20.dp),
}

/**
 * Props for a button icon slot.
 *
 * Use [icon] for the visual and [spacing] to control the gap between the icon
 * and label. If [spacing] is null, Forge uses the active theme spacing.
 */
@Immutable
data class ForgeButtonIcon(
    val icon: ForgeIconSpec,
    val size: Dp? = null,
    val spacing: Dp? = null,
)

/**
 * Color state model for [ForgeButton].
 *
 * Forge owns these states directly instead of delegating them to Material
 * button defaults.
 */
@Immutable
data class ForgeButtonColors(
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

object ForgeButtonDefaults {
    /**
     * Default Forge-owned button colors for each [ForgeButtonVariant].
     */
    @Composable
    fun colors(variant: ForgeButtonVariant): ForgeButtonColors {
        val disabledContainer = ForgeTheme.colors.onSurface.copy(alpha = 0.10f)
        val disabledContent = ForgeTheme.colors.onSurface.copy(alpha = 0.38f)

        return when (variant) {
            ForgeButtonVariant.Primary -> ForgeButtonColors(
                container = ForgeTheme.colors.primary,
                content = ForgeTheme.colors.onPrimary,
                disabledContainer = disabledContainer,
                disabledContent = disabledContent,
            )

            ForgeButtonVariant.Secondary -> ForgeButtonColors(
                container = ForgeTheme.colors.secondaryContainer,
                content = ForgeTheme.colors.onSecondaryContainer,
                disabledContainer = disabledContainer,
                disabledContent = disabledContent,
            )

            ForgeButtonVariant.Outline -> ForgeButtonColors(
                container = Color.Transparent,
                content = ForgeTheme.colors.primary,
                pressedContainer = ForgeTheme.colors.primary.copy(alpha = 0.10f),
                disabledContainer = Color.Transparent,
                disabledContent = disabledContent,
                border = ForgeTheme.colors.borderStrong,
                pressedBorder = ForgeTheme.colors.primary,
            )

            ForgeButtonVariant.Ghost -> ForgeButtonColors(
                container = Color.Transparent,
                content = ForgeTheme.colors.primary,
                pressedContainer = ForgeTheme.colors.primary.copy(alpha = 0.10f),
                disabledContainer = Color.Transparent,
                disabledContent = disabledContent,
            )

            ForgeButtonVariant.Danger -> ForgeButtonColors(
                container = ForgeTheme.colors.error,
                content = ForgeTheme.colors.onError,
                disabledContainer = disabledContainer,
                disabledContent = disabledContent,
            )
        }
    }
}

/**
 * Standard Forge button.
 *
 * This is a custom button implementation: Forge owns layout, shape, border,
 * state colors, and icon slots instead of wrapping Material button components.
 *
 * @param accessibilityLabel optional label for icon-heavy buttons. Text-only
 * buttons can rely on their visible text.
 */
@Composable
fun ForgeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ForgeButtonVariant = ForgeButtonVariant.Primary,
    size: ForgeButtonSize = ForgeButtonSize.Medium,
    shape: Shape = RoundedCornerShape(ForgeTheme.radii.md),
    colors: ForgeButtonColors = ForgeButtonDefaults.colors(variant),
    leadingIcon: ForgeButtonIcon? = null,
    trailingIcon: ForgeButtonIcon? = null,
    accessibilityLabel: String? = null,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = size.horizontalPadding,
        vertical = size.verticalPadding,
    ),
    content: @Composable RowScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val containerColor = colors.container(enabled = enabled, pressed = pressed)
    val contentColor = colors.content(enabled = enabled)
    val borderColor = colors.border(enabled = enabled, pressed = pressed)
    val border = borderColor?.let { BorderStroke(ForgeTheme.borders.thin, it) }

    CompositionLocalProvider(LocalForgeContentColor provides contentColor) {
        Row(
            modifier = modifier
                .defaultMinSize(minHeight = size.minHeight)
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
                    if (accessibilityLabel != null) {
                        contentDescription = accessibilityLabel
                    }
                    if (!enabled) {
                        disabled()
                    }
                }
                .padding(contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ForgeButtonIconSlot(
                icon = leadingIcon,
                fallbackSize = size.iconSize,
                contentColor = contentColor,
            )

            content()

            ForgeButtonIconSlot(
                icon = trailingIcon,
                fallbackSize = size.iconSize,
                contentColor = contentColor,
                leading = false,
            )
        }
    }
}

@Composable
private fun RowScope.ForgeButtonIconSlot(
    icon: ForgeButtonIcon?,
    fallbackSize: Dp,
    contentColor: Color,
    leading: Boolean = true,
) {
    if (icon == null) return

    if (leading) {
        val iconSize = icon.size ?: fallbackSize
        ForgeIcon(
            spec = icon.icon.copy(size = iconSize),
            modifier = Modifier.size(iconSize),
            tint = icon.icon.tint.takeIfSpecified(contentColor),
        )
        Spacer(Modifier.width(icon.spacing ?: ForgeTheme.spacing.xs))
    } else {
        val iconSize = icon.size ?: fallbackSize
        Spacer(Modifier.width(icon.spacing ?: ForgeTheme.spacing.xs))
        ForgeIcon(
            spec = icon.icon.copy(size = iconSize),
            modifier = Modifier.size(iconSize),
            tint = icon.icon.tint.takeIfSpecified(contentColor),
        )
    }
}

private fun Color.takeIfSpecified(fallback: Color): Color =
    if (this == Color.Unspecified) fallback else this
