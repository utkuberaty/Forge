package com.star.forge.kit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalForgeColors = staticCompositionLocalOf { ForgeColorSchemes.light() }
internal val LocalForgeContentColor = staticCompositionLocalOf { Color.Unspecified }
internal val LocalForgeSpacing = staticCompositionLocalOf { ForgeSpacing() }
internal val LocalForgeRadii = staticCompositionLocalOf { ForgeRadii() }
internal val LocalForgeBorders = staticCompositionLocalOf { ForgeBorders() }
internal val LocalForgeTypography = staticCompositionLocalOf { ForgeTypography.default }
internal val LocalForgeShapes = staticCompositionLocalOf { ForgeShapes.from(ForgeRadii()) }

/**
 * Read-only access to the active Forge theme values.
 *
 * Use this inside shared UI and primitives instead of hardcoded spacing,
 * radius, border, type, shape, or color values.
 */
object ForgeTheme {
    /** Active Forge-owned color palette. */
    val colors: ForgeColors
        @Composable
        @ReadOnlyComposable
        get() = LocalForgeColors.current

    /** Active content color for text and icons inside Forge containers. */
    val contentColor: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalForgeContentColor.current

    /** Active spacing scale. */
    val spacing: ForgeSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalForgeSpacing.current

    /** Active radius scale. */
    val radii: ForgeRadii
        @Composable
        @ReadOnlyComposable
        get() = LocalForgeRadii.current

    /** Active border width scale. */
    val borders: ForgeBorders
        @Composable
        @ReadOnlyComposable
        get() = LocalForgeBorders.current

    /** Active Forge-owned typography scale. */
    val typography: ForgeTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalForgeTypography.current

    /** Active Forge-owned shape scale. */
    val shapes: ForgeShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalForgeShapes.current
}

/**
 * Provides the Forge UI kit theme.
 *
 * @param darkTheme whether the dark Forge defaults should be used.
 * @param colors Forge-owned color palette.
 * @param spacing layout spacing scale for Forge primitives.
 * @param radii corner radius scale for Forge primitives.
 * @param borders border width scale for dividers and outlines.
 * @param typography Forge-owned text styles.
 * @param shapes Forge-owned shapes, derived from [radii] by default.
 * @param content themed content.
 */
@Composable
fun ForgeKitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: ForgeColors = if (darkTheme) ForgeColorSchemes.dark() else ForgeColorSchemes.light(),
    spacing: ForgeSpacing = ForgeSpacing(),
    radii: ForgeRadii = ForgeRadii(),
    borders: ForgeBorders = ForgeBorders(),
    typography: ForgeTypography = ForgeTypography.default,
    shapes: ForgeShapes = ForgeShapes.from(radii),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalForgeColors provides colors,
        LocalForgeContentColor provides colors.onSurface,
        LocalForgeSpacing provides spacing,
        LocalForgeRadii provides radii,
        LocalForgeBorders provides borders,
        LocalForgeTypography provides typography,
        LocalForgeShapes provides shapes,
        content = content,
    )
}
