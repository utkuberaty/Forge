package com.star.forge.kit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalForgeTokens = staticCompositionLocalOf { ForgeTokenSets.light() }
internal val LocalForgeContentColor = staticCompositionLocalOf { Color.Unspecified }

public object ForgeTheme {
    public val tokens: ForgeTokens
        @Composable @ReadOnlyComposable
        get() = LocalForgeTokens.current
    public val colors: ForgeColors
        @Composable @ReadOnlyComposable
        get() = tokens.colors
    public val contentColor: Color
        @Composable @ReadOnlyComposable
        get() = LocalForgeContentColor.current
    public val spacing: ForgeSpacing
        @Composable @ReadOnlyComposable
        get() = tokens.spacing
    public val radii: ForgeRadii
        @Composable @ReadOnlyComposable
        get() = tokens.radii
    public val borders: ForgeBorders
        @Composable @ReadOnlyComposable
        get() = tokens.borders
    public val typography: ForgeTypography
        @Composable @ReadOnlyComposable
        get() = tokens.typography
    public val shapes: ForgeShapes
        @Composable @ReadOnlyComposable
        get() = ForgeShapes.from(tokens.radii)
    public val opacity: ForgeOpacity
        @Composable @ReadOnlyComposable
        get() = tokens.opacity
    public val motion: ForgeMotion
        @Composable @ReadOnlyComposable
        get() = tokens.motion
    public val elevation: ForgeElevation
        @Composable @ReadOnlyComposable
        get() = tokens.elevation
    public val touchTargets: ForgeTouchTargets
        @Composable @ReadOnlyComposable
        get() = tokens.touchTargets
    public val components: ForgeComponentTokens
        @Composable @ReadOnlyComposable
        get() = tokens.components
}

@Composable
public fun ForgeKitTheme(
    tokenSet: ForgeTokenSet = ForgeTokenSets.default(),
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
): Unit = ForgeKitTheme(tokens = if (darkTheme) tokenSet.dark else tokenSet.light, content = content)

@Composable
public fun ForgeKitTheme(
    tokens: ForgeTokens,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalForgeTokens provides tokens,
        LocalForgeContentColor provides tokens.colors.onSurface,
        content = content,
    )
}
