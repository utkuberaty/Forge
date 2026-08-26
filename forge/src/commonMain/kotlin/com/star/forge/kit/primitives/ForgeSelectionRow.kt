package com.star.forge.kit.primitives

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import com.star.forge.kit.theme.ForgeTheme

/** Control rendered by [ForgeSelectionRow]. */
public enum class ForgeSelectionControl { Checkbox, Radio }

/** A whole-row checkbox or radio target with merged text and control semantics. */
@Composable
public fun ForgeSelectionRow(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    primaryText: String,
    modifier: Modifier = Modifier,
    control: ForgeSelectionControl = ForgeSelectionControl.Checkbox,
    supportingText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true,
    accessibilityLabel: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    require(primaryText.isNotBlank()) { "primaryText must not be blank" }
    val tokens = ForgeTheme.components.selectionRow
    val horizontalPadding = tokens.horizontalPadding ?: ForgeTheme.spacing.md
    val verticalPadding = tokens.verticalPadding ?: ForgeTheme.spacing.sm
    val controlGap = tokens.controlGap ?: ForgeTheme.spacing.sm
    val selectionModifier =
        when (control) {
            ForgeSelectionControl.Checkbox ->
                Modifier.toggleable(
                    value = selected,
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    role = Role.Checkbox,
                    onValueChange = onSelectedChange,
                )
            ForgeSelectionControl.Radio ->
                Modifier.selectable(
                    selected = selected,
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    role = Role.RadioButton,
                    onClick = { onSelectedChange(true) },
                )
        }

    Row(
        modifier =
            modifier
                .defaultMinSize(minHeight = tokens.minimumHeight.coerceAtLeast(ForgeTheme.touchTargets.minimum))
                .then(selectionModifier)
                .semantics(mergeDescendants = true) {
                    accessibilityLabel?.let { contentDescription = it }
                    errorText?.let { error(it) }
                }.padding(horizontalPadding, verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (control) {
            ForgeSelectionControl.Checkbox ->
                ForgeCheckbox(
                    checked = selected,
                    enabled = enabled,
                    modifier = Modifier.clearAndSetSemantics { },
                )
            ForgeSelectionControl.Radio ->
                ForgeRadioButton(
                    selected = selected,
                    enabled = enabled,
                    modifier = Modifier.clearAndSetSemantics { },
                )
        }
        Spacer(Modifier.width(controlGap))
        Column(Modifier.weight(1f)) {
            ForgeText(
                text = primaryText,
                color =
                    if (enabled) {
                        ForgeTheme.colors.onSurface
                    } else {
                        ForgeTheme.colors.onSurface.copy(
                            alpha = ForgeTheme.opacity.disabledContent,
                        )
                    },
                style = ForgeTheme.typography.bodyLarge,
            )
            supportingText?.let {
                ForgeText(
                    text = it,
                    color = ForgeTheme.colors.onSurfaceVariant,
                    style = ForgeTheme.typography.bodySmall,
                )
            }
            errorText?.let {
                ForgeText(text = it, color = ForgeTheme.colors.error, style = ForgeTheme.typography.bodySmall)
            }
        }
    }
}
