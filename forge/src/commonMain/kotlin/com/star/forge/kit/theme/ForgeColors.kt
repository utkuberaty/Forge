package com.star.forge.kit.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Forge-owned color roles.
 *
 * These names describe product meaning instead of implementation details from
 * Android, Material, or any other UI framework. Apps can replace the whole
 * palette in [ForgeKitTheme] without changing primitive APIs.
 */
@Immutable
data class ForgeColors(
    /** Main action and strongest brand/action color. */
    val primary: Color,

    /** Text or icon color placed on top of [primary]. */
    val onPrimary: Color,

    /** Soft primary container for selected, highlighted, or branded surfaces. */
    val primaryContainer: Color,

    /** Text or icon color placed on top of [primaryContainer]. */
    val onPrimaryContainer: Color,

    /** Supporting action color with less emphasis than [primary]. */
    val secondary: Color,

    /** Text or icon color placed on top of [secondary]. */
    val onSecondary: Color,

    /** Soft supporting container for secondary surfaces. */
    val secondaryContainer: Color,

    /** Text or icon color placed on top of [secondaryContainer]. */
    val onSecondaryContainer: Color,

    /** Accent color for moments that should not compete with primary actions. */
    val tertiary: Color,

    /** Text or icon color placed on top of [tertiary]. */
    val onTertiary: Color,

    /** Soft accent container. */
    val tertiaryContainer: Color,

    /** Text or icon color placed on top of [tertiaryContainer]. */
    val onTertiaryContainer: Color,

    /** Screen background behind surfaces. */
    val background: Color,

    /** Text or icon color placed on top of [background]. */
    val onBackground: Color,

    /** Normal UI container color for cards, sheets, fields, and panels. */
    val surface: Color,

    /** Text or icon color placed on top of [surface]. */
    val onSurface: Color,

    /** Muted surface for low-emphasis containers and inactive areas. */
    val surfaceVariant: Color,

    /** Muted text or icon color for secondary labels and helper text. */
    val onSurfaceVariant: Color,

    /** Destructive action, failure, or blocking validation state. */
    val error: Color,

    /** Text or icon color placed on top of [error]. */
    val onError: Color,

    /** Soft destructive/failure container. */
    val errorContainer: Color,

    /** Text or icon color placed on top of [errorContainer]. */
    val onErrorContainer: Color,

    /** Positive result, completed action, available state, or successful validation. */
    val success: Color,

    /** Text or icon color placed on top of [success]. */
    val onSuccess: Color,

    /** Caution, pending risk, recoverable problem, or action that needs attention. */
    val warning: Color,

    /** Text or icon color placed on top of [warning]. */
    val onWarning: Color,

    /** Informational state, neutral guidance, or non-blocking status. */
    val info: Color,

    /** Text or icon color placed on top of [info]. */
    val onInfo: Color,

    /** Subtle outline for cards, fields, dividers, and low-emphasis boundaries. */
    val border: Color,

    /** Stronger outline for focused, selected, active, or high-contrast boundaries. */
    val borderStrong: Color,

    /** Surface color for content raised above the default screen background. */
    val surfaceRaised: Color,

    /** Soft overlay behind dialogs, sheets, popovers, or temporary blocking UI. */
    val scrimSoft: Color,
)

/**
 * Default Forge palettes.
 */
object ForgeColorSchemes {
    /** Default light Forge color palette. */
    fun light(): ForgeColors = ForgeColors(
        primary = Color(0xFF275D67),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFC5EEF5),
        onPrimaryContainer = Color(0xFF00363D),
        secondary = Color(0xFF67587A),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFECDCFF),
        onSecondaryContainer = Color(0xFF211532),
        tertiary = Color(0xFF7A5732),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFFFDDBA),
        onTertiaryContainer = Color(0xFF2B1700),
        background = Color(0xFFFCFCFD),
        onBackground = Color(0xFF1A1C1D),
        surface = Color(0xFFFCFCFD),
        onSurface = Color(0xFF1A1C1D),
        surfaceVariant = Color(0xFFE0E4E7),
        onSurfaceVariant = Color(0xFF43484B),
        error = Color(0xFFBA1A1A),
        onError = Color.White,
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
        success = Color(0xFF1B6D3A),
        onSuccess = Color.White,
        warning = Color(0xFF946300),
        onWarning = Color.White,
        info = Color(0xFF255EA8),
        onInfo = Color.White,
        border = Color(0xFFD2D7DA),
        borderStrong = Color(0xFF8C9499),
        surfaceRaised = Color.White,
        scrimSoft = Color(0x66000000),
    )

    /** Default dark Forge color palette. */
    fun dark(): ForgeColors = ForgeColors(
        primary = Color(0xFF8ACFD9),
        onPrimary = Color(0xFF00363D),
        primaryContainer = Color(0xFF0C4B54),
        onPrimaryContainer = Color(0xFFC5EEF5),
        secondary = Color(0xFFD4C0E8),
        onSecondary = Color(0xFF372A49),
        secondaryContainer = Color(0xFF4E4161),
        onSecondaryContainer = Color(0xFFECDCFF),
        tertiary = Color(0xFFE9BF91),
        onTertiary = Color(0xFF442A0A),
        tertiaryContainer = Color(0xFF604018),
        onTertiaryContainer = Color(0xFFFFDDBA),
        background = Color(0xFF111415),
        onBackground = Color(0xFFE2E3E4),
        surface = Color(0xFF111415),
        onSurface = Color(0xFFE2E3E4),
        surfaceVariant = Color(0xFF43484B),
        onSurfaceVariant = Color(0xFFC3C7CB),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        success = Color(0xFF84D99C),
        onSuccess = Color(0xFF003913),
        warning = Color(0xFFFFC56D),
        onWarning = Color(0xFF4A2F00),
        info = Color(0xFFA8C8FF),
        onInfo = Color(0xFF003160),
        border = Color(0xFF3B4246),
        borderStrong = Color(0xFF9AA1A6),
        surfaceRaised = Color(0xFF1B1F21),
        scrimSoft = Color(0x99000000),
    )

}
