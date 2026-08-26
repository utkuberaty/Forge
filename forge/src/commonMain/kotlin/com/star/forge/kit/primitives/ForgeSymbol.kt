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
import com.star.forge.kit.theme.ForgeSymbolSize
import com.star.forge.kit.theme.ForgeTheme

public enum class ForgeSymbolVariant { Ghost, Primary, Secondary, Outline, Danger }

@Immutable
public data class ForgeSymbolColors(
    public val container: Color,
    public val content: Color,
    public val pressedContainer: Color,
    public val disabledContainer: Color,
    public val disabledContent: Color,
    public val border: Color? = null,
    public val pressedBorder: Color? = border,
    public val disabledBorder: Color? = border,
)

public object ForgeSymbolDefaults {
    @Composable
    public fun colors(variant: ForgeSymbolVariant): ForgeSymbolColors {
        val opacity = ForgeTheme.opacity
        val visual = ForgeTheme.components.symbol.visuals
        val disabledContainer = ForgeTheme.colors.onSurface.copy(alpha = opacity.disabledContainer)
        val disabledContent = ForgeTheme.colors.onSurface.copy(alpha = opacity.disabledContent)
        val base =
            when (variant) {
                ForgeSymbolVariant.Ghost ->
                    ForgeSymbolColors(
                        Color.Transparent,
                        ForgeTheme.colors.onSurfaceVariant,
                        ForgeTheme.colors.onSurface.copy(alpha = opacity.subtle),
                        Color.Transparent,
                        disabledContent,
                    )
                ForgeSymbolVariant.Primary ->
                    ForgeSymbolColors(
                        ForgeTheme.colors.primary,
                        ForgeTheme.colors.onPrimary,
                        ForgeTheme.colors.primary.copy(alpha = opacity.pressed),
                        disabledContainer,
                        disabledContent,
                    )
                ForgeSymbolVariant.Secondary ->
                    ForgeSymbolColors(
                        ForgeTheme.colors.secondaryContainer,
                        ForgeTheme.colors.onSecondaryContainer,
                        ForgeTheme.colors.secondaryContainer.copy(alpha = opacity.pressed),
                        disabledContainer,
                        disabledContent,
                    )
                ForgeSymbolVariant.Outline ->
                    ForgeSymbolColors(
                        Color.Transparent,
                        ForgeTheme.colors.primary,
                        ForgeTheme.colors.primary.copy(alpha = opacity.subtle),
                        Color.Transparent,
                        disabledContent,
                        ForgeTheme.colors.borderStrong,
                        ForgeTheme.colors.primary,
                        ForgeTheme.colors.borderStrong.copy(alpha = opacity.disabledContent),
                    )
                ForgeSymbolVariant.Danger ->
                    ForgeSymbolColors(
                        ForgeTheme.colors.errorContainer,
                        ForgeTheme.colors.error,
                        ForgeTheme.colors.errorContainer.copy(alpha = opacity.pressed),
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
public fun ForgeSymbol(
    icon: ForgeIconSpec,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    accessibilityLabel: String? = icon.contentDescription,
    variant: ForgeSymbolVariant = ForgeSymbolVariant.Ghost,
    size: ForgeSymbolSize = ForgeTheme.components.symbol.medium,
    shape: Shape = RoundedCornerShape(ForgeTheme.radii.sm),
    colors: ForgeSymbolColors = ForgeSymbolDefaults.colors(variant),
) {
    require(onClick == null || !accessibilityLabel.isNullOrBlank()) {
        "Clickable ForgeSymbol requires an accessibilityLabel."
    }
    require(size.visualSize.value > 0f && size.iconSize.value > 0f && size.iconSize <= size.visualSize) {
        "symbol dimensions must be positive and the icon must fit its visual size"
    }
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val clickable = onClick != null
    val container =
        when {
            !enabled -> colors.disabledContainer
            clickable && pressed -> colors.pressedContainer
            else -> colors.container
        }
    val content = if (enabled) colors.content else colors.disabledContent
    val borderColor =
        when {
            !enabled -> colors.disabledBorder
            clickable && pressed -> colors.pressedBorder
            else -> colors.border
        }
    val border = borderColor?.let { BorderStroke(ForgeTheme.borders.thin, it) }
    val touchTarget = ForgeTheme.components.symbol.minimumTouchTarget ?: ForgeTheme.touchTargets.minimum

    Box(
        modifier =
            modifier
                .then(if (clickable) Modifier.defaultMinSize(minWidth = touchTarget, minHeight = touchTarget) else Modifier)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(interactionSource, indication = null, enabled = enabled, role = Role.Button, onClick = onClick)
                    } else {
                        Modifier
                    },
                ).semantics(mergeDescendants = true) {
                    accessibilityLabel?.let { contentDescription = it }
                    if (clickable && !enabled) disabled()
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
