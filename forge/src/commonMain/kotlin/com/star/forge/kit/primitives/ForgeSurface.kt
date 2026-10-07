package com.star.forge.kit.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import com.star.forge.kit.theme.ForgeTheme
import com.star.forge.kit.theme.LocalForgeContentColor

/**
 * Forge-owned surface primitive.
 *
 * Use this for containers that should inherit Forge border, shape, color, and
 * content-color defaults without wrapping Material `Surface`.
 */
@Composable
public fun ForgeSurface(
    modifier: Modifier = Modifier,
    shape: Shape = ForgeTheme.shapes.medium,
    color: Color = ForgeTheme.colors.surface,
    contentColor: Color = ForgeTheme.colors.onSurface,
    border: BorderStroke? =
        BorderStroke(
            width = ForgeTheme.borders.thin,
            color = ForgeTheme.colors.border,
        ),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalForgeContentColor provides contentColor) {
        Box(
            modifier =
                modifier
                    .clip(shape)
                    .background(color, shape)
                    .then(if (border != null) Modifier.border(border, shape) else Modifier),
        ) {
            content()
        }
    }
}

/**
 * Interactive surface for cards and rows with one primary action. Exposes button
 * and disabled semantics, clips interaction to [shape], and uses [pressedColor]
 * for feedback. Keep nested actions outside this surface.
 */
@Composable
public fun ForgeSurface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ForgeTheme.shapes.medium,
    color: Color = ForgeTheme.colors.surface,
    contentColor: Color = ForgeTheme.colors.onSurface,
    border: BorderStroke? = BorderStroke(ForgeTheme.borders.thin, ForgeTheme.colors.border),
    pressedColor: Color = ForgeTheme.colors.surfaceVariant,
    accessibilityLabel: String? = null,
    content: @Composable () -> Unit,
) {
    val interactions = remember { MutableInteractionSource() }
    val pressed by interactions.collectIsPressedAsState()
    ForgeSurface(
        modifier =
            modifier
                .defaultMinSize(
                    minWidth = ForgeTheme.touchTargets.minimum,
                    minHeight = ForgeTheme.touchTargets.minimum,
                ).clip(shape)
                .clickable(
                    interactionSource = interactions,
                    indication = null,
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                ).semantics(mergeDescendants = true) {
                    if (accessibilityLabel != null) contentDescription = accessibilityLabel
                    if (!enabled) disabled()
                },
        shape = shape,
        color = if (pressed && enabled) pressedColor else color,
        contentColor = if (enabled) contentColor else contentColor.copy(alpha = ForgeTheme.opacity.disabledContent),
        border = border,
        content = content,
    )
}
