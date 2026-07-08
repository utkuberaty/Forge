package com.star.forge.kit.primitives

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.star.forge.kit.theme.ForgeTheme

/**
 * Validation or feedback state for a Forge form field.
 */
enum class ForgeTextFieldState {
    /** Normal input state. */
    Default,

    /** Valid, confirmed, or completed input state. */
    Success,

    /** Recoverable warning or "needs attention" input state. */
    Warning,

    /** Blocking validation or destructive input state. */
    Error,
}

/**
 * Props for a text field icon slot.
 *
 * Text field adornments render through [ForgeSymbol], so callers can keep a
 * plain icon, add a shaped background, or make the leading/trailing symbol
 * clickable. Use [spacing] for the gap between the symbol and editable text.
 * If [spacing] is null, Forge uses the active theme spacing.
 */
data class ForgeTextFieldIcon(
    val icon: ForgeIconSpec,
    val size: Dp? = null,
    val spacing: Dp? = null,
    val symbolSize: ForgeSymbolSize? = null,
    val variant: ForgeSymbolVariant = ForgeSymbolVariant.Ghost,
    val shape: Shape? = null,
    val colors: ForgeSymbolColors? = null,
    val enabled: Boolean = true,
    val onClick: (() -> Unit)? = null,
)

/**
 * Color state model for [ForgeTextField].
 *
 * Forge owns field colors directly instead of delegating focus, disabled, and
 * validation states to Material defaults.
 */
@Immutable
data class ForgeTextFieldColors(
    val container: Color,
    val content: Color,
    val placeholder: Color,
    val label: Color,
    val focusedLabel: Color,
    val border: Color,
    val focusedBorder: Color,
    val cursor: Color,
    val supporting: Color,
    val disabledContainer: Color,
    val disabledContent: Color,
    val disabledLabel: Color,
    val disabledBorder: Color,
    val success: Color,
    val warning: Color,
    val errorContainer: Color,
    val errorContent: Color,
    val error: Color,
) {
    fun container(enabled: Boolean, state: ForgeTextFieldState): Color = when {
        !enabled -> disabledContainer
        state == ForgeTextFieldState.Error -> errorContainer
        else -> container
    }

    fun content(enabled: Boolean, state: ForgeTextFieldState): Color = when {
        !enabled -> disabledContent
        state == ForgeTextFieldState.Error -> errorContent
        else -> content
    }

    fun label(enabled: Boolean, focused: Boolean, state: ForgeTextFieldState): Color = when {
        !enabled -> disabledLabel
        state == ForgeTextFieldState.Error -> error
        state == ForgeTextFieldState.Success -> success
        state == ForgeTextFieldState.Warning -> warning
        focused -> focusedLabel
        else -> label
    }

    fun border(enabled: Boolean, focused: Boolean, state: ForgeTextFieldState): Color = when {
        !enabled -> disabledBorder
        state == ForgeTextFieldState.Error -> error
        state == ForgeTextFieldState.Success -> success
        state == ForgeTextFieldState.Warning -> warning
        focused -> focusedBorder
        else -> border
    }

    fun supporting(enabled: Boolean, state: ForgeTextFieldState): Color = when {
        !enabled -> disabledLabel
        state == ForgeTextFieldState.Error -> error
        state == ForgeTextFieldState.Success -> success
        state == ForgeTextFieldState.Warning -> warning
        else -> supporting
    }
}

object ForgeTextFieldDefaults {
    /** Minimum height for a single-line Forge text field. */
    val MinHeight: Dp = 56.dp

    /** Default icon size for field icon slots. */
    val IconSize: Dp = 18.dp

    /**
     * Default Forge-owned text field colors.
     */
    @Composable
    fun colors(): ForgeTextFieldColors = ForgeTextFieldColors(
        container = ForgeTheme.colors.surface,
        content = ForgeTheme.colors.onSurface,
        placeholder = ForgeTheme.colors.onSurfaceVariant,
        label = ForgeTheme.colors.onSurfaceVariant,
        focusedLabel = ForgeTheme.colors.primary,
        border = ForgeTheme.colors.border,
        focusedBorder = ForgeTheme.colors.primary,
        cursor = ForgeTheme.colors.primary,
        supporting = ForgeTheme.colors.onSurfaceVariant,
        disabledContainer = ForgeTheme.colors.surfaceVariant.copy(alpha = 0.38f),
        disabledContent = ForgeTheme.colors.onSurface.copy(alpha = 0.38f),
        disabledLabel = ForgeTheme.colors.onSurfaceVariant.copy(alpha = 0.38f),
        disabledBorder = ForgeTheme.colors.border.copy(alpha = 0.38f),
        success = ForgeTheme.colors.success,
        warning = ForgeTheme.colors.warning,
        errorContainer = ForgeTheme.colors.errorContainer,
        errorContent = ForgeTheme.colors.onErrorContainer,
        error = ForgeTheme.colors.error,
    )
}

/**
 * Standard Forge text field.
 *
 * This field is built on Compose Multiplatform [BasicTextField]. Forge owns the
 * container, border, icon slots, helper/error text, validation state, and the
 * floating hint animation.
 *
 * If [label] is provided, it starts inline while the empty field is unfocused
 * and animates to the top-left when focused or filled. If [label] is not
 * provided, [placeholder] becomes that floating hint, so the field can still
 * explain itself without a separate top label.
 *
 * @param supportingText helper text shown below the field when no [errorText]
 * is provided.
 * @param errorText blocking validation text shown below the field and treated
 * as [ForgeTextFieldState.Error].
 * @param isError compatibility flag for callers that already model validation
 * with a boolean. Prefer [state] or [errorText] for new code.
 * @param accessibilityLabel optional spoken label. Defaults to [label], then
 * [placeholder], so visual field names are also exposed to accessibility.
 * @param accessibilityStateDescription optional spoken state for custom app
 * wording. Forge derives one from validation/read-only state when omitted.
 */
@Composable
fun ForgeTextField(
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
    errorText: String? = null,
    state: ForgeTextFieldState = ForgeTextFieldState.Default,
    isError: Boolean = false,
    singleLine: Boolean = true,
    shape: Shape = RoundedCornerShape(ForgeTheme.radii.md),
    colors: ForgeTextFieldColors = ForgeTextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    accessibilityLabel: String? = label ?: placeholder,
    accessibilityStateDescription: String? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val effectiveState = when {
        isError || errorText != null -> ForgeTextFieldState.Error
        else -> state
    }
    val floatingText = label ?: placeholder
    val shouldFloat = floatingText != null && (focused || value.isNotEmpty())
    val message = errorText ?: supportingText
    val fieldStateDescription = accessibilityStateDescription ?: effectiveState.accessibilityDescription(readOnly)

    val containerColor by animateColorAsState(
        targetValue = colors.container(enabled = enabled, state = effectiveState),
        label = "ForgeTextFieldContainerColor",
    )
    val contentColor by animateColorAsState(
        targetValue = colors.content(enabled = enabled, state = effectiveState),
        label = "ForgeTextFieldContentColor",
    )
    val labelColor by animateColorAsState(
        targetValue = colors.label(enabled = enabled, focused = focused, state = effectiveState),
        label = "ForgeTextFieldLabelColor",
    )
    val borderColor by animateColorAsState(
        targetValue = colors.border(enabled = enabled, focused = focused, state = effectiveState),
        label = "ForgeTextFieldBorderColor",
    )
    val supportingColor by animateColorAsState(
        targetValue = colors.supporting(enabled = enabled, state = effectiveState),
        label = "ForgeTextFieldSupportingColor",
    )
    val floatingProgress by animateFloatAsState(
        targetValue = if (shouldFloat) 1f else 0f,
        label = "ForgeTextFieldFloatingProgress",
    )
    val showFloatingLabel = floatingText != null && (shouldFloat || floatingProgress > 0.01f)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.semantics {
            if (accessibilityLabel != null) {
                contentDescription = accessibilityLabel
            }
            if (fieldStateDescription != null) {
                stateDescription = fieldStateDescription
            }
            if (errorText != null) {
                error(errorText)
            }
            if (!enabled) {
                disabled()
            }
        },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = ForgeTheme.typography.bodyLarge.copy(color = contentColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(if (enabled) colors.cursor else colors.disabledContent),
        decorationBox = { innerTextField ->
            Column(verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs)) {
                if (showFloatingLabel) {
                    FloatingTextFieldLabel(
                        text = floatingText,
                        color = labelColor,
                        startPadding = leadingIcon.floatingLabelStartPadding(),
                        progress = floatingProgress,
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = ForgeTextFieldDefaults.MinHeight)
                        .clip(shape)
                        .background(containerColor, shape)
                        .border(
                            border = BorderStroke(
                                width = if (focused) ForgeTheme.borders.medium else ForgeTheme.borders.thin,
                                color = borderColor,
                            ),
                            shape = shape,
                        )
                        .padding(
                            horizontal = ForgeTheme.spacing.md,
                            vertical = ForgeTheme.spacing.sm,
                        ),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    TextFieldInputRow(
                        value = value,
                        label = label,
                        placeholder = placeholder,
                        shouldFloat = shouldFloat,
                        placeholderColor = colors.placeholder,
                        enabled = enabled,
                        leadingIcon = leadingIcon,
                        trailingIcon = trailingIcon,
                        innerTextField = innerTextField,
                    )
                }

                if (message != null) {
                    ForgeText(
                        text = message,
                        modifier = Modifier.padding(horizontal = ForgeTheme.spacing.xs),
                        color = supportingColor,
                        style = ForgeTheme.typography.bodySmall,
                    )
                }
            }
        },
    )
}

@Composable
private fun TextFieldInputRow(
    value: String,
    label: String?,
    placeholder: String?,
    shouldFloat: Boolean,
    placeholderColor: Color,
    enabled: Boolean,
    leadingIcon: ForgeTextFieldIcon?,
    trailingIcon: ForgeTextFieldIcon?,
    innerTextField: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextFieldIconSlot(
            icon = leadingIcon,
            contentColor = placeholderColor,
            enabled = enabled,
        )

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart,
        ) {
            InlinePlaceholder(
                value = value,
                label = label,
                placeholder = placeholder,
                shouldFloat = shouldFloat,
                color = placeholderColor,
            )
            innerTextField()
        }

        TextFieldIconSlot(
            icon = trailingIcon,
            contentColor = placeholderColor,
            enabled = enabled,
            leading = false,
        )
    }
}

@Composable
private fun InlinePlaceholder(
    value: String,
    label: String?,
    placeholder: String?,
    shouldFloat: Boolean,
    color: Color,
) {
    val inlineText = when {
        value.isNotEmpty() -> null
        label != null && !shouldFloat -> label
        label != null && shouldFloat -> placeholder
        placeholder != null && !shouldFloat -> placeholder
        placeholder != null && shouldFloat -> null
        else -> null
    }

    if (inlineText != null) {
        ForgeText(
            text = inlineText,
            color = color,
            style = ForgeTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun FloatingTextFieldLabel(
    text: String?,
    color: Color,
    startPadding: Dp,
    progress: Float,
) {
    if (text == null) return

    ForgeText(
        text = text,
        modifier = Modifier
            .padding(start = startPadding)
            .graphicsLayer {
                val scale = 0.96f + (0.04f * progress)
                alpha = progress
                scaleX = scale
                scaleY = scale
                transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 0f)
            },
        color = color,
        style = ForgeTheme.typography.labelMedium,
    )
}

@Composable
private fun TextFieldIconSlot(
    icon: ForgeTextFieldIcon?,
    contentColor: Color,
    enabled: Boolean,
    leading: Boolean = true,
) {
    if (icon == null) return

    val iconSize = icon.size ?: icon.symbolSize?.iconSize ?: ForgeTextFieldDefaults.IconSize
    val symbolSize = icon.symbolSize ?: ForgeSymbolSize(
        containerSize = iconSize,
        iconSize = iconSize,
    )
    val gap = icon.spacing ?: ForgeTheme.spacing.sm

    if (!leading) {
        Spacer(Modifier.width(gap))
    }

    ForgeSymbol(
        icon = icon.icon.copy(size = symbolSize.iconSize),
        enabled = enabled && icon.enabled,
        onClick = icon.onClick,
        variant = icon.variant,
        size = symbolSize,
        shape = icon.shape ?: RoundedCornerShape(ForgeTheme.radii.sm),
        modifier = Modifier.size(symbolSize.containerSize),
        colors = icon.colors ?: ForgeSymbolDefaults.colors(icon.variant),
    )

    if (leading) {
        Spacer(Modifier.width(gap))
    }
}

@Composable
private fun ForgeTextFieldIcon?.labelStartPadding(): Dp {
    if (this == null) return ForgeTheme.spacing.zero
    val iconSize = size ?: symbolSize?.iconSize ?: ForgeTextFieldDefaults.IconSize
    val containerSize = symbolSize?.containerSize ?: iconSize
    return containerSize + (spacing ?: ForgeTheme.spacing.sm)
}

@Composable
private fun ForgeTextFieldIcon?.floatingLabelStartPadding(): Dp =
    ForgeTheme.spacing.zero

private fun ForgeTextFieldState.accessibilityDescription(readOnly: Boolean): String? = when {
    readOnly -> "Read only"
    this == ForgeTextFieldState.Success -> "Valid"
    this == ForgeTextFieldState.Warning -> "Needs attention"
    this == ForgeTextFieldState.Error -> "Error"
    else -> null
}

private fun Color.takeIfSpecified(fallback: Color): Color =
    if (this == Color.Unspecified) fallback else this
