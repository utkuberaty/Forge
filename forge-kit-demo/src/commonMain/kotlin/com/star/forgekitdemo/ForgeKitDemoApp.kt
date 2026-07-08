@file:OptIn(ExperimentalLayoutApi::class)

package com.star.forgekitdemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.star.forge.kit.primitives.ForgeButton
import com.star.forge.kit.primitives.ForgeButtonIcon
import com.star.forge.kit.primitives.ForgeButtonSize
import com.star.forge.kit.primitives.ForgeButtonVariant
import com.star.forge.kit.primitives.ForgeCheckbox
import com.star.forge.kit.primitives.ForgeHorizontalDivider
import com.star.forge.kit.primitives.ForgeIcon
import com.star.forge.kit.primitives.ForgeIconButton
import com.star.forge.kit.primitives.ForgeIconButtonVariant
import com.star.forge.kit.primitives.ForgeIconSpec
import com.star.forge.kit.primitives.ForgeImage
import com.star.forge.kit.primitives.ForgeImageSpec
import com.star.forge.kit.primitives.ForgeSlider
import com.star.forge.kit.primitives.ForgeSurface
import com.star.forge.kit.primitives.ForgeSwitch
import com.star.forge.kit.primitives.ForgeSymbolSize
import com.star.forge.kit.primitives.ForgeSymbolVariant
import com.star.forge.kit.primitives.forgeImePadding
import com.star.forge.kit.primitives.forgeSafeDrawingPadding
import com.star.forge.kit.primitives.ForgeText
import com.star.forge.kit.primitives.ForgeTextField
import com.star.forge.kit.primitives.ForgeTextFieldIcon
import com.star.forge.kit.theme.ForgeKitTheme
import com.star.forge.kit.theme.ForgeRadii
import com.star.forge.kit.theme.ForgeSpacing
import com.star.forge.kit.theme.ForgeTheme
import kotlin.math.roundToInt

/**
 * Shared Compose Multiplatform demo app for Forge Kit.
 *
 * The app is intentionally shaped like a useful design-lab workspace: teams can
 * adjust token values, review primitive states, and use it as a living showcase
 * while Forge grows.
 */
@Composable
fun ForgeKitDemoApp() {
    var darkMode by remember { mutableStateOf(false) }
    var compactMode by remember { mutableStateOf(false) }
    var spacingValue by remember { mutableStateOf(16f) }
    var radiusValue by remember { mutableStateOf(12f) }
    var workspaceName by remember { mutableStateOf("Forge launch board") }
    var releaseReady by remember { mutableStateOf(false) }
    var docsReady by remember { mutableStateOf(true) }
    var iosReady by remember { mutableStateOf(false) }

    val baseSpacing = spacingValue.roundToInt().dp
    val spacing = ForgeSpacing(
        nano = 2.dp,
        xxs = 4.dp,
        xs = if (compactMode) 6.dp else 8.dp,
        sm = if (compactMode) 10.dp else 12.dp,
        md = baseSpacing,
        lg = baseSpacing + 8.dp,
        xl = baseSpacing + 16.dp,
        xxl = baseSpacing + 32.dp,
        xxxl = baseSpacing + 48.dp,
    )
    val radii = ForgeRadii(
        xs = (radiusValue * 0.35f).roundToInt().dp,
        sm = (radiusValue * 0.65f).roundToInt().dp,
        md = radiusValue.roundToInt().dp,
        lg = (radiusValue + 4f).roundToInt().dp,
        xl = (radiusValue + 12f).roundToInt().dp,
    )

    ForgeKitTheme(
        darkTheme = darkMode,
        spacing = spacing,
        radii = radii,
    ) {
        ForgeKitDemoContent(
            darkMode = darkMode,
            compactMode = compactMode,
            spacingValue = spacingValue,
            radiusValue = radiusValue,
            workspaceName = workspaceName,
            releaseReady = releaseReady,
            docsReady = docsReady,
            iosReady = iosReady,
            onDarkModeChange = { darkMode = it },
            onCompactModeChange = { compactMode = it },
            onSpacingValueChange = { spacingValue = it },
            onRadiusValueChange = { radiusValue = it },
            onWorkspaceNameChange = { workspaceName = it },
            onReleaseReadyChange = { releaseReady = it },
            onDocsReadyChange = { docsReady = it },
            onIosReadyChange = { iosReady = it },
        )
    }
}

@Composable
private fun ForgeKitDemoContent(
    darkMode: Boolean,
    compactMode: Boolean,
    spacingValue: Float,
    radiusValue: Float,
    workspaceName: String,
    releaseReady: Boolean,
    docsReady: Boolean,
    iosReady: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onCompactModeChange: (Boolean) -> Unit,
    onSpacingValueChange: (Float) -> Unit,
    onRadiusValueChange: (Float) -> Unit,
    onWorkspaceNameChange: (String) -> Unit,
    onReleaseReadyChange: (Boolean) -> Unit,
    onDocsReadyChange: (Boolean) -> Unit,
    onIosReadyChange: (Boolean) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeTheme.colors.background)
            .forgeSafeDrawingPadding()
            .forgeImePadding()
            .verticalScroll(scrollState)
            .padding(ForgeTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
    ) {
        DemoHeader(darkMode = darkMode, onDarkModeChange = onDarkModeChange)

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
        ) {
            ControlPanel(
                compactMode = compactMode,
                spacingValue = spacingValue,
                radiusValue = radiusValue,
                onCompactModeChange = onCompactModeChange,
                onSpacingValueChange = onSpacingValueChange,
                onRadiusValueChange = onRadiusValueChange,
                modifier = Modifier.widthIn(min = 280.dp, max = 420.dp),
            )

            WorkspacePreview(
                workspaceName = workspaceName,
                releaseReady = releaseReady,
                docsReady = docsReady,
                iosReady = iosReady,
                onWorkspaceNameChange = onWorkspaceNameChange,
                onReleaseReadyChange = onReleaseReadyChange,
                onDocsReadyChange = onDocsReadyChange,
                onIosReadyChange = onIosReadyChange,
                modifier = Modifier.widthIn(min = 320.dp, max = 640.dp),
            )
        }

        PrimitiveGallery()
        TokenReference()
    }
}

@Composable
private fun DemoHeader(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
) {
    ForgeSurface(
        shape = RoundedCornerShape(ForgeTheme.radii.lg),
        color = ForgeTheme.colors.surfaceRaised,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ForgeTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(ForgeTheme.radii.md))
                    .background(ForgeTheme.colors.primary),
                contentAlignment = Alignment.Center,
            ) {
                ForgeIcon(
                    spec = ForgeIconSpec.vector(
                        imageVector = DemoIcons.ForgeMark,
                        contentDescription = null,
                        size = 28.dp,
                    ),
                    modifier = Modifier.size(28.dp),
                    tint = ForgeTheme.colors.onPrimary,
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xxs),
            ) {
                ForgeText(
                    text = "ForgeKitDemo",
                    style = ForgeTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                ForgeText(
                    text = "Adjust tokens, inspect primitives, and shape a real app surface from the kit.",
                    color = ForgeTheme.colors.onSurfaceVariant,
                    style = ForgeTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ForgeText(
                    text = if (darkMode) "Dark" else "Light",
                    style = ForgeTheme.typography.labelLarge,
                )
                ForgeSwitch(checked = darkMode, onCheckedChange = onDarkModeChange)
            }
        }
    }
}

@Composable
private fun ControlPanel(
    compactMode: Boolean,
    spacingValue: Float,
    radiusValue: Float,
    onCompactModeChange: (Boolean) -> Unit,
    onSpacingValueChange: (Float) -> Unit,
    onRadiusValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    ForgeSurface(
        modifier = modifier,
        shape = RoundedCornerShape(ForgeTheme.radii.lg),
        color = ForgeTheme.colors.surfaceRaised,
    ) {
        Column(
            modifier = Modifier.padding(ForgeTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
        ) {
            SectionTitle(
                title = "Token controls",
                subtitle = "Tune the kit values and watch the workspace adapt.",
            )

            ToggleRow(
                title = "Compact density",
                subtitle = "Reduces the smaller spacing steps.",
                checked = compactMode,
                onCheckedChange = onCompactModeChange,
            )

            TokenSlider(
                title = "Base spacing",
                value = spacingValue,
                valueLabel = "${spacingValue.roundToInt()} dp",
                range = 12f..28f,
                onValueChange = onSpacingValueChange,
            )

            TokenSlider(
                title = "Main radius",
                value = radiusValue,
                valueLabel = "${radiusValue.roundToInt()} dp",
                range = 4f..28f,
                onValueChange = onRadiusValueChange,
            )

            ForgeHorizontalDivider()

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs),
                verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs),
            ) {
                StatusPill("Spacing ${spacingValue.roundToInt()}dp", ForgeTheme.colors.primary, ForgeTheme.colors.onPrimary)
                StatusPill("Radius ${radiusValue.roundToInt()}dp", ForgeTheme.colors.info, ForgeTheme.colors.onInfo)
                StatusPill(if (compactMode) "Compact" else "Comfort", ForgeTheme.colors.success, ForgeTheme.colors.onSuccess)
            }
        }
    }
}

@Composable
private fun WorkspacePreview(
    workspaceName: String,
    releaseReady: Boolean,
    docsReady: Boolean,
    iosReady: Boolean,
    onWorkspaceNameChange: (String) -> Unit,
    onReleaseReadyChange: (Boolean) -> Unit,
    onDocsReadyChange: (Boolean) -> Unit,
    onIosReadyChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    ForgeSurface(
        modifier = modifier,
        shape = RoundedCornerShape(ForgeTheme.radii.lg),
        color = ForgeTheme.colors.surfaceRaised,
    ) {
        Column(
            modifier = Modifier.padding(ForgeTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
        ) {
            SectionTitle(
                title = "Workspace",
                subtitle = "A useful board for reviewing kit readiness across apps.",
            )

            ForgeTextField(
                value = workspaceName,
                onValueChange = onWorkspaceNameChange,
                label = "Workspace name",
                placeholder = "Name this kit board",
                leadingIcon = ForgeTextFieldIcon(
                    icon = ForgeIconSpec.vector(
                        imageVector = DemoIcons.ForgeMark,
                        contentDescription = null,
                    ),
                    symbolSize = ForgeSymbolSize.Small,
                    variant = ForgeSymbolVariant.Secondary,
                    shape = RoundedCornerShape(ForgeTheme.radii.sm),
                ),
                trailingIcon = if (workspaceName.isNotEmpty()) {
                    ForgeTextFieldIcon(
                        icon = ForgeIconSpec.vector(
                            imageVector = DemoIcons.Close,
                            contentDescription = "Clear workspace name",
                        ),
                        symbolSize = ForgeSymbolSize.Small,
                        variant = ForgeSymbolVariant.Ghost,
                        onClick = { onWorkspaceNameChange("") },
                    )
                } else {
                    null
                },
                supportingText = "Custom Forge BasicTextField with an animated floating hint.",
                modifier = Modifier.fillMaxWidth(),
            )

            Column(verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm)) {
                ChecklistItem(
                    title = "Primitive states reviewed",
                    subtitle = "Buttons, fields, surfaces, text, and dividers",
                    checked = releaseReady,
                    onCheckedChange = onReleaseReadyChange,
                )
                ChecklistItem(
                    title = "KDoc explains token meaning",
                    subtitle = "Color roles, spacing, borders, and radii are documented",
                    checked = docsReady,
                    onCheckedChange = onDocsReadyChange,
                )
                ChecklistItem(
                    title = "iOS framework builds",
                    subtitle = "Shared UI is ready to host from SwiftUI",
                    checked = iosReady,
                    onCheckedChange = onIosReadyChange,
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
                verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
            ) {
                ForgeButton(
                    onClick = {},
                    variant = ForgeButtonVariant.Primary,
                    leadingIcon = ForgeButtonIcon(ForgeIconSpec.vector(DemoIcons.Check)),
                ) {
                    ForgeText("Save snapshot")
                }
                ForgeButton(
                    onClick = {},
                    variant = ForgeButtonVariant.Outline,
                    trailingIcon = ForgeButtonIcon(ForgeIconSpec.vector(DemoIcons.Export)),
                ) {
                    ForgeText("Export tokens")
                }
                ForgeButton(
                    onClick = {},
                    variant = ForgeButtonVariant.Ghost,
                    leadingIcon = ForgeButtonIcon(ForgeIconSpec.vector(DemoIcons.Reset)),
                ) {
                    ForgeText("Reset")
                }
            }
        }
    }
}

@Composable
private fun PrimitiveGallery() {
    ForgeSurface(
        shape = RoundedCornerShape(ForgeTheme.radii.lg),
        color = ForgeTheme.colors.surfaceRaised,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ForgeTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
        ) {
            SectionTitle(
                title = "Primitive gallery",
                subtitle = "Current Forge primitives shown in real states.",
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
                verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
            ) {
                ForgeButton(
                    onClick = {},
                    variant = ForgeButtonVariant.Primary,
                    leadingIcon = ForgeButtonIcon(ForgeIconSpec.vector(DemoIcons.Plus)),
                ) {
                    ForgeText("Primary")
                }
                ForgeButton(
                    onClick = {},
                    variant = ForgeButtonVariant.Secondary,
                    trailingIcon = ForgeButtonIcon(ForgeIconSpec.vector(DemoIcons.ArrowRight)),
                ) {
                    ForgeText("Secondary")
                }
                ForgeButton(onClick = {}, variant = ForgeButtonVariant.Outline) {
                    ForgeText("Outline")
                }
                ForgeButton(onClick = {}, variant = ForgeButtonVariant.Ghost) {
                    ForgeText("Ghost")
                }
                ForgeButton(onClick = {}, variant = ForgeButtonVariant.Danger) {
                    ForgeText("Danger")
                }
                ForgeButton(
                    onClick = {},
                    enabled = false,
                    size = ForgeButtonSize.Small,
                ) {
                    ForgeText("Disabled")
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ForgeIconButton(
                    onClick = {},
                    icon = ForgeIconSpec.vector(DemoIcons.Plus, contentDescription = "Add"),
                    variant = ForgeIconButtonVariant.Primary,
                )
                ForgeIconButton(
                    onClick = {},
                    icon = ForgeIconSpec.vector(DemoIcons.Info, contentDescription = "Info"),
                    variant = ForgeIconButtonVariant.Tonal,
                )
                ForgeIconButton(
                    onClick = {},
                    icon = ForgeIconSpec.vector(DemoIcons.Help, contentDescription = "Help"),
                    variant = ForgeIconButtonVariant.Outline,
                )
                ForgeIconButton(
                    onClick = {},
                    icon = ForgeIconSpec.vector(DemoIcons.Alert, contentDescription = "Alert"),
                    variant = ForgeIconButtonVariant.Danger,
                )
            }

            ForgeHorizontalDivider()

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
            ) {
                SampleMetric("12", "spacing tokens")
                SampleMetric("7", "radius tokens")
                SampleMetric("4", "border widths")
                SampleMetric("10", "color roles")
            }

            ForgeImagePreview()
        }
    }
}

@Composable
private fun TokenReference() {
    ForgeSurface(
        shape = RoundedCornerShape(ForgeTheme.radii.lg),
        color = ForgeTheme.colors.surfaceRaised,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ForgeTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
        ) {
            SectionTitle(
                title = "Color meaning",
                subtitle = "Semantic colors describe purpose, not decoration.",
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
                verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
            ) {
                ColorRole("Success", "Completed, valid, available", ForgeTheme.colors.success, ForgeTheme.colors.onSuccess)
                ColorRole("Warning", "Needs attention or caution", ForgeTheme.colors.warning, ForgeTheme.colors.onWarning)
                ColorRole("Info", "Guidance and neutral status", ForgeTheme.colors.info, ForgeTheme.colors.onInfo)
                ColorRole("Border", "Dividers and low-emphasis outlines", ForgeTheme.colors.border, ForgeTheme.colors.onSurface)
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    subtitle: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xxs)) {
        ForgeText(
            text = title,
            style = ForgeTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        ForgeText(
            text = subtitle,
            color = ForgeTheme.colors.onSurfaceVariant,
            style = ForgeTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun ToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xxs),
        ) {
            ForgeText(text = title, fontWeight = FontWeight.SemiBold)
            ForgeText(
                text = subtitle,
                color = ForgeTheme.colors.onSurfaceVariant,
                style = ForgeTheme.typography.bodySmall,
            )
        }
        ForgeSwitch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun TokenSlider(
    title: String,
    value: Float,
    valueLabel: String,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ForgeText(text = title, fontWeight = FontWeight.SemiBold)
            ForgeText(
                text = valueLabel,
                color = ForgeTheme.colors.onSurfaceVariant,
                style = ForgeTheme.typography.labelLarge,
            )
        }
        ForgeSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
        )
    }
}

@Composable
private fun ChecklistItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    ForgeSurface(
        shape = RoundedCornerShape(ForgeTheme.radii.md),
        color = ForgeTheme.colors.surface,
        border = ForgeTheme.borders.stroke(
            color = if (checked) ForgeTheme.colors.borderStrong else ForgeTheme.colors.border,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ForgeTheme.spacing.sm,
                    vertical = ForgeTheme.spacing.xs,
                ),
            horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ForgeCheckbox(checked = checked, onCheckedChange = onCheckedChange)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xxs),
            ) {
                ForgeText(text = title, fontWeight = FontWeight.SemiBold)
                ForgeText(
                    text = subtitle,
                    color = ForgeTheme.colors.onSurfaceVariant,
                    style = ForgeTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun StatusPill(
    text: String,
    color: Color,
    contentColor: Color,
) {
    ForgeSurface(
        shape = CircleShape,
        color = color,
        contentColor = contentColor,
        border = null,
    ) {
        ForgeText(
            text = text,
            modifier = Modifier.padding(
                horizontal = ForgeTheme.spacing.sm,
                vertical = ForgeTheme.spacing.xxs,
            ),
            color = contentColor,
            style = ForgeTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun SampleMetric(
    value: String,
    label: String,
) {
    ForgeSurface(
        modifier = Modifier.widthIn(min = 136.dp),
        shape = RoundedCornerShape(ForgeTheme.radii.md),
        color = ForgeTheme.colors.surface,
    ) {
        Column(
            modifier = Modifier.padding(ForgeTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs),
        ) {
            ForgeText(
                text = value,
                style = ForgeTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            ForgeText(
                text = label,
                color = ForgeTheme.colors.onSurfaceVariant,
                style = ForgeTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun ColorRole(
    name: String,
    meaning: String,
    color: Color,
    contentColor: Color,
) {
    ForgeSurface(
        modifier = Modifier.widthIn(min = 220.dp, max = 280.dp),
        shape = RoundedCornerShape(ForgeTheme.radii.md),
        color = ForgeTheme.colors.surface,
    ) {
        Row(
            modifier = Modifier.padding(ForgeTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(ForgeTheme.radii.sm))
                    .background(color),
                contentAlignment = Alignment.Center,
            ) {
                Spacer(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(contentColor),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xxs)) {
                ForgeText(text = name, fontWeight = FontWeight.SemiBold)
                ForgeText(
                    text = meaning,
                    color = ForgeTheme.colors.onSurfaceVariant,
                    style = ForgeTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun ForgeImagePreview() {
    ForgeSurface(
        shape = RoundedCornerShape(ForgeTheme.radii.md),
        color = ForgeTheme.colors.surface,
    ) {
        Row(
            modifier = Modifier.padding(ForgeTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(ForgeTheme.radii.md)),
                contentAlignment = Alignment.Center,
            ) {
                ForgeImage(
                    spec = ForgeImageSpec(
                        painter = ColorPainter(ForgeTheme.colors.primaryContainer),
                        contentDescription = "Forge generated image placeholder",
                        contentScale = ContentScale.Crop,
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
                ForgeIcon(
                    spec = ForgeIconSpec.vector(DemoIcons.ForgeMark, size = 32.dp),
                    modifier = Modifier.size(32.dp),
                    tint = ForgeTheme.colors.onPrimaryContainer,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xxs)) {
                ForgeText(text = "ForgeImage", fontWeight = FontWeight.SemiBold)
                ForgeText(
                    text = "Painter-backed imagery uses ForgeImageSpec, so app visuals stay replaceable.",
                    color = ForgeTheme.colors.onSurfaceVariant,
                    style = ForgeTheme.typography.bodySmall,
                )
            }
        }
    }
}

private object DemoIcons {
    val ForgeMark: ImageVector = ImageVector.Builder(
        name = "ForgeMark",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(4f, 4f)
            horizontalLineTo(20f)
            verticalLineTo(7f)
            horizontalLineTo(8f)
            verticalLineTo(11f)
            horizontalLineTo(18f)
            verticalLineTo(14f)
            horizontalLineTo(8f)
            verticalLineTo(20f)
            horizontalLineTo(4f)
            close()
        }
    }.build()

    val Plus: ImageVector = ImageVector.Builder(
        name = "Plus",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(11f, 5f)
            horizontalLineTo(13f)
            verticalLineTo(11f)
            horizontalLineTo(19f)
            verticalLineTo(13f)
            horizontalLineTo(13f)
            verticalLineTo(19f)
            horizontalLineTo(11f)
            verticalLineTo(13f)
            horizontalLineTo(5f)
            verticalLineTo(11f)
            horizontalLineTo(11f)
            close()
        }
    }.build()

    val Check: ImageVector = ImageVector.Builder(
        name = "Check",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(9.4f, 16.6f)
            lineTo(4.8f, 12f)
            lineTo(6.2f, 10.6f)
            lineTo(9.4f, 13.8f)
            lineTo(17.8f, 5.4f)
            lineTo(19.2f, 6.8f)
            close()
        }
    }.build()

    val Close: ImageVector = ImageVector.Builder(
        name = "Close",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(6.4f, 5f)
            lineTo(12f, 10.6f)
            lineTo(17.6f, 5f)
            lineTo(19f, 6.4f)
            lineTo(13.4f, 12f)
            lineTo(19f, 17.6f)
            lineTo(17.6f, 19f)
            lineTo(12f, 13.4f)
            lineTo(6.4f, 19f)
            lineTo(5f, 17.6f)
            lineTo(10.6f, 12f)
            lineTo(5f, 6.4f)
            close()
        }
    }.build()

    val Export: ImageVector = ImageVector.Builder(
        name = "Export",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(13f, 4f)
            horizontalLineTo(20f)
            verticalLineTo(11f)
            horizontalLineTo(18f)
            verticalLineTo(7.4f)
            lineTo(10.7f, 14.7f)
            lineTo(9.3f, 13.3f)
            lineTo(16.6f, 6f)
            horizontalLineTo(13f)
            close()
            moveTo(5f, 6f)
            horizontalLineTo(11f)
            verticalLineTo(8f)
            horizontalLineTo(7f)
            verticalLineTo(17f)
            horizontalLineTo(16f)
            verticalLineTo(13f)
            horizontalLineTo(18f)
            verticalLineTo(19f)
            horizontalLineTo(5f)
            close()
        }
    }.build()

    val Reset: ImageVector = ImageVector.Builder(
        name = "Reset",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 5f)
            curveTo(15.3f, 5f, 18f, 7.7f, 18f, 11f)
            curveTo(18f, 14.3f, 15.3f, 17f, 12f, 17f)
            curveTo(9.8f, 17f, 7.9f, 15.8f, 6.8f, 14f)
            lineTo(8.6f, 13f)
            curveTo(9.3f, 14.2f, 10.6f, 15f, 12f, 15f)
            curveTo(14.2f, 15f, 16f, 13.2f, 16f, 11f)
            curveTo(16f, 8.8f, 14.2f, 7f, 12f, 7f)
            curveTo(10.5f, 7f, 9.2f, 7.8f, 8.5f, 9f)
            horizontalLineTo(11f)
            verticalLineTo(11f)
            horizontalLineTo(5f)
            verticalLineTo(5f)
            horizontalLineTo(7f)
            verticalLineTo(7.4f)
            curveTo(8.1f, 5.9f, 9.9f, 5f, 12f, 5f)
            close()
        }
    }.build()

    val ArrowRight: ImageVector = ImageVector.Builder(
        name = "ArrowRight",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(13f, 5f)
            lineTo(20f, 12f)
            lineTo(13f, 19f)
            lineTo(11.6f, 17.6f)
            lineTo(16.2f, 13f)
            horizontalLineTo(4f)
            verticalLineTo(11f)
            horizontalLineTo(16.2f)
            lineTo(11.6f, 6.4f)
            close()
        }
    }.build()

    val Info: ImageVector = ImageVector.Builder(
        name = "Info",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(11f, 10f)
            horizontalLineTo(13f)
            verticalLineTo(18f)
            horizontalLineTo(11f)
            close()
            moveTo(11f, 6f)
            horizontalLineTo(13f)
            verticalLineTo(8f)
            horizontalLineTo(11f)
            close()
            moveTo(12f, 2f)
            curveTo(6.5f, 2f, 2f, 6.5f, 2f, 12f)
            curveTo(2f, 17.5f, 6.5f, 22f, 12f, 22f)
            curveTo(17.5f, 22f, 22f, 17.5f, 22f, 12f)
            curveTo(22f, 6.5f, 17.5f, 2f, 12f, 2f)
            close()
            moveTo(12f, 20f)
            curveTo(7.6f, 20f, 4f, 16.4f, 4f, 12f)
            curveTo(4f, 7.6f, 7.6f, 4f, 12f, 4f)
            curveTo(16.4f, 4f, 20f, 7.6f, 20f, 12f)
            curveTo(20f, 16.4f, 16.4f, 20f, 12f, 20f)
            close()
        }
    }.build()

    val Help: ImageVector = ImageVector.Builder(
        name = "Help",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(11f, 17f)
            horizontalLineTo(13f)
            verticalLineTo(19f)
            horizontalLineTo(11f)
            close()
            moveTo(12f, 5f)
            curveTo(9.8f, 5f, 8f, 6.5f, 8f, 8.7f)
            horizontalLineTo(10f)
            curveTo(10f, 7.7f, 10.8f, 7f, 12f, 7f)
            curveTo(13.2f, 7f, 14f, 7.7f, 14f, 8.8f)
            curveTo(14f, 10f, 13.4f, 10.5f, 12.4f, 11.2f)
            curveTo(11.2f, 12f, 11f, 12.9f, 11f, 14f)
            verticalLineTo(15f)
            horizontalLineTo(13f)
            verticalLineTo(14.2f)
            curveTo(13f, 13.4f, 13.3f, 12.9f, 14.2f, 12.2f)
            curveTo(15.3f, 11.4f, 16f, 10.4f, 16f, 8.8f)
            curveTo(16f, 6.6f, 14.3f, 5f, 12f, 5f)
            close()
        }
    }.build()

    val Alert: ImageVector = ImageVector.Builder(
        name = "Alert",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(11f, 7f)
            horizontalLineTo(13f)
            verticalLineTo(13f)
            horizontalLineTo(11f)
            close()
            moveTo(11f, 15f)
            horizontalLineTo(13f)
            verticalLineTo(17f)
            horizontalLineTo(11f)
            close()
            moveTo(12f, 2f)
            lineTo(22f, 20f)
            horizontalLineTo(2f)
            close()
            moveTo(12f, 6.1f)
            lineTo(5.4f, 18f)
            horizontalLineTo(18.6f)
            close()
        }
    }.build()
}
