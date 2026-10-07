package com.star.forgekitdemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.star.forge.kit.theme.ForgeKitTheme
import com.star.forge.kit.theme.ForgeTokenSet
import com.star.forge.kit.theme.ForgeTokenSets
import com.star.forgekitdemo.showcase.component.ShowcaseScaffold
import com.star.forgekitdemo.showcase.registry.ForgeShowcaseRegistry
import com.star.forgekitdemo.showcase.section.ShowcaseSectionContent

/** Shared Android and iOS Forge showcase entry point. */
@Composable
fun ForgeKitDemoApp() {
    var dark by remember { mutableStateOf(false) }
    var personalized by remember { mutableStateOf(false) }
    var rtl by remember { mutableStateOf(false) }
    val events = remember { mutableStateListOf<String>() }
    val defaultTokens = remember { ForgeTokenSets.default() }
    val personalizedTokens = remember { personalizedTokenSet(defaultTokens) }
    val tokenSet = if (personalized) personalizedTokens else defaultTokens

    ForgeKitTheme(tokenSet = tokenSet, darkTheme = dark) {
        CompositionLocalProvider(LocalLayoutDirection provides if (rtl) LayoutDirection.Rtl else LayoutDirection.Ltr) {
            ShowcaseScaffold(
                dark = dark,
                personalized = personalized,
                rtl = rtl,
                events = events,
                onDarkChange = { dark = it },
                onPersonalizedChange = { personalized = it },
                onRtlChange = { rtl = it },
            ) {
                ForgeShowcaseRegistry.sections.forEach { section ->
                    ShowcaseSectionContent(section) { event -> events += event }
                }
            }
        }
    }
}

private fun personalizedTokenSet(base: ForgeTokenSet): ForgeTokenSet =
    ForgeTokenSet(
        light =
            base.light.copy(
                colors =
                    base.light.colors.copy(
                        primary = Color(0xFF7B2CBF),
                        onPrimary = Color.White,
                        primaryContainer = Color(0xFFE9D5FF),
                        onPrimaryContainer = Color(0xFF2D0A45),
                        secondary = Color(0xFFB45309),
                        secondaryContainer = Color(0xFFFFEDD5),
                    ),
            ),
        dark =
            base.dark.copy(
                colors =
                    base.dark.colors.copy(
                        primary = Color(0xFFD8B4FE),
                        onPrimary = Color(0xFF3B0764),
                        primaryContainer = Color(0xFF581C87),
                        onPrimaryContainer = Color(0xFFF3E8FF),
                        secondary = Color(0xFFFDBA74),
                        secondaryContainer = Color(0xFF7C2D12),
                    ),
            ),
    )
