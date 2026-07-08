package com.star.forge.kit.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.star.forge.kit.theme.ForgeTheme

/** Horizontal separator using Forge border tokens by default. */
@Composable
fun ForgeHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = ForgeTheme.borders.thin,
    color: Color = ForgeTheme.colors.border,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .background(color),
    )
}

/** Vertical separator using Forge border tokens by default. */
@Composable
fun ForgeVerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = ForgeTheme.borders.thin,
    color: Color = ForgeTheme.colors.border,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(thickness)
            .background(color),
    )
}
