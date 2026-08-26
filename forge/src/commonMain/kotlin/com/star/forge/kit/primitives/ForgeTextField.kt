package com.star.forge.kit.primitives

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import com.star.forge.kit.theme.ForgeSymbolSize
import com.star.forge.kit.theme.ForgeTheme

/** Caller-owned field status and optional user-facing feedback copy. */
public sealed interface ForgeFieldFeedback {
    public val message: String?

    public data class Helper(
        override val message: String,
    ) : ForgeFieldFeedback

    public data class Checking(
        override val message: String? = null,
    ) : ForgeFieldFeedback

    public data class Valid(
        override val message: String? = null,
    ) : ForgeFieldFeedback

    public data class Invalid(
        override val message: String,
    ) : ForgeFieldFeedback
}

/** Configuration for a leading or trailing field symbol. */
@Immutable
public data class ForgeTextFieldIcon(
    public val icon: ForgeIconSpec,
    public val size: Dp? = null,
    public val spacing: Dp? = null,
    public val symbolSize: ForgeSymbolSize? = null,
    public val variant: ForgeSymbolVariant = ForgeSymbolVariant.Ghost,
    public val shape: Shape? = null,
    public val colors: ForgeSymbolColors? = null,
    public val enabled: Boolean = true,
    public val accessibilityLabel: String? = icon.contentDescription,
    public val onClick: (() -> Unit)? = null,
)

/** Colors used by [ForgeTextField]. */
@Immutable
public data class ForgeTextFieldColors(
    public val container: Color,
    public val content: Color,
    public val placeholder: Color,
    public val label: Color,
    public val focusedLabel: Color,
    public val border: Color,
    public val focusedBorder: Color,
    public val cursor: Color,
    public val supporting: Color,
    public val checking: Color,
    public val valid: Color,
    public val invalidContainer: Color,
    public val invalidContent: Color,
    public val invalid: Color,
    public val disabledContainer: Color,
    public val disabledContent: Color,
    public val disabledLabel: Color,
    public val disabledBorder: Color,
)

/** Defaults for [ForgeTextField]. */
public object ForgeTextFieldDefaults {
    /** Colors resolved through component overrides, then semantic and foundation tokens. */
    @Composable
    public fun colors(): ForgeTextFieldColors {
        val component = ForgeTheme.components.textField
        val opacity = ForgeTheme.opacity
        return ForgeTextFieldColors(
            container = ForgeTheme.colors.surface,
            content = ForgeTheme.colors.onSurface,
            placeholder = ForgeTheme.colors.onSurfaceVariant,
            label = ForgeTheme.colors.onSurfaceVariant,
            focusedLabel = ForgeTheme.colors.primary,
            border = ForgeTheme.colors.border,
            focusedBorder = ForgeTheme.colors.primary,
            cursor = ForgeTheme.colors.primary,
            supporting = component.helperColor ?: ForgeTheme.colors.onSurfaceVariant,
            checking = component.checkingColor ?: ForgeTheme.colors.primary,
            valid = component.validColor ?: ForgeTheme.colors.success,
            invalidContainer = component.invalidContainer ?: ForgeTheme.colors.errorContainer,
            invalidContent = ForgeTheme.colors.onErrorContainer,
            invalid = component.invalidColor ?: ForgeTheme.colors.error,
            disabledContainer = ForgeTheme.colors.surfaceVariant.copy(alpha = opacity.disabledContent),
            disabledContent = ForgeTheme.colors.onSurface.copy(alpha = opacity.disabledContent),
            disabledLabel = ForgeTheme.colors.onSurfaceVariant.copy(alpha = opacity.disabledContent),
            disabledBorder = ForgeTheme.colors.border.copy(alpha = opacity.disabledContent),
        )
    }
}

/**
 * Forge-owned text field. Validation logic and every displayed message remain caller-owned.
 */
@Composable
public fun ForgeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ForgeTextFieldIcon? = null,
    trailingIcon: ForgeTextFieldIcon? = null,
    supportingText: String? = null,
    feedback: ForgeFieldFeedback? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = RoundedCornerShape(ForgeTheme.components.textField.cornerRadius ?: ForgeTheme.radii.md),
    colors: ForgeTextFieldColors = ForgeTextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    accessibilityLabel: String? = label ?: placeholder,
    accessibilityStateDescription: String? = null,
) {
    require(minLines > 0) { "minLines must be positive" }
    require(maxLines >= minLines) { "maxLines must be greater than or equal to minLines" }
    require(!singleLine || (minLines == 1 && maxLines == 1)) { "singleLine fields must use exactly one line" }
    val focused by interactionSource.collectIsFocusedAsState()
    val invalid = feedback as? ForgeFieldFeedback.Invalid
    val message = feedback?.message ?: supportingText
    val animation = tween<Color>(ForgeTheme.motion.fastDurationMillis, easing = ForgeTheme.motion.standardEasing)
    val targetContainer =
        when {
            !enabled -> colors.disabledContainer
            invalid != null -> colors.invalidContainer
            else -> colors.container
        }
    val targetContent =
        when {
            !enabled -> colors.disabledContent
            invalid != null -> colors.invalidContent
            else -> colors.content
        }
    val targetBorder =
        when {
            !enabled -> colors.disabledBorder
            invalid != null -> colors.invalid
            focused -> colors.focusedBorder
            else -> colors.border
        }
    val targetLabel =
        when {
            !enabled -> colors.disabledLabel
            invalid != null -> colors.invalid
            focused -> colors.focusedLabel
            else -> colors.label
        }
    val containerColor by animateColorAsState(targetContainer, animation, label = "ForgeFieldContainer")
    val contentColor by animateColorAsState(targetContent, animation, label = "ForgeFieldContent")
    val borderColor by animateColorAsState(targetBorder, animation, label = "ForgeFieldBorder")
    val labelColor by animateColorAsState(targetLabel, animation, label = "ForgeFieldLabel")
    val horizontalPadding = ForgeTheme.components.textField.horizontalPadding ?: ForgeTheme.spacing.md
    val verticalPadding = ForgeTheme.components.textField.verticalPadding ?: ForgeTheme.spacing.sm

    Column(
        modifier =
            modifier.semantics(mergeDescendants = true) {
                accessibilityLabel?.let { contentDescription = it }
                accessibilityStateDescription?.let { stateDescription = it }
                invalid?.let { error(it.message) }
                if (!enabled) disabled()
            },
        verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs),
    ) {
        label?.let {
            ForgeText(text = it, color = labelColor, style = ForgeTheme.typography.labelMedium)
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = ForgeTheme.typography.bodyLarge.copy(color = contentColor),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(if (enabled) colors.cursor else colors.disabledContent),
            decorationBox = { innerTextField ->
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = ForgeTheme.components.textField.minimumHeight)
                            .clip(shape)
                            .background(containerColor, shape)
                            .border(
                                BorderStroke(
                                    if (focused) ForgeTheme.borders.medium else ForgeTheme.borders.thin,
                                    borderColor,
                                ),
                                shape,
                            ).padding(horizontalPadding, verticalPadding),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextFieldIconSlot(leadingIcon, enabled, leading = true)
                    Box(Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty() && placeholder != null) {
                            ForgeText(
                                text = placeholder,
                                color = colors.placeholder,
                                style = ForgeTheme.typography.bodyLarge,
                            )
                        }
                        innerTextField()
                    }
                    TextFieldIconSlot(trailingIcon, enabled, leading = false)
                }
            },
        )

        if (message != null) {
            ForgeText(
                text = message,
                modifier =
                    Modifier
                        .padding(horizontal = ForgeTheme.spacing.xs)
                        .semantics {
                            if (feedback is ForgeFieldFeedback.Checking || feedback is ForgeFieldFeedback.Valid) {
                                liveRegion = LiveRegionMode.Polite
                            }
                        },
                color = feedbackColor(feedback, colors),
                style = ForgeTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun TextFieldIconSlot(
    icon: ForgeTextFieldIcon?,
    enabled: Boolean,
    leading: Boolean,
) {
    if (icon == null) return
    val iconSize = icon.size ?: icon.symbolSize?.iconSize ?: ForgeTheme.components.textField.iconSize
    val symbolSize = icon.symbolSize ?: ForgeSymbolSize(iconSize, iconSize)
    val gap = icon.spacing ?: ForgeTheme.spacing.sm
    if (!leading) Spacer(Modifier.width(gap))
    ForgeSymbol(
        icon = icon.icon.copy(size = symbolSize.iconSize),
        enabled = enabled && icon.enabled,
        onClick = icon.onClick,
        accessibilityLabel = icon.accessibilityLabel,
        variant = icon.variant,
        size = symbolSize,
        shape = icon.shape ?: RoundedCornerShape(ForgeTheme.radii.sm),
        colors = icon.colors ?: ForgeSymbolDefaults.colors(icon.variant),
    )
    if (leading) Spacer(Modifier.width(gap))
}

private fun feedbackColor(
    feedback: ForgeFieldFeedback?,
    colors: ForgeTextFieldColors,
): Color =
    when (feedback) {
        is ForgeFieldFeedback.Checking -> colors.checking
        is ForgeFieldFeedback.Valid -> colors.valid
        is ForgeFieldFeedback.Invalid -> colors.invalid
        is ForgeFieldFeedback.Helper, null -> colors.supporting
    }
