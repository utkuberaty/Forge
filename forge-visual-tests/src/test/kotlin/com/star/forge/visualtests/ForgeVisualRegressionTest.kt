package com.star.forge.visualtests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import com.star.forge.kit.primitives.ForgeButton
import com.star.forge.kit.primitives.ForgeButtonState
import com.star.forge.kit.primitives.ForgeCheckbox
import com.star.forge.kit.primitives.ForgeFieldFeedback
import com.star.forge.kit.primitives.ForgeProgressIndicator
import com.star.forge.kit.primitives.ForgeSelectionControl
import com.star.forge.kit.primitives.ForgeSelectionRow
import com.star.forge.kit.primitives.ForgeSwitch
import com.star.forge.kit.primitives.ForgeText
import com.star.forge.kit.primitives.ForgeTextField
import com.star.forge.kit.theme.ForgeButtonSize
import com.star.forge.kit.theme.ForgeKitTheme
import com.star.forge.kit.theme.ForgeTheme
import com.star.forge.kit.theme.ForgeTokenSet
import com.star.forge.kit.theme.ForgeTokenSets
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w411dp-h891dp-xxhdpi")
class ForgeVisualRegressionTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun defaultAndPersonalizedLightDarkBrands() {
        capture("brands") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    BrandColumn("Default light", ForgeTokenSets.default(), dark = false)
                    BrandColumn("Default dark", ForgeTokenSets.default(), dark = true)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    BrandColumn("Brand light", personalizedTokens(), dark = false)
                    BrandColumn("Brand dark", personalizedTokens(), dark = true)
                }
            }
        }
    }

    @Test
    fun sizesLoadingAndFeedback() {
        capture("sizes-feedback") {
            ForgeKitTheme {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ForgeButton(onClick = {}, size = ForgeTheme.components.button.small) { ForgeText("Small") }
                    ForgeButton(onClick = {}, size = ForgeTheme.components.button.medium) { ForgeText("Medium") }
                    ForgeButton(onClick = {}, size = ForgeTheme.components.button.large) { ForgeText("Large") }
                    ForgeButton(
                        onClick = {},
                        size = ForgeButtonSize(50.dp, 24.dp, 10.dp, 22.dp),
                    ) { ForgeText("Custom") }
                    ForgeButton(onClick = {}, state = ForgeButtonState.Loading("Saving")) { ForgeText("Save") }
                    ForgeTextField(
                        value = "x",
                        onValueChange = {},
                        label = "Account name",
                        feedback = ForgeFieldFeedback.Invalid("Enter at least three characters"),
                    )
                    ForgeProgressIndicator(progress = 0.64f)
                }
            }
        }
    }

    @Test
    fun longContentRtlAndDisabledControls() {
        capture("long-rtl-disabled") {
            ForgeKitTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ForgeSelectionRow(
                            selected = true,
                            onSelectedChange = {},
                            primaryText = "A deliberately long selection title that wraps across constrained mobile widths",
                            supportingText = "Direction-sensitive layout is rendered right to left.",
                            control = ForgeSelectionControl.Radio,
                        )
                        ForgeSelectionRow(
                            selected = false,
                            onSelectedChange = {},
                            primaryText = "Disabled selection",
                            enabled = false,
                        )
                        ForgeCheckbox(true, onCheckedChange = {}, enabled = false, accessibilityLabel = "Disabled checkbox")
                        ForgeSwitch(true, onCheckedChange = {}, enabled = false, accessibilityLabel = "Disabled switch")
                    }
                }
            }
        }
    }

    private fun capture(
        name: String,
        content: @Composable () -> Unit,
    ) {
        compose.setContent {
            Column(
                Modifier.fillMaxSize().background(Color(0xFFF1F3F4)).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) { content() }
        }
        compose.onRoot().captureRoboImage("src/test/snapshots/$name.png")
    }
}

@Composable
@Suppress("FunctionName")
private fun BrandColumn(
    title: String,
    tokenSet: ForgeTokenSet,
    dark: Boolean,
) {
    ForgeKitTheme(tokenSet = tokenSet, darkTheme = dark) {
        Column(
            Modifier.background(ForgeTheme.colors.background).padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ForgeText(title)
            ForgeButton(onClick = {}) { ForgeText("Primary") }
            ForgeCheckbox(true, onCheckedChange = {}, accessibilityLabel = "Selected")
            ForgeProgressIndicator(progress = 0.72f)
        }
    }
}

private fun personalizedTokens(): ForgeTokenSet {
    val base = ForgeTokenSets.default()
    return base.copy(
        light =
            base.light.copy(
                colors =
                    base.light.colors.copy(
                        primary = Color(0xFF7B2CBF),
                        onPrimary = Color.White,
                        primaryContainer = Color(0xFFE9D5FF),
                    ),
            ),
        dark =
            base.dark.copy(
                colors =
                    base.dark.colors.copy(
                        primary = Color(0xFFFFB3FF),
                        onPrimary = Color(0xFF3B0055),
                        primaryContainer = Color(0xFF5A167F),
                    ),
            ),
    )
}
