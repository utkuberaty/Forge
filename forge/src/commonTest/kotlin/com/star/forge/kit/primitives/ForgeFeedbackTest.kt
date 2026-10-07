package com.star.forge.kit.primitives

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ForgeFeedbackTest {
    @Test
    fun callerOwnsEveryFeedbackMessage() {
        assertEquals("Help", ForgeFieldFeedback.Helper("Help").message)
        assertNull(ForgeFieldFeedback.Checking().message)
        assertNull(ForgeFieldFeedback.Valid().message)
        assertEquals("Required", ForgeFieldFeedback.Invalid("Required").message)
    }
}
