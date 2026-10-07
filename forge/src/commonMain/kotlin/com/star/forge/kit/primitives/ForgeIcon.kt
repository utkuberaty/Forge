package com.star.forge.kit.primitives

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp

/**
 * Source used by [ForgeIcon].
 *
 * Forge icons are intentionally owned by the kit/app layer instead of relying
 * on Material icon components. Use [Vector] for code-defined icons and [Painted]
 * for image/vector assets loaded by the app.
 */
@Immutable
public sealed interface ForgeIconSource {
    /** Code-defined vector icon. */
    @Immutable
    public data class Vector(
        public val imageVector: ImageVector,
    ) : ForgeIconSource

    /** Painter-backed icon, usually from app resources. */
    @Immutable
    public data class Painted(
        public val painter: Painter,
    ) : ForgeIconSource
}

/**
 * Props for rendering a Forge icon.
 *
 * Pass this into primitives such as [ForgeButton] and [ForgeIconButton] when an
 * action needs a leading, trailing, or icon-only visual.
 */
@Immutable
public data class ForgeIconSpec(
    public val source: ForgeIconSource,
    public val contentDescription: String? = null,
    public val size: Dp? = null,
    public val tint: Color = Color.Unspecified,
) {
    public companion object {
        /** Creates icon props from a code-defined [ImageVector]. */
        public fun vector(
            imageVector: ImageVector,
            contentDescription: String? = null,
            size: Dp? = null,
            tint: Color = Color.Unspecified,
        ): ForgeIconSpec =
            ForgeIconSpec(
                source = ForgeIconSource.Vector(imageVector),
                contentDescription = contentDescription,
                size = size,
                tint = tint,
            )

        /** Creates icon props from a [Painter], usually loaded from resources. */
        public fun painter(
            painter: Painter,
            contentDescription: String? = null,
            size: Dp? = null,
            tint: Color = Color.Unspecified,
        ): ForgeIconSpec =
            ForgeIconSpec(
                source = ForgeIconSource.Painted(painter),
                contentDescription = contentDescription,
                size = size,
                tint = tint,
            )
    }
}

/**
 * Custom Forge icon renderer.
 *
 * This uses Compose [Image] directly so the kit controls tint, sizing, and
 * resource shape without wrapping Material `Icon`.
 */
@Composable
public fun ForgeIcon(
    spec: ForgeIconSpec,
    modifier: Modifier = Modifier,
    tint: Color = spec.tint,
) {
    val resolvedSize = spec.size ?: com.star.forge.kit.theme.ForgeTheme.components.symbol.icon.iconSize
    require(resolvedSize.value > 0f) { "icon size must be positive" }
    val painter =
        when (val source = spec.source) {
            is ForgeIconSource.Vector -> rememberVectorPainter(source.imageVector)
            is ForgeIconSource.Painted -> source.painter
        }
    val colorFilter = if (tint.isSpecified) ColorFilter.tint(tint) else null

    Image(
        painter = painter,
        contentDescription = spec.contentDescription,
        modifier = modifier.size(resolvedSize),
        contentScale = ContentScale.Fit,
        colorFilter = colorFilter,
    )
}
