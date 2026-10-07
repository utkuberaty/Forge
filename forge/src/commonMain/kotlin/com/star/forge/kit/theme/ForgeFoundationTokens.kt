package com.star.forge.kit.theme

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
public data class ForgeOpacity(
    public val full: Float = 1f,
    public val pressed: Float = 0.86f,
    public val medium: Float = 0.64f,
    public val disabledContent: Float = 0.38f,
    public val disabledContainer: Float = 0.10f,
    public val subtle: Float = 0.08f,
    public val invisible: Float = 0f,
) {
    init {
        require(
            listOf(full, pressed, medium, disabledContent, disabledContainer, subtle, invisible).all {
                it.isFinite() && it in 0f..1f
            },
        ) { "opacity tokens must be finite values from 0 to 1" }
    }
}

@Immutable
public data class ForgeMotion(
    public val instantDurationMillis: Int = 0,
    public val fastDurationMillis: Int = 100,
    public val normalDurationMillis: Int = 200,
    public val slowDurationMillis: Int = 300,
    public val emphasizedDurationMillis: Int = 450,
    public val standardEasing: Easing = FastOutSlowInEasing,
) {
    init {
        require(
            listOf(instantDurationMillis, fastDurationMillis, normalDurationMillis, slowDurationMillis, emphasizedDurationMillis).all {
                it >=
                    0
            },
        ) {
            "motion durations must be nonnegative"
        }
    }
}

@Immutable
public data class ForgeElevation(
    public val none: Dp = 0.dp,
    public val low: Dp = 2.dp,
    public val medium: Dp = 6.dp,
    public val high: Dp = 12.dp,
) {
    init {
        require(listOf(none, low, medium, high).all { it.value >= 0f }) { "elevation tokens must be nonnegative" }
    }
}

@Immutable
public data class ForgeTouchTargets(
    public val minimum: Dp = 48.dp,
    public val comfortable: Dp = 52.dp,
) {
    init {
        require(minimum.value >= 48f) { "minimum touch target must be at least 48dp" }
        require(comfortable >= minimum) { "comfortable touch target must not be smaller than minimum" }
    }
}
