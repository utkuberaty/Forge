package com.star.forge.kit.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import com.star.forge.kit.primitives.ForgeSurface
import com.star.forge.kit.primitives.ForgeText
import com.star.forge.kit.theme.ForgeTheme

/** Selectable, horizontally scrollable plain code. It never executes or interprets the text. */
@Composable
public fun ForgeCodeBlock(
    code: String,
    modifier: Modifier = Modifier,
) {
    ForgeSurface(modifier.fillMaxWidth(), color = ForgeTheme.colors.background) {
        SelectionContainer {
            ForgeText(
                text = code,
                modifier = Modifier.horizontalScroll(rememberScrollState()).padding(ForgeTheme.spacing.md),
                style = ForgeTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                softWrap = false,
            )
        }
    }
}
