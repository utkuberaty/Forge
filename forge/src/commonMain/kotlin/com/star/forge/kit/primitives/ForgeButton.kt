package com.star.forge.kit.primitives

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.star.forge.kit.theme.ForgeButtonSize
import com.star.forge.kit.theme.ForgeTheme
import com.star.forge.kit.theme.LocalForgeContentColor

public enum class ForgeButtonVariant { Primary, Secondary, Outline, Ghost, Danger }

public sealed interface ForgeButtonState {
    public data object Idle : ForgeButtonState

    @Immutable
    public data class Loading(
        public val label: String? = null,
        public val icon: ForgeIconSpec? = null,
    ) : ForgeButtonState
}

@Immutable
public data class ForgeButtonIcon(
    public val icon: ForgeIconSpec,
    public val size: Dp? = null,
    public val spacing: Dp? = null,
)

@Immutable
public data class ForgeButtonColors(
    public val container: Color,
    public val content: Color,
    public val pressedContainer: Color,
    public val disabledContainer: Color,
    public val disabledContent: Color,
    public val border: Color? = null,
    public val pressedBorder: Color? = border,
    public val disabledBorder: Color? = border,
) {
    public fun container(
        enabled: Boolean,
        pressed: Boolean,
    ): Color =
        when {
            !enabled -> disabledContainer
            pressed -> pressedContainer
            else -> container
        }

    public fun content(enabled: Boolean): Color = if (enabled) content else disabledContent

    public fun border(
        enabled: Boolean,
        pressed: Boolean,
    ): Color? =
        when {
            !enabled -> disabledBorder
            pressed -> pressedBorder
            else -> border
        }
}

public object ForgeButtonDefaults {
    @Composable
    public fun colors(variant: ForgeButtonVariant): ForgeButtonColors {
        val opacity = ForgeTheme.opacity
        val disabledContainer = ForgeTheme.colors.onSurface.copy(alpha = opacity.disabledContainer)
        val disabledContent = ForgeTheme.colors.onSurface.copy(alpha = opacity.disabledContent)
        val visual = ForgeTheme.components.button.visuals
        val pressedAlpha = visual.pressedOpacity ?: ForgeTheme.components.button.pressedOpacity ?: opacity.pressed

        val base =
            when (variant) {
                ForgeButtonVariant.Primary ->
                    ForgeButtonColors(
                        container = ForgeTheme.colors.primary,
                        content = ForgeTheme.colors.onPrimary,
                        pressedContainer = ForgeTheme.colors.primary.copy(alpha = pressedAlpha),
                        disabledContainer = disabledContainer,
                        disabledContent = disabledContent,
                    )
                ForgeButtonVariant.Secondary ->
                    ForgeButtonColors(
                        container = ForgeTheme.colors.secondaryContainer,
                        content = ForgeTheme.colors.onSecondaryContainer,
                        pressedContainer = ForgeTheme.colors.secondaryContainer.copy(alpha = pressedAlpha),
                        disabledContainer = disabledContainer,
                        disabledContent = disabledContent,
                    )
                ForgeButtonVariant.Outline ->
                    ForgeButtonColors(
                        container = Color.Transparent,
                        content = ForgeTheme.colors.primary,
                        pressedContainer = ForgeTheme.colors.primary.copy(alpha = opacity.subtle),
                        disabledContainer = Color.Transparent,
                        disabledContent = disabledContent,
                        border = ForgeTheme.colors.borderStrong,
                        pressedBorder = ForgeTheme.colors.primary,
                        disabledBorder = ForgeTheme.colors.borderStrong.copy(alpha = opacity.disabledContent),
                    )
                ForgeButtonVariant.Ghost ->
                    ForgeButtonColors(
                        container = Color.Transparent,
                        content = ForgeTheme.colors.primary,
                        pressedContainer = ForgeTheme.colors.primary.copy(alpha = opacity.subtle),
                        disabledContainer = Color.Transparent,
                        disabledContent = disabledContent,
                    )
                ForgeButtonVariant.Danger ->
                    ForgeButtonColors(
                        container = ForgeTheme.colors.error,
                        content = ForgeTheme.colors.onError,
                        pressedContainer = ForgeTheme.colors.error.copy(alpha = pressedAlpha),
                        disabledContainer = disabledContainer,
                        disabledContent = disabledContent,
                    )
            }
        return base.copy(
            container = visual.container ?: base.container,
            content = visual.content ?: base.content,
            disabledContainer = visual.disabledContainer ?: base.disabledContainer,
            disabledContent = visual.disabledContent ?: base.disabledContent,
            border = visual.border ?: base.border,
        )
    }
}

@Composable
public fun ForgeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: ForgeButtonState = ForgeButtonState.Idle,
    variant: ForgeButtonVariant = ForgeButtonVariant.Primary,
    size: ForgeButtonSize = ForgeTheme.components.button.medium,
    shape: Shape =
        RoundedCornerShape(
            ForgeTheme.components.button.visuals.cornerRadius
                ?: ForgeTheme.components.button.cornerRadius
                ?: ForgeTheme.radii.md,
        ),
    colors: ForgeButtonColors = ForgeButtonDefaults.colors(variant),
    leadingIcon: ForgeButtonIcon? = null,
    trailingIcon: ForgeButtonIcon? = null,
    accessibilityLabel: String? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = size.horizontalPadding, vertical = size.verticalPadding),
    content: @Composable RowScope.() -> Unit,
) {
    require(
        size.visualHeight.value > 0f && size.horizontalPadding.value >= 0f && size.verticalPadding.value >= 0f && size.iconSize.value > 0f,
    ) {
        "button dimensions must be positive and padding must be nonnegative"
    }
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val effectiveEnabled = enabled && state is ForgeButtonState.Idle
    val containerColor = colors.container(effectiveEnabled, pressed)
    val contentColor = colors.content(effectiveEnabled)
    val borderColor = colors.border(effectiveEnabled, pressed)
    val border = borderColor?.let { BorderStroke(ForgeTheme.borders.thin, it) }
    val touchTarget = ForgeTheme.components.button.minimumTouchTarget ?: ForgeTheme.touchTargets.minimum

    CompositionLocalProvider(LocalForgeContentColor provides contentColor) {
        Box(
            modifier =
                modifier
                    .defaultMinSize(minWidth = touchTarget, minHeight = touchTarget)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = effectiveEnabled,
                        role = Role.Button,
                        onClick = onClick,
                    ).semantics(mergeDescendants = true) {
                        accessibilityLabel?.let { contentDescription = it }
                        if (!effectiveEnabled) disabled()
                    },
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier =
                    Modifier
                        .defaultMinSize(minHeight = size.visualHeight)
                        .clip(shape)
                        .background(containerColor, shape)
                        .then(if (border != null) Modifier.border(border, shape) else Modifier)
                        .padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val loading = state as? ForgeButtonState.Loading
                if (loading != null && (loading.label != null || loading.icon != null)) {
                    LoadingContent(loading = loading, size = size, contentColor = contentColor)
                } else {
                    ButtonIconSlot(leadingIcon, size.iconSize, contentColor)
                    content()
                    ButtonIconSlot(trailingIcon, size.iconSize, contentColor, leading = false)
                }
            }
        }
    }
}

@Composable
private fun RowScope.LoadingContent(
    loading: ForgeButtonState.Loading,
    size: ForgeButtonSize,
    contentColor: Color,
) {
    loading.icon?.let { icon ->
        val transition = rememberInfiniteTransition(label = "ForgeButtonLoading")
        val rotation by transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        tween(
                            ForgeTheme.components.button.visuals.motionDurationMillis ?: ForgeTheme.motion.emphasizedDurationMillis,
                            easing = LinearEasing,
                        ),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "ForgeButtonLoadingRotation",
        )
        ForgeIcon(icon.copy(size = size.iconSize), Modifier.size(size.iconSize).rotate(rotation), icon.tint.takeIfSpecified(contentColor))
        if (loading.label != null) Spacer(Modifier.width(ForgeTheme.spacing.xs))
    }
    loading.label?.let { ForgeText(it, color = contentColor, style = ForgeTheme.typography.labelLarge) }
}

@Composable
private fun RowScope.ButtonIconSlot(
    icon: ForgeButtonIcon?,
    fallbackSize: Dp,
    contentColor: Color,
    leading: Boolean = true,
) {
    if (icon == null) return
    val iconSize = icon.size ?: fallbackSize
    if (!leading) Spacer(Modifier.width(icon.spacing ?: ForgeTheme.spacing.xs))
    ForgeIcon(icon.icon.copy(size = iconSize), Modifier.size(iconSize), icon.icon.tint.takeIfSpecified(contentColor))
    if (leading) Spacer(Modifier.width(icon.spacing ?: ForgeTheme.spacing.xs))
}

private fun Color.takeIfSpecified(fallback: Color): Color = if (this == Color.Unspecified) fallback else this
