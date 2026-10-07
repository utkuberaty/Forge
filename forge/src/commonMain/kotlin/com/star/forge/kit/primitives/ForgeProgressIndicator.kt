package com.star.forge.kit.primitives

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import com.star.forge.kit.theme.ForgeProgressSize
import com.star.forge.kit.theme.ForgeTheme

/** Colors used by [ForgeProgressIndicator]. */
@Immutable
public data class ForgeProgressIndicatorColors(
    public val indicator: Color,
    public val track: Color,
    public val icon: Color,
)

/** Defaults for [ForgeProgressIndicator]. */
public object ForgeProgressIndicatorDefaults {
    @Composable
    public fun colors(): ForgeProgressIndicatorColors {
        val visual = ForgeTheme.components.progress.visuals
        return ForgeProgressIndicatorColors(
            indicator = visual.selectedContent ?: ForgeTheme.colors.primary,
            track = visual.container ?: ForgeTheme.colors.surfaceVariant,
            icon = visual.content ?: ForgeTheme.colors.primary,
        )
    }
}

/** Circular determinate or indeterminate progress with optional centered icon. */
@Composable
public fun ForgeProgressIndicator(
    progress: Float? = null,
    modifier: Modifier = Modifier,
    icon: ForgeIconSpec? = null,
    size: ForgeProgressSize = ForgeTheme.components.progress.medium,
    colors: ForgeProgressIndicatorColors = ForgeProgressIndicatorDefaults.colors(),
    accessibilityLabel: String? = null,
) {
    require(progress == null || (progress.isFinite() && progress in 0f..1f)) {
        "progress must be null or a finite value from 0 to 1"
    }
    require(size.visualSize.value > 0f && size.strokeWidth.value > 0f && size.iconSize.value > 0f) {
        "progress dimensions must be positive"
    }
    val transition = rememberInfiniteTransition(label = "ForgeProgress")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
            infiniteRepeatable(
                animation =
                    tween(
                        durationMillis =
                            (
                                ForgeTheme.components.progress.visuals.motionDurationMillis
                                    ?: ForgeTheme.motion.emphasizedDurationMillis
                            ) * 2,
                        easing = ForgeTheme.motion.standardEasing,
                    ),
                repeatMode = RepeatMode.Restart,
            ),
        label = "ForgeProgressRotation",
    )

    Box(
        modifier =
            modifier
                .size(size.visualSize)
                .semantics {
                    accessibilityLabel?.let { contentDescription = it }
                    progressBarRangeInfo = progress?.let {
                        ProgressBarRangeInfo(it, 0f..1f)
                    } ?: ProgressBarRangeInfo.Indeterminate
                },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.size(size.visualSize)) {
            val stroke = size.strokeWidth.toPx()
            val inset = stroke / 2f
            val arcSize = Size(this.size.width - stroke, this.size.height - stroke)
            drawArc(
                color = colors.track,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(stroke, cap = StrokeCap.Round),
            )
            drawArc(
                color = colors.indicator,
                startAngle = if (progress == null) rotation - 90f else -90f,
                sweepAngle = if (progress == null) 96f else 360f * progress,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(stroke, cap = StrokeCap.Round),
            )
        }
        icon?.let {
            ForgeIcon(
                spec = it.copy(contentDescription = null, size = size.iconSize),
                tint = if (it.tint == Color.Unspecified) colors.icon else it.tint,
            )
        }
    }
}
