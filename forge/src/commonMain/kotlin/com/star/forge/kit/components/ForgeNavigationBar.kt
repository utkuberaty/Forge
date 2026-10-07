package com.star.forge.kit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.star.forge.kit.primitives.ForgeIcon
import com.star.forge.kit.primitives.ForgeIconSpec
import com.star.forge.kit.primitives.ForgeText
import com.star.forge.kit.theme.ForgeTheme

/** One navigation destination. Wording and icons are supplied by the app. */
@Immutable
data class ForgeNavigationItem(val label: String, val icon: ForgeIconSpec, val enabled: Boolean = true)

/**
 * Forge-owned compact navigation bar with tab/selection semantics and 48 dp
 * minimum touch targets. Hosts own navigation and system inset handling.
 */
@Composable
fun ForgeNavigationBar(
    items: List<ForgeNavigationItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(items.isNotEmpty() && selectedIndex in items.indices) { "Select an existing navigation item." }
    Row(
        modifier.fillMaxWidth().selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs),
    ) {
        items.forEachIndexed { index, item ->
            val selected = selectedIndex == index
            val interactions = remember { MutableInteractionSource() }
            val pressed by interactions.collectIsPressedAsState()
            val color = if (selected) ForgeTheme.colors.primary else ForgeTheme.colors.onSurfaceVariant
            Row(
                Modifier.weight(1f).defaultMinSize(minHeight = 48.dp).clip(ForgeTheme.shapes.medium)
                    .background(
                        when {
                            selected -> ForgeTheme.colors.primaryContainer
                            pressed -> ForgeTheme.colors.surfaceVariant
                            else -> ForgeTheme.colors.surface
                        },
                    )
                    .selectable(
                        selected = selected,
                        enabled = item.enabled,
                        interactionSource = interactions,
                        indication = null,
                        role = Role.Tab,
                        onClick = { onSelect(index) },
                    )
                    .semantics(mergeDescendants = true) {}
                    .padding(ForgeTheme.spacing.sm),
                horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val tint = if (item.enabled) color else color.copy(alpha = 0.38f)
                ForgeIcon(item.icon.copy(contentDescription = null), tint = tint)
                ForgeText(item.label, color = tint, style = ForgeTheme.typography.labelLarge)
            }
        }
    }
}
