package com.star.forge.kit.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Forge-owned typography scale.
 *
 * This intentionally uses Compose Multiplatform [TextStyle] primitives instead
 * of Material typography. Apps can replace the whole scale in [ForgeKitTheme].
 */
@Immutable
data class ForgeTypography(
    val headlineMedium: TextStyle = TextStyle(
        fontSize = 28.sp,
        lineHeight = 34.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    val headlineSmall: TextStyle = TextStyle(
        fontSize = 24.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    val titleLarge: TextStyle = TextStyle(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    val titleMedium: TextStyle = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    val titleSmall: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    val bodyLarge: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal,
    ),
    val bodyMedium: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal,
    ),
    val bodySmall: TextStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal,
    ),
    val labelLarge: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    val labelMedium: TextStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    val labelSmall: TextStyle = TextStyle(
        fontSize = 11.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.SemiBold,
    ),
) {
    companion object {
        /** Default Forge typography scale. */
        val default: ForgeTypography = ForgeTypography()
    }
}
