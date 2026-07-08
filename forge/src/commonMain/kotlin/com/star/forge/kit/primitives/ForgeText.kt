package com.star.forge.kit.primitives

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.star.forge.kit.theme.ForgeTheme

/**
 * Forge text primitive for plain strings.
 *
 * This renders with Compose Multiplatform [BasicText] and Forge typography
 * tokens, not Material text.
 */
@Composable
fun ForgeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = ForgeTheme.typography.bodyMedium,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = style.withForgeOverrides(
            color = color,
            fontWeight = fontWeight,
            textAlign = textAlign,
        ),
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
    )
}

/**
 * Forge text primitive for annotated strings.
 *
 * Use this overload when a text value contains multiple spans, links, or inline
 * styling.
 */
@Composable
fun ForgeText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = ForgeTheme.typography.bodyMedium,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = style.withForgeOverrides(
            color = color,
            fontWeight = fontWeight,
            textAlign = textAlign,
        ),
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
    )
}

@Composable
private fun TextStyle.withForgeOverrides(
    color: Color,
    fontWeight: FontWeight?,
    textAlign: TextAlign?,
): TextStyle {
    val colorAndWeight = merge(
        TextStyle(
            color = if (color.isSpecified) color else ForgeTheme.contentColor,
            fontWeight = fontWeight,
        )
    )

    return if (textAlign == null) {
        colorAndWeight
    } else {
        colorAndWeight.merge(TextStyle(textAlign = textAlign))
    }
}
