package com.star.forge.kit.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import com.star.forge.kit.theme.ForgeIconButtonSize
import com.star.forge.kit.theme.ForgeTheme

public enum class ForgeIconButtonVariant { Ghost, Primary, Tonal, Outline, Danger }

@Immutable
public data class ForgeIconButtonColors(
    public val container: Color,
    public val content: Color,
    public val pressedContainer: Color,
    public val disabledContainer: Color,
    public val disabledContent: Color,
    public val border: Color? = null,
    public val pressedBorder: Color? = border,
    public val disabledBorder: Color? = border,
) {
    public fun container(
        enabled: Boolean,
        pressed: Boolean,
    ): Color =
        when {
            !enabled -> disabledContainer
            pressed -> pressedContainer
            else -> container
        }

    public fun content(enabled: Boolean): Color = if (enabled) content else disabledContent

    public fun border(
        enabled: Boolean,
        pressed: Boolean,
    ): Color? =
        when {
            !enabled -> disabledBorder
            pressed -> pressedBorder
            else -> border
        }
}

public object ForgeIconButtonDefaults {
    @Composable
    public fun colors(variant: ForgeIconButtonVariant): ForgeIconButtonColors {
        val opacity = ForgeTheme.opacity
        val visual = ForgeTheme.components.iconButton.visuals
        val disabledContainer = ForgeTheme.colors.onSurface.copy(alpha = opacity.disabledContainer)
        val disabledContent = ForgeTheme.colors.onSurface.copy(alpha = opacity.disabledContent)
        val base =
            when (variant) {
                ForgeIconButtonVariant.Ghost ->
                    ForgeIconButtonColors(
                        Color.Transparent,
                        ForgeTheme.colors.onSurfaceVariant,
                        ForgeTheme.colors.onSurface.copy(alpha = opacity.subtle),
                        Color.Transparent,
                        disabledContent,
                    )
                ForgeIconButtonVariant.Primary ->
                    ForgeIconButtonColors(
                        ForgeTheme.colors.primary,
                        ForgeTheme.colors.onPrimary,
                        ForgeTheme.colors.primary.copy(alpha = opacity.pressed),
                        disabledContainer,
                        disabledContent,
                    )
                ForgeIconButtonVariant.Tonal ->
                    ForgeIconButtonColors(
                        ForgeTheme.colors.secondaryContainer,
                        ForgeTheme.colors.onSecondaryContainer,
                        ForgeTheme.colors.secondaryContainer.copy(alpha = opacity.pressed),
                        disabledContainer,
                        disabledContent,
                    )
                ForgeIconButtonVariant.Outline ->
                    ForgeIconButtonColors(
                        Color.Transparent,
                        ForgeTheme.colors.primary,
                        ForgeTheme.colors.primary.copy(alpha = opacity.subtle),
                        Color.Transparent,
                        disabledContent,
                        ForgeTheme.colors.borderStrong,
                        ForgeTheme.colors.primary,
                        ForgeTheme.colors.borderStrong.copy(alpha = opacity.disabledContent),
                    )
                ForgeIconButtonVariant.Danger ->
                    ForgeIconButtonColors(
                        ForgeTheme.colors.error,
                        ForgeTheme.colors.onError,
                        ForgeTheme.colors.error.copy(alpha = opacity.pressed),
                        disabledContainer,
                        disabledContent,
                    )
            }
        return base.copy(
            container = visual.container ?: base.container,
            content = visual.content ?: base.content,
            disabledContainer = visual.disabledContainer ?: base.disabledContainer,
            disabledContent = visual.disabledContent ?: base.disabledContent,
            border = visual.border ?: base.border,
        )
    }
}

@Composable
public fun ForgeIconButton(
    onClick: () -> Unit,
    icon: ForgeIconSpec,
    accessibilityLabel: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ForgeIconButtonVariant = ForgeIconButtonVariant.Ghost,
    size: ForgeIconButtonSize = ForgeTheme.components.iconButton.medium,
    shape: Shape =
        RoundedCornerShape(
            ForgeTheme.components.iconButton.visuals.cornerRadius
                ?: ForgeTheme.components.iconButton.cornerRadius
                ?: ForgeTheme.radii.md,
        ),
    colors: ForgeIconButtonColors = ForgeIconButtonDefaults.colors(variant),
) {
    require(accessibilityLabel.isNotBlank()) { "icon-only actions require a meaningful accessibilityLabel" }
    require(size.visualSize.value > 0f && size.iconSize.value > 0f && size.iconSize <= size.visualSize) {
        "icon button dimensions must be positive and the icon must fit its visual size"
    }
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val touchTarget = ForgeTheme.components.iconButton.minimumTouchTarget ?: ForgeTheme.touchTargets.minimum
    val container = colors.container(enabled, pressed)
    val content = colors.content(enabled)
    val borderColor = colors.border(enabled, pressed)
    val border = borderColor?.let { BorderStroke(ForgeTheme.borders.thin, it) }

    Box(
        modifier =
            modifier
                .defaultMinSize(minWidth = touchTarget, minHeight = touchTarget)
                .clickable(interactionSource, indication = null, enabled = enabled, role = Role.Button, onClick = onClick)
                .semantics(mergeDescendants = true) {
                    contentDescription = accessibilityLabel
                    if (!enabled) disabled()
                },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(size.visualSize)
                    .clip(shape)
                    .background(container, shape)
                    .then(if (border != null) Modifier.border(border, shape) else Modifier),
            contentAlignment = Alignment.Center,
        ) {
            ForgeIcon(
                spec = icon.copy(contentDescription = null, size = size.iconSize),
                modifier = Modifier.size(size.iconSize),
                tint = icon.tint.takeIfSpecified(content),
            )
        }
    }
}

private fun Color.takeIfSpecified(fallback: Color): Color = if (this == Color.Unspecified) fallback else this
