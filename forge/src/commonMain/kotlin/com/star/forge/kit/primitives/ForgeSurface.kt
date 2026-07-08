package com.star.forge.kit.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.star.forge.kit.theme.LocalForgeContentColor
import com.star.forge.kit.theme.ForgeTheme

/**
 * Forge-owned surface primitive.
 *
 * Use this for containers that should inherit Forge border, shape, color, and
 * content-color defaults without wrapping Material `Surface`.
 */
@Composable
fun ForgeSurface(
    modifier: Modifier = Modifier,
    shape: Shape = ForgeTheme.shapes.medium,
    color: Color = ForgeTheme.colors.surface,
    contentColor: Color = ForgeTheme.colors.onSurface,
    border: BorderStroke? = BorderStroke(
        width = ForgeTheme.borders.thin,
        color = ForgeTheme.colors.border,
    ),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalForgeContentColor provides contentColor) {
        Box(
            modifier = modifier
                .clip(shape)
                .background(color, shape)
                .then(if (border != null) Modifier.border(border, shape) else Modifier),
        ) {
            content()
        }
    }
}
