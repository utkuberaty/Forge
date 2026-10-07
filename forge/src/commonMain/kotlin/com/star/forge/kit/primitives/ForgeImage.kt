package com.star.forge.kit.primitives

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

/**
 * Props for rendering a Forge image.
 *
 * Use this for product, avatar, illustration, preview, or branded imagery so
 * image usage stays explicit and easy to standardize across apps.
 */
@Immutable
public data class ForgeImageSpec(
    public val painter: Painter,
    public val contentDescription: String? = null,
    public val contentScale: ContentScale = ContentScale.Crop,
    public val alpha: Float = 1f,
)

/**
 * Custom Forge image renderer.
 *
 * This wraps the low-level Compose [Image] primitive, not a Material component.
 */
@Composable
public fun ForgeImage(
    spec: ForgeImageSpec,
    modifier: Modifier = Modifier,
) {
    require(spec.alpha.isFinite() && spec.alpha in 0f..1f) { "image alpha must be a finite value from 0 to 1" }
    Image(
        painter = spec.painter,
        contentDescription = spec.contentDescription,
        modifier = modifier,
        contentScale = spec.contentScale,
        alpha = spec.alpha,
    )
}
