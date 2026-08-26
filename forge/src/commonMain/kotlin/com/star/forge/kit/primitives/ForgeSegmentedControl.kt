package com.star.forge.kit.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import com.star.forge.kit.theme.ForgeTheme

/** Stable-ID item rendered by [ForgeSegmentedControl]. */
@Immutable
public data class ForgeSegmentedItem(
    public val id: String,
    public val label: String,
    public val icon: ForgeIconSpec? = null,
    public val enabled: Boolean = true,
)

/** Colors used by [ForgeSegmentedControl]. */
@Immutable
public data class ForgeSegmentedControlColors(
    public val selectedContainer: Color,
    public val selectedContent: Color,
    public val unselectedContainer: Color,
    public val unselectedContent: Color,
    public val border: Color,
    public val disabledContent: Color,
)

/** Defaults for [ForgeSegmentedControl]. */
public object ForgeSegmentedControlDefaults {
    @Composable
    public fun colors(): ForgeSegmentedControlColors {
        val component = ForgeTheme.components.segmentedControl
        return ForgeSegmentedControlColors(
            selectedContainer = component.selectedContainer ?: ForgeTheme.colors.primaryContainer,
            selectedContent = component.selectedContent ?: ForgeTheme.colors.onPrimaryContainer,
            unselectedContainer = component.unselectedContainer ?: Color.Transparent,
            unselectedContent = component.unselectedContent ?: ForgeTheme.colors.onSurfaceVariant,
            border = ForgeTheme.colors.border,
            disabledContent = ForgeTheme.colors.onSurface.copy(alpha = ForgeTheme.opacity.disabledContent),
        )
    }
}

/** Horizontally scrollable single-selection segmented control. */
@Composable
public fun ForgeSegmentedControl(
    items: List<ForgeSegmentedItem>,
    selectedId: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ForgeSegmentedControlColors = ForgeSegmentedControlDefaults.colors(),
) {
    require(items.isNotEmpty()) { "items must not be empty" }
    require(items.all { it.id.isNotBlank() && it.label.isNotBlank() }) { "item IDs and labels must not be blank" }
    require(items.map { it.id }.distinct().size == items.size) { "item IDs must be unique" }
    require(items.any { it.id == selectedId }) { "selectedId must identify an item" }
    val tokens = ForgeTheme.components.segmentedControl
    val contentPadding = tokens.contentPadding ?: ForgeTheme.spacing.xs
    val itemGap = tokens.itemGap ?: ForgeTheme.spacing.xs
    val itemHorizontalPadding = tokens.itemHorizontalPadding ?: ForgeTheme.spacing.md
    val shape = RoundedCornerShape(ForgeTheme.radii.md)

    Row(
        modifier =
            modifier
                .horizontalScroll(rememberScrollState())
                .clip(shape)
                .border(BorderStroke(ForgeTheme.borders.thin, colors.border), shape)
                .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(itemGap),
    ) {
        items.forEach { item ->
            val selected = item.id == selectedId
            val itemEnabled = enabled && item.enabled
            val interactionSource = remember(item.id) { MutableInteractionSource() }
            val container = if (selected) colors.selectedContainer else colors.unselectedContainer
            val content =
                when {
                    !itemEnabled -> colors.disabledContent
                    selected -> colors.selectedContent
                    else -> colors.unselectedContent
                }

            Row(
                modifier =
                    Modifier
                        .defaultMinSize(minHeight = tokens.minimumHeight.coerceAtLeast(ForgeTheme.touchTargets.minimum))
                        .clip(shape)
                        .background(container, shape)
                        .selectable(
                            selected = selected,
                            interactionSource = interactionSource,
                            indication = null,
                            enabled = itemEnabled,
                            role = Role.Tab,
                            onClick = { onSelected(item.id) },
                        ).semantics { if (!itemEnabled) disabled() }
                        .padding(horizontal = itemHorizontalPadding, vertical = ForgeTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                item.icon?.let {
                    ForgeIcon(
                        spec = it.copy(contentDescription = null),
                        modifier = Modifier.size(it.size ?: ForgeTheme.components.symbol.icon.iconSize),
                        tint = if (it.tint == Color.Unspecified) content else it.tint,
                    )
                    Spacer(Modifier.width(ForgeTheme.spacing.xs))
                }
                Box(contentAlignment = Alignment.Center) {
                    ForgeText(text = item.label, color = content, style = ForgeTheme.typography.labelLarge)
                }
            }
        }
    }
}
