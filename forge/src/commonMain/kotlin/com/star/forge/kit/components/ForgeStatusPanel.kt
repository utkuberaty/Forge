package com.star.forge.kit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import com.star.forge.kit.primitives.ForgeButton
import com.star.forge.kit.primitives.ForgeButtonVariant
import com.star.forge.kit.primitives.ForgeProgressIndicator
import com.star.forge.kit.primitives.ForgeSurface
import com.star.forge.kit.primitives.ForgeText
import com.star.forge.kit.theme.ForgeTheme

/** Semantic color used by a status panel. Product wording belongs to its caller. */
enum class ForgeStatusTone { Neutral, Error }

/**
 * A configurable loading, empty, or error panel using Forge primitives and tokens.
 * [actionLabel] and [onAction] must be supplied together. Status changes are announced
 * through a polite live region, and [loading] adds indeterminate progress semantics.
 */
@Composable
fun ForgeStatusPanel(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    tone: ForgeStatusTone = ForgeStatusTone.Neutral,
    loading: Boolean = false,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    require((actionLabel == null) == (onAction == null)) { "Supply both actionLabel and onAction." }
    ForgeSurface(modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Polite }) {
        Column(
            Modifier.fillMaxWidth().padding(ForgeTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
            ) {
                if (loading) ForgeProgressIndicator()
                ForgeText(
                    title,
                    style = ForgeTheme.typography.titleSmall,
                    color = if (tone == ForgeStatusTone.Error) ForgeTheme.colors.error else ForgeTheme.colors.onSurface,
                )
            }
            ForgeText(message, color = ForgeTheme.colors.onSurfaceVariant)
            if (actionLabel != null && onAction != null) {
                ForgeButton(onClick = onAction, variant = ForgeButtonVariant.Outline) { ForgeText(actionLabel) }
            }
        }
    }
}
