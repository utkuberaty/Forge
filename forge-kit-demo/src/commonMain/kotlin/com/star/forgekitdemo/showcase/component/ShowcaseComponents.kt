package com.star.forgekitdemo.showcase.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.star.forge.kit.primitives.ForgeButton
import com.star.forge.kit.primitives.ForgeButtonVariant
import com.star.forge.kit.primitives.ForgeSurface
import com.star.forge.kit.primitives.ForgeText
import com.star.forge.kit.theme.ForgeTheme

@Composable
fun ShowcaseScaffold(
    dark: Boolean,
    personalized: Boolean,
    rtl: Boolean,
    events: List<String>,
    onDarkChange: (Boolean) -> Unit,
    onPersonalizedChange: (Boolean) -> Unit,
    onRtlChange: (Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(ForgeTheme.colors.background)
                .verticalScroll(rememberScrollState())
                .padding(ForgeTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
    ) {
        ForgeText("Forge mobile component showcase", style = ForgeTheme.typography.headlineMedium)
        ForgeText(
            "The same registry renders on Android and iOS. Every control below reports callbacks.",
            color = ForgeTheme.colors.onSurfaceVariant,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm)) {
            ForgeButton(onClick = { onDarkChange(!dark) }, variant = ForgeButtonVariant.Outline) {
                ForgeText(if (dark) "Light" else "Dark")
            }
            ForgeButton(onClick = { onPersonalizedChange(!personalized) }, variant = ForgeButtonVariant.Outline) {
                ForgeText(if (personalized) "Default brand" else "Personalized brand")
            }
            ForgeButton(onClick = { onRtlChange(!rtl) }, variant = ForgeButtonVariant.Outline) {
                ForgeText(if (rtl) "LTR" else "RTL")
            }
        }
        content()
        ForgeSurface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(ForgeTheme.radii.md),
            color = ForgeTheme.colors.surfaceRaised,
        ) {
            Column(
                Modifier.padding(ForgeTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs),
            ) {
                ForgeText("Event log", style = ForgeTheme.typography.titleMedium)
                if (events.isEmpty()) ForgeText("No callbacks yet", color = ForgeTheme.colors.onSurfaceVariant)
                events.takeLast(6).forEach { ForgeText(it, style = ForgeTheme.typography.bodySmall) }
            }
        }
    }
}

@Composable
fun ShowcaseCard(
    title: String,
    description: String,
    content: @Composable () -> Unit,
) {
    ForgeSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ForgeTheme.radii.lg),
        color = ForgeTheme.colors.surfaceRaised,
    ) {
        Column(
            Modifier.padding(ForgeTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
        ) {
            ForgeText(title, style = ForgeTheme.typography.titleLarge)
            ForgeText(description, color = ForgeTheme.colors.onSurfaceVariant)
            content()
        }
    }
}
