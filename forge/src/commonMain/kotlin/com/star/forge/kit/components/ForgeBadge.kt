package com.star.forge.kit.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.star.forge.kit.primitives.ForgeSurface
import com.star.forge.kit.primitives.ForgeText
import com.star.forge.kit.theme.ForgeTheme

/** Semantic visual treatment for a non-interactive badge. */
enum class ForgeBadgeTone { Neutral, Primary, Success }

/** Small metadata/status label. Actions should use buttons or other interactive primitives. */
@Composable
fun ForgeBadge(label: String, modifier: Modifier = Modifier, tone: ForgeBadgeTone = ForgeBadgeTone.Neutral) {
    val colors = ForgeTheme.colors
    val background = when (tone) {
        ForgeBadgeTone.Neutral -> colors.surfaceVariant
        ForgeBadgeTone.Primary -> colors.primaryContainer
        ForgeBadgeTone.Success -> colors.success.copy(alpha = 0.12f)
    }
    val foreground = when (tone) {
        ForgeBadgeTone.Neutral -> colors.onSurfaceVariant
        ForgeBadgeTone.Primary -> colors.onPrimaryContainer
        ForgeBadgeTone.Success -> colors.success
    }
    ForgeSurface(modifier, color = background, contentColor = foreground, shape = ForgeTheme.shapes.small, border = null) {
        ForgeText(
            label,
            Modifier.padding(horizontal = ForgeTheme.spacing.xs, vertical = ForgeTheme.spacing.xxs),
            style = ForgeTheme.typography.labelSmall,
        )
    }
}
