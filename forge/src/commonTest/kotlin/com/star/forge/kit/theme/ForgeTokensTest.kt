package com.star.forge.kit.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class ForgeTokensTest {
    @Test
    fun defaultSetContainsDistinctLightAndDarkPalettes() {
        val tokens = ForgeTokenSets.default()
        assertNotEquals(tokens.light.colors.background, tokens.dark.colors.background)
        assertEquals(48.dp, tokens.light.touchTargets.minimum)
    }

    @Test
    fun copiedBrandDoesNotMutateTheDefaultBrand() {
        val original = ForgeTokenSets.default()
        val branded =
            original.copy(
                light = original.light.copy(colors = original.light.colors.copy(primary = Color.Magenta)),
            )
        assertEquals(Color.Magenta, branded.light.colors.primary)
        assertNotEquals(Color.Magenta, original.light.colors.primary)
        assertEquals(original.dark, branded.dark)
    }

    @Test
    fun tokenPrecedenceIsDirectThenComponentThenSemanticThenDefault() {
        assertEquals("direct", resolveForgeToken("direct", "component", "semantic", "default"))
        assertEquals("component", resolveForgeToken(null, "component", "semantic", "default"))
        assertEquals("semantic", resolveForgeToken(null, null, "semantic", "default"))
        assertEquals("default", resolveForgeToken(null, null, null, "default"))
    }

    @Test
    fun componentVisualOverridesRemainNullableAndBrandIsolated() {
        val defaults = ForgeTokenSets.light()
        val branded =
            defaults.copy(
                components =
                    defaults.components.copy(
                        button =
                            defaults.components.button.copy(
                                visuals =
                                    defaults.components.button.visuals.copy(
                                        container = Color.Cyan,
                                        pressedOpacity = 0.7f,
                                        motionDurationMillis = 140,
                                    ),
                            ),
                    ),
            )
        assertEquals(Color.Cyan, branded.components.button.visuals.container)
        assertEquals(null, defaults.components.button.visuals.container)
        assertEquals(0.7f, branded.components.button.visuals.pressedOpacity)
    }

    @Test
    fun invalidFoundationTokensFailAtConstruction() {
        assertFailsWith<IllegalArgumentException> { ForgeOpacity(pressed = 1.1f) }
        assertFailsWith<IllegalArgumentException> { ForgeMotion(fastDurationMillis = -1) }
        assertFailsWith<IllegalArgumentException> { ForgeElevation(low = (-1).dp) }
        assertFailsWith<IllegalArgumentException> { ForgeTouchTargets(minimum = 47.dp) }
        assertFailsWith<IllegalArgumentException> { ForgeComponentVisualOverrides(pressedOpacity = -0.1f) }
    }
}
