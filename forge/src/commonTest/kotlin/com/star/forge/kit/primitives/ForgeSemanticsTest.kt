package com.star.forge.kit.primitives

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.v2.runComposeUiTest
import com.star.forge.kit.theme.ForgeKitTheme
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class ForgeSemanticsTest {
    @Test
    fun enabledButtonAndIconActionsInvokeOneCallbackWithButtonRole() =
        runComposeUiTest {
            var callbacks = 0
            setContent {
                ForgeKitTheme {
                    ForgeButton(onClick = { callbacks++ }, accessibilityLabel = "Continue") { ForgeText("Continue") }
                    ForgeIconButton(
                        onClick = { callbacks++ },
                        icon = ForgeIconSpec.painter(ColorPainter(Color.Black)),
                        accessibilityLabel = "Add item",
                    )
                }
            }
            onNodeWithContentDescription("Continue")
                .assertHasClickAction()
                .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
                .performClick()
            onNodeWithContentDescription("Add item")
                .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
                .performClick()
            assertEquals(2, callbacks)
        }

    @Test
    fun loadingButtonBlocksClicksAndExposesDisabledState() =
        runComposeUiTest {
            var clicks = 0
            setContent {
                ForgeKitTheme {
                    ForgeButton(
                        onClick = { clicks++ },
                        state = ForgeButtonState.Loading(label = "Saving"),
                        accessibilityLabel = "Save project",
                    ) { ForgeText("Save") }
                }
            }
            onNodeWithContentDescription("Save project").assertIsNotEnabled().performClick()
            assertEquals(0, clicks)
        }

    @Test
    fun checkboxAndRadioExposeToggleAndSelectionSemantics() =
        runComposeUiTest {
            setContent {
                ForgeKitTheme {
                    ForgeCheckbox(true, onCheckedChange = {}, accessibilityLabel = "Analytics")
                    ForgeRadioButton(true, onClick = {}, accessibilityLabel = "Preferred")
                }
            }
            onNodeWithContentDescription("Analytics").assertIsOn()
            onNodeWithContentDescription("Preferred").assertIsSelected()
        }

    @Test
    fun switchExposesLocalizedToggleStateAndDisabledBehavior() =
        runComposeUiTest {
            var calls = 0
            setContent {
                ForgeKitTheme {
                    ForgeSwitch(
                        checked = false,
                        onCheckedChange = { calls++ },
                        enabled = false,
                        accessibilityLabel = "Sync",
                    )
                }
            }
            onNodeWithContentDescription("Sync").assertIsOff().assertIsNotEnabled().performClick()
            assertEquals(0, calls)
        }

    @Test
    fun invalidFieldAlwaysExposesErrorSemantics() =
        runComposeUiTest {
            setContent {
                ForgeKitTheme {
                    ForgeTextField(
                        value = "x",
                        onValueChange = {},
                        label = "Username",
                        feedback = ForgeFieldFeedback.Invalid("Too short"),
                    )
                }
            }
            onNodeWithContentDescription("Username").assert(
                SemanticsMatcher.expectValue(SemanticsProperties.Error, "Too short"),
            )
        }

    @Test
    fun sliderAccessibilityUsesTheSameSteppingAsPointerInput() =
        runComposeUiTest {
            var value = 0f
            var finished = 0
            setContent {
                ForgeKitTheme {
                    ForgeSlider(
                        value = value,
                        onValueChange = { value = it },
                        valueRange = 0f..1f,
                        steps = 3,
                        onValueChangeFinished = { finished++ },
                        accessibilityLabel = "Scale",
                    )
                }
            }
            onNodeWithContentDescription("Scale").performSemanticsAction(SemanticsActions.SetProgress) { action ->
                action(0.7f)
            }
            assertEquals(0.75f, value)
            assertEquals(1, finished)
        }

    @Test
    fun progressExposesDeterminateAndIndeterminateRangeSemantics() =
        runComposeUiTest {
            setContent {
                ForgeKitTheme {
                    ForgeProgressIndicator(progress = 0.5f, accessibilityLabel = "Upload")
                    ForgeProgressIndicator(accessibilityLabel = "Loading")
                }
            }
            onNodeWithContentDescription("Upload").assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ProgressBarRangeInfo,
                    ProgressBarRangeInfo(0.5f, 0f..1f),
                ),
            )
            onNodeWithContentDescription("Loading").assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ProgressBarRangeInfo,
                    ProgressBarRangeInfo.Indeterminate,
                ),
            )
        }

    @Test
    fun wholeSelectionRowAndSegmentsInvokeExactlyOneCallback() =
        runComposeUiTest {
            var rowClicks = 0
            var selected = "one"
            setContent {
                ForgeKitTheme {
                    ForgeSelectionRow(
                        selected = false,
                        onSelectedChange = { rowClicks++ },
                        primaryText = "Whole row",
                    )
                    ForgeSegmentedControl(
                        items = listOf(ForgeSegmentedItem("one", "One"), ForgeSegmentedItem("two", "Two")),
                        selectedId = selected,
                        onSelected = { selected = it },
                    )
                }
            }
            onNodeWithText("Whole row").performClick()
            onNodeWithText("Two").performClick()
            assertEquals(1, rowClicks)
            assertEquals("two", selected)
        }
}
