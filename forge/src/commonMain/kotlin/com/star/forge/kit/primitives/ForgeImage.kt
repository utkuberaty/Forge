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
data class ForgeImageSpec(
    val painter: Painter,
    val contentDescription: String? = null,
    val contentScale: ContentScale = ContentScale.Crop,
    val alpha: Float = 1f,
)

/**
 * Custom Forge image renderer.
 *
 * This wraps the low-level Compose [Image] primitive, not a Material component.
 */
@Composable
fun ForgeImage(
    spec: ForgeImageSpec,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = spec.painter,
        contentDescription = spec.contentDescription,
        modifier = modifier,
        contentScale = spec.contentScale,
        alpha = spec.alpha,
    )
}
