package com.star.forge.kit.primitives

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Source used by [ForgeIcon].
 *
 * Forge icons are intentionally owned by the kit/app layer instead of relying
 * on Material icon components. Use [Vector] for code-defined icons and [Painted]
 * for image/vector assets loaded by the app.
 */
@Immutable
sealed interface ForgeIconSource {
    /** Code-defined vector icon. */
    @Immutable
    data class Vector(val imageVector: ImageVector) : ForgeIconSource

    /** Painter-backed icon, usually from app resources. */
    @Immutable
    data class Painted(val painter: Painter) : ForgeIconSource
}

/**
 * Props for rendering a Forge icon.
 *
 * Pass this into primitives such as [ForgeButton] and [ForgeIconButton] when an
 * action needs a leading, trailing, or icon-only visual.
 */
@Immutable
data class ForgeIconSpec(
    val source: ForgeIconSource,
    val contentDescription: String? = null,
    val size: Dp = 18.dp,
    val tint: Color = Color.Unspecified,
) {
    companion object {
        /** Creates icon props from a code-defined [ImageVector]. */
        fun vector(
            imageVector: ImageVector,
            contentDescription: String? = null,
            size: Dp = 18.dp,
            tint: Color = Color.Unspecified,
        ): ForgeIconSpec = ForgeIconSpec(
            source = ForgeIconSource.Vector(imageVector),
            contentDescription = contentDescription,
            size = size,
            tint = tint,
        )

        /** Creates icon props from a [Painter], usually loaded from resources. */
        fun painter(
            painter: Painter,
            contentDescription: String? = null,
            size: Dp = 18.dp,
            tint: Color = Color.Unspecified,
        ): ForgeIconSpec = ForgeIconSpec(
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
fun ForgeIcon(
    spec: ForgeIconSpec,
    modifier: Modifier = Modifier,
    tint: Color = spec.tint,
) {
    val painter = when (val source = spec.source) {
        is ForgeIconSource.Vector -> rememberVectorPainter(source.imageVector)
        is ForgeIconSource.Painted -> source.painter
    }
    val colorFilter = if (tint.isSpecified) ColorFilter.tint(tint) else null

    Image(
        painter = painter,
        contentDescription = spec.contentDescription,
        modifier = modifier.size(spec.size),
        contentScale = ContentScale.Fit,
        colorFilter = colorFilter,
    )
}
