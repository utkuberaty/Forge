package com.star.forgekitdemo.showcase.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import com.star.forge.kit.primitives.ForgeButton
import com.star.forge.kit.primitives.ForgeButtonState
import com.star.forge.kit.primitives.ForgeButtonVariant
import com.star.forge.kit.primitives.ForgeCheckbox
import com.star.forge.kit.primitives.ForgeFieldFeedback
import com.star.forge.kit.primitives.ForgeIconButton
import com.star.forge.kit.primitives.ForgeIconButtonVariant
import com.star.forge.kit.primitives.ForgeIconSpec
import com.star.forge.kit.primitives.ForgeProgressIndicator
import com.star.forge.kit.primitives.ForgeRadioButton
import com.star.forge.kit.primitives.ForgeSegmentedControl
import com.star.forge.kit.primitives.ForgeSegmentedItem
import com.star.forge.kit.primitives.ForgeSelectionControl
import com.star.forge.kit.primitives.ForgeSelectionRow
import com.star.forge.kit.primitives.ForgeSlider
import com.star.forge.kit.primitives.ForgeSwitch
import com.star.forge.kit.primitives.ForgeText
import com.star.forge.kit.primitives.ForgeTextField
import com.star.forge.kit.theme.ForgeButtonSize
import com.star.forge.kit.theme.ForgeTheme
import com.star.forgekitdemo.showcase.component.ShowcaseCard
import com.star.forgekitdemo.showcase.model.ShowcaseSection
import com.star.forgekitdemo.showcase.model.ShowcaseSectionId

@Composable
fun ShowcaseSectionContent(
    section: ShowcaseSection,
    onEvent: (String) -> Unit,
) {
    ShowcaseCard(section.title, section.description) {
        when (section.id) {
            ShowcaseSectionId.Actions -> ActionsSection(onEvent)
            ShowcaseSectionId.Inputs -> InputsSection(onEvent)
            ShowcaseSectionId.Selection -> SelectionSection(onEvent)
            ShowcaseSectionId.Feedback -> FeedbackSection()
        }
    }
}

@Composable
private fun ActionsSection(onEvent: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm)) {
        ForgeButton(onClick = { onEvent("Primary clicked") }, size = ForgeTheme.components.button.small) {
            ForgeText("Small")
        }
        ForgeButton(
            onClick = { onEvent("Custom clicked") },
            size = ForgeButtonSize(48.dp, 22.dp, 10.dp, 20.dp),
            variant = ForgeButtonVariant.Secondary,
        ) { ForgeText("Custom editable size") }
        ForgeButton(
            onClick = { onEvent("Loading click should never fire") },
            state = ForgeButtonState.Loading(label = "Saving"),
        ) { ForgeText("Save") }
        ForgeButton(onClick = {}, enabled = false) { ForgeText("Disabled") }
        ForgeIconButton(
            onClick = { onEvent("Accessible icon action") },
            icon = ForgeIconSpec.painter(ColorPainter(ForgeTheme.colors.primary)),
            accessibilityLabel = "Create item",
            variant = ForgeIconButtonVariant.Outline,
        )
    }
}

@Composable
private fun InputsSection(onEvent: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    var slider by remember { mutableStateOf(0.5f) }
    Column(
        modifier = Modifier.widthIn(max = 420.dp),
        verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
    ) {
        ForgeTextField(
            value = text,
            onValueChange = {
                text = it
                onEvent("Field changed")
            },
            label = "Project name",
            placeholder = "A deliberately long value is supported",
            feedback =
                when {
                    text.isEmpty() -> ForgeFieldFeedback.Helper("Use a name visible to collaborators")
                    text.length < 3 -> ForgeFieldFeedback.Invalid("Enter at least three characters")
                    else -> ForgeFieldFeedback.Valid("Name is available")
                },
            modifier = Modifier.fillMaxWidth(),
        )
        ForgeTextField(
            value = "Checking remotely",
            onValueChange = {},
            feedback = ForgeFieldFeedback.Checking("Checking availability"),
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
        )
        ForgeSlider(
            value = slider,
            onValueChange = { slider = it },
            onValueChangeFinished = { onEvent("Slider finished at $slider") },
            valueRange = 0f..1f,
            steps = 3,
            accessibilityLabel = "Preview scale",
            accessibilityValueDescription = "${(slider * 100).toInt()} percent",
        )
    }
}

@Composable
private fun SelectionSection(onEvent: (String) -> Unit) {
    var checked by remember { mutableStateOf(false) }
    var switched by remember { mutableStateOf(true) }
    var radio by remember { mutableStateOf("first") }
    var segment by remember { mutableStateOf("code") }
    Column(verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm)) {
        Row(horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm)) {
            ForgeCheckbox(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    onEvent("Checkbox: $it")
                },
                accessibilityLabel = "Include analytics",
            )
            ForgeSwitch(
                checked = switched,
                onCheckedChange = {
                    switched = it
                    onEvent("Switch: $it")
                },
                accessibilityLabel = "Automatic updates",
            )
            ForgeRadioButton(
                selected = radio == "first",
                onClick = {
                    radio = "first"
                    onEvent("Radio: first")
                },
                accessibilityLabel = "First option",
            )
        }
        ForgeSelectionRow(
            selected = checked,
            onSelectedChange = {
                checked = it
                onEvent("Selection row: $it")
            },
            primaryText = "Select the whole row",
            supportingText = "Primary and supporting text are merged into one accessible target.",
        )
        ForgeSelectionRow(
            selected = radio == "second",
            onSelectedChange = {
                radio = "second"
                onEvent("Radio row: second")
            },
            primaryText = "A radio selection row with very long content that wraps naturally",
            errorText = if (radio != "second") "Choose this option to clear the example error" else null,
            control = ForgeSelectionControl.Radio,
        )
        ForgeSegmentedControl(
            items =
                listOf(
                    ForgeSegmentedItem("design", "Design"),
                    ForgeSegmentedItem("code", "Code"),
                    ForgeSegmentedItem("disabled", "Unavailable", enabled = false),
                    ForgeSegmentedItem("release", "A long release segment"),
                ),
            selectedId = segment,
            onSelected = {
                segment = it
                onEvent("Segment: $it")
            },
        )
    }
}

@Composable
private fun FeedbackSection() {
    Row(horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.lg)) {
        ForgeProgressIndicator(progress = 0.72f, size = ForgeTheme.components.progress.small, accessibilityLabel = "72 percent")
        ForgeProgressIndicator(progress = 0.42f, size = ForgeTheme.components.progress.large, accessibilityLabel = "42 percent")
        ForgeProgressIndicator(accessibilityLabel = "Loading")
    }
}
