package com.star.forgekitdemo.showcase.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import com.star.forge.kit.components.ForgeBadge
import com.star.forge.kit.components.ForgeBadgeTone
import com.star.forge.kit.components.ForgeCodeBlock
import com.star.forge.kit.components.ForgeNavigationBar
import com.star.forge.kit.components.ForgeNavigationItem
import com.star.forge.kit.components.ForgeStatusPanel
import com.star.forge.kit.components.ForgeStatusTone
import com.star.forge.kit.primitives.ForgeButton
import com.star.forge.kit.primitives.ForgeIconSpec
import com.star.forge.kit.primitives.ForgeSurface
import com.star.forge.kit.primitives.ForgeText
import com.star.forge.kit.primitives.ForgeTextField
import com.star.forge.kit.theme.ForgeTheme

@Composable
internal fun AppComponentsSection(onEvent: (String) -> Unit) {
    var loading by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(0) }
    var opened by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md)) {
        ForgeButton(onClick = { loading = !loading }) { ForgeText("Toggle loading") }
        ForgeStatusPanel(
            title = if (loading) "Loading" else "Nothing here yet",
            message = "Apps supply wording; Forge supplies themed controls and accessibility.",
            loading = loading,
        )
        ForgeStatusPanel(
            title = "Request failed",
            message = "Retry toggles the example above.",
            tone = ForgeStatusTone.Error,
            actionLabel = "Retry example",
            onAction = {
                loading = true
                onEvent("Retry clicked")
            },
        )
        ForgeCodeBlock("ForgeKitTheme { ForgeText(\"Explore\") }")
        val icon = ForgeIconSpec.painter(ColorPainter(ForgeTheme.colors.primary))
        ForgeNavigationBar(
            items =
                listOf(
                    ForgeNavigationItem("Workspace", icon),
                    ForgeNavigationItem("Reference", icon),
                    ForgeNavigationItem("Unavailable", icon, enabled = false),
                ),
            selectedIndex = selected,
            onSelect = {
                selected = it
                onEvent("Navigation: $it")
            },
        )
        ForgeTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = "Search workspace",
            accessibilityLabel = "Workspace search",
            floatingLabelEnabled = false,
        )
        ForgeTextField(value = search, onValueChange = { search = it }, placeholder = "Floating hint", floatingLabelEnabled = true)
        ForgeSurface(
            onClick = {
                opened = !opened
                onEvent("Card opened: $opened")
            },
            modifier = Modifier.fillMaxWidth(),
            accessibilityLabel = "Toggle example card",
        ) {
            Column(Modifier.padding(ForgeTheme.spacing.md), verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm)) {
                ForgeText(if (opened) "Card opened" else "Open this card")
                Row(horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs)) {
                    ForgeBadge("Metadata")
                    ForgeBadge("Selected", tone = ForgeBadgeTone.Primary)
                    ForgeBadge("Ready", tone = ForgeBadgeTone.Success)
                }
            }
        }
        ForgeSurface(onClick = {}, enabled = false, accessibilityLabel = "Unavailable card") {
            ForgeText("Disabled card", Modifier.padding(ForgeTheme.spacing.md))
        }
    }
}
