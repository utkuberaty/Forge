package com.star.forge.kit.primitives

import kotlin.test.Test
import kotlin.test.assertEquals

class ForgeSliderMathTest {
    @Test
    fun fractionClampsAtBothEnds() {
        assertEquals(0f, sliderFraction(-5f, 0f..10f))
        assertEquals(0.5f, sliderFraction(5f, 0f..10f))
        assertEquals(1f, sliderFraction(20f, 0f..10f))
    }

    @Test
    fun stepsAndClampingShareOneConversion() {
        assertEquals(0f, snapSliderValue(-1f, 0f..1f, steps = 3))
        assertEquals(0.25f, snapSliderValue(0.2f, 0f..1f, steps = 3))
        assertEquals(0.75f, snapSliderValue(0.7f, 0f..1f, steps = 3))
        assertEquals(1f, snapSliderValue(2f, 0f..1f, steps = 3))
    }

    @Test
    fun rtlMirrorsOnlyTheVisualFraction() {
        assertEquals(0.25f, sliderVisualFraction(0.25f, rtl = false))
        assertEquals(0.75f, sliderVisualFraction(0.25f, rtl = true))
    }
}
