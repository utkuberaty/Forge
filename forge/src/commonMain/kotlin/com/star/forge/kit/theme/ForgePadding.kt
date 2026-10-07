package com.star.forge.kit.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
public fun Modifier.forgePadding(
    horizontal: Dp = ForgeTheme.spacing.zero,
    vertical: Dp = ForgeTheme.spacing.zero,
): Modifier = padding(horizontal = horizontal, vertical = vertical)

@Composable
public fun Modifier.forgePadding(
    start: Dp = ForgeTheme.spacing.zero,
    top: Dp = ForgeTheme.spacing.zero,
    end: Dp = ForgeTheme.spacing.zero,
    bottom: Dp = ForgeTheme.spacing.zero,
): Modifier = padding(start = start, top = top, end = end, bottom = bottom)

@Composable
public fun ForgePaddingValues(
    horizontal: Dp = ForgeTheme.spacing.zero,
    vertical: Dp = ForgeTheme.spacing.zero,
): PaddingValues = PaddingValues(horizontal = horizontal, vertical = vertical)
