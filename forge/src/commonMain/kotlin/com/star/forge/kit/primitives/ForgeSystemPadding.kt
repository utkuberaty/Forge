package com.star.forge.kit.primitives

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier

/**
 * System inset groups that Forge apps can opt into.
 *
 * Use these instead of raw platform inset calls so app screens stay consistent
 * across Android and iOS.
 */
enum class ForgeSystemPadding {
    /** Avoids system bars, display cutouts, and other areas unsafe for drawing important UI. */
    SafeDrawing,

    /** Avoids areas unsafe for readable/tappable content. */
    SafeContent,

    /** Avoids gesture exclusion areas where system gestures may conflict. */
    SafeGestures,

    /** Status and navigation bars. */
    SystemBars,

    /** Top status bar area. */
    StatusBars,

    /** Bottom/side navigation bar area. */
    NavigationBars,

    /** On-screen keyboard area. */
    Ime,
}

/**
 * Applies one Forge system padding group.
 */
fun Modifier.forgeSystemPadding(
    padding: ForgeSystemPadding = ForgeSystemPadding.SafeDrawing,
): Modifier = when (padding) {
    ForgeSystemPadding.SafeDrawing -> safeDrawingPadding()
    ForgeSystemPadding.SafeContent -> safeContentPadding()
    ForgeSystemPadding.SafeGestures -> safeGesturesPadding()
    ForgeSystemPadding.SystemBars -> systemBarsPadding()
    ForgeSystemPadding.StatusBars -> statusBarsPadding()
    ForgeSystemPadding.NavigationBars -> navigationBarsPadding()
    ForgeSystemPadding.Ime -> imePadding()
}

/** Applies safe drawing padding for status bars, navigation bars, and cutouts. */
fun Modifier.forgeSafeDrawingPadding(): Modifier =
    forgeSystemPadding(ForgeSystemPadding.SafeDrawing)

/** Applies safe content padding for readable/tappable screen content. */
fun Modifier.forgeSafeContentPadding(): Modifier =
    forgeSystemPadding(ForgeSystemPadding.SafeContent)

/** Applies padding for areas that may conflict with system gestures. */
fun Modifier.forgeSafeGesturesPadding(): Modifier =
    forgeSystemPadding(ForgeSystemPadding.SafeGestures)

/** Applies padding for system status/navigation bars. */
fun Modifier.forgeSystemBarsPadding(): Modifier =
    forgeSystemPadding(ForgeSystemPadding.SystemBars)

/** Applies padding for the on-screen keyboard. Place before scroll modifiers. */
fun Modifier.forgeImePadding(): Modifier = imePadding()
