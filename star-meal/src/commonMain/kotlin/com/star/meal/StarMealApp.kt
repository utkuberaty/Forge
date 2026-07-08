package com.star.meal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.star.forge.kit.primitives.ForgeButton
import com.star.forge.kit.primitives.ForgeButtonIcon
import com.star.forge.kit.primitives.ForgeButtonVariant
import com.star.forge.kit.primitives.ForgeCheckbox
import com.star.forge.kit.primitives.ForgeIconSpec
import com.star.forge.kit.primitives.ForgeSurface
import com.star.forge.kit.primitives.ForgeSwitch
import com.star.forge.kit.primitives.ForgeSymbol
import com.star.forge.kit.primitives.ForgeSymbolSize
import com.star.forge.kit.primitives.ForgeSymbolVariant
import com.star.forge.kit.primitives.ForgeText
import com.star.forge.kit.primitives.ForgeTextField
import com.star.forge.kit.primitives.forgeImePadding
import com.star.forge.kit.primitives.forgeSafeDrawingPadding
import com.star.forge.kit.theme.ForgeKitTheme
import com.star.forge.kit.theme.ForgeTheme

/**
 * Entry point for the first real Forge-built app.
 *
 * The app name can change later. The first goal is to pressure-test Forge with
 * a real meal capture workflow: estimate nutrition, show confidence, and flag
 * possible profile-based concerns without pretending to diagnose health issues.
 */
@Composable
fun StarMealApp() {
    ForgeKitTheme {
        StarMealScreen()
    }
}

@Composable
private fun StarMealScreen() {
    val analyzer = remember { RuleBasedMealAnalyzer() }
    var profile by remember { mutableStateOf(HealthProfile()) }
    var mealDescription by remember { mutableStateOf("Greek yogurt bowl with berries and honey") }
    var portionNote by remember { mutableStateOf("medium bowl") }
    var lastEstimate by remember {
        mutableStateOf(analyzer.analyze(mealDescription, portionNote, profile))
    }
    val history = remember {
        mutableStateListOf(
            lastEstimate,
            analyzer.analyze("Chicken salad with avocado", "large plate", profile),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .forgeSafeDrawingPadding()
            .forgeImePadding()
            .verticalScroll(rememberScrollState())
            .padding(ForgeTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.md),
    ) {
        StarMealTopBar()

        SafetyNote()

        CaptureCard(
            mealDescription = mealDescription,
            onMealDescriptionChange = { mealDescription = it },
            portionNote = portionNote,
            onPortionNoteChange = { portionNote = it },
            onAnalyze = {
                lastEstimate = analyzer.analyze(mealDescription, portionNote, profile)
                history.add(0, lastEstimate)
            },
        )

        EstimateCard(estimate = lastEstimate)

        HealthProfileCard(
            profile = profile,
            onProfileChange = { profile = it },
        )

        MealHistoryCard(history = history.take(4))
    }
}

@Composable
private fun StarMealTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
    ) {
        ForgeSymbol(
            icon = ForgeIconSpec.vector(MealIcons.Spark, contentDescription = null),
            variant = ForgeSymbolVariant.Primary,
            size = ForgeSymbolSize.Large,
            shape = RoundedCornerShape(ForgeTheme.radii.md),
        )

        Column(modifier = Modifier.weight(1f)) {
            ForgeText(
                text = "StarMeal",
                style = ForgeTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            ForgeText(
                text = "Meal estimates with profile-aware flags",
                color = ForgeTheme.colors.onSurfaceVariant,
                style = ForgeTheme.typography.bodySmall,
            )
        }

        ForgeSymbol(
            icon = ForgeIconSpec.vector(MealIcons.Profile, contentDescription = "Health profile"),
            variant = ForgeSymbolVariant.Secondary,
            size = ForgeSymbolSize.Medium,
            shape = RoundedCornerShape(ForgeTheme.radii.md),
        )
    }
}

@Composable
private fun SafetyNote() {
    AppCard(
        borderColor = ForgeTheme.colors.warning,
        containerColor = ForgeTheme.colors.warning.copy(alpha = 0.10f),
    ) {
        ForgeText(
            text = "Estimates are for tracking and reflection. Food photos can be ambiguous, so confirm ingredients and portions. This app should not replace medical advice.",
            color = ForgeTheme.colors.onSurface,
            style = ForgeTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun CaptureCard(
    mealDescription: String,
    onMealDescriptionChange: (String) -> Unit,
    portionNote: String,
    onPortionNoteChange: (String) -> Unit,
    onAnalyze: () -> Unit,
) {
    AppCard {
        SectionHeader(
            icon = MealIcons.Camera,
            title = "Capture meal",
            subtitle = "Photo AI will plug into this same analyzer flow.",
        )

        Spacer(Modifier.height(ForgeTheme.spacing.md))

        ForgeTextField(
            value = mealDescription,
            onValueChange = onMealDescriptionChange,
            label = "Meal or visible ingredients",
            placeholder = "Example: rice bowl with chicken and sauce",
            leadingIcon = com.star.forge.kit.primitives.ForgeTextFieldIcon(
                icon = ForgeIconSpec.vector(MealIcons.Food, contentDescription = null),
                symbolSize = ForgeSymbolSize.Small,
                variant = ForgeSymbolVariant.Secondary,
            ),
            supportingText = "In v1, write what the photo would show. Camera capture comes next.",
            singleLine = false,
        )

        Spacer(Modifier.height(ForgeTheme.spacing.sm))

        ForgeTextField(
            value = portionNote,
            onValueChange = onPortionNoteChange,
            label = "Portion note",
            placeholder = "small plate, medium bowl, 2 slices",
            leadingIcon = com.star.forge.kit.primitives.ForgeTextFieldIcon(
                icon = ForgeIconSpec.vector(MealIcons.Scale, contentDescription = null),
                symbolSize = ForgeSymbolSize.Small,
                variant = ForgeSymbolVariant.Secondary,
            ),
        )

        Spacer(Modifier.height(ForgeTheme.spacing.md))

        Row(horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm)) {
            ForgeButton(
                onClick = onAnalyze,
                modifier = Modifier.weight(1f),
                leadingIcon = ForgeButtonIcon(
                    icon = ForgeIconSpec.vector(MealIcons.Spark, contentDescription = null),
                ),
            ) {
                ForgeText("Analyze meal")
            }

            ForgeButton(
                onClick = {},
                modifier = Modifier.weight(1f),
                enabled = false,
                variant = ForgeButtonVariant.Secondary,
                leadingIcon = ForgeButtonIcon(
                    icon = ForgeIconSpec.vector(MealIcons.Camera, contentDescription = null),
                ),
                accessibilityLabel = "Photo capture will be added next",
            ) {
                ForgeText("Photo next")
            }
        }
    }
}

@Composable
private fun EstimateCard(estimate: MealEstimate) {
    AppCard {
        SectionHeader(
            icon = MealIcons.Chart,
            title = "Latest estimate",
            subtitle = "${estimate.confidenceLabel} confidence from ${estimate.sourceLabel}",
        )

        Spacer(Modifier.height(ForgeTheme.spacing.md))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
        ) {
            NutritionMetric(
                label = "Calories",
                value = estimate.nutrition.calories.toString(),
                modifier = Modifier.weight(1f),
            )
            NutritionMetric(
                label = "Protein",
                value = "${estimate.nutrition.proteinGrams}g",
                modifier = Modifier.weight(1f),
            )
            NutritionMetric(
                label = "Carbs",
                value = "${estimate.nutrition.carbsGrams}g",
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(ForgeTheme.spacing.sm))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
        ) {
            NutritionMetric(
                label = "Fat",
                value = "${estimate.nutrition.fatGrams}g",
                modifier = Modifier.weight(1f),
            )
            NutritionMetric(
                label = "Sugar",
                value = "${estimate.nutrition.sugarGrams}g",
                modifier = Modifier.weight(1f),
            )
            NutritionMetric(
                label = "Sodium",
                value = "${estimate.nutrition.sodiumMg}mg",
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(ForgeTheme.spacing.md))

        if (estimate.flags.isEmpty()) {
            EmptyFlagRow()
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs)) {
                estimate.flags.forEach { flag ->
                    HealthFlagRow(flag)
                }
            }
        }
    }
}

@Composable
private fun NutritionMetric(label: String, value: String, modifier: Modifier = Modifier) {
    ForgeSurface(
        modifier = modifier,
        shape = RoundedCornerShape(ForgeTheme.radii.md),
        color = ForgeTheme.colors.surfaceRaised,
        border = BorderStroke(ForgeTheme.borders.thin, ForgeTheme.colors.border),
    ) {
        Column(
            modifier = Modifier.padding(ForgeTheme.spacing.sm),
            verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs),
        ) {
            ForgeText(
                text = value,
                style = ForgeTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
            )
            ForgeText(
                text = label,
                color = ForgeTheme.colors.onSurfaceVariant,
                style = ForgeTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun HealthFlagRow(flag: HealthFlag) {
    val color = when (flag.severity) {
        FlagSeverity.Low -> ForgeTheme.colors.info
        FlagSeverity.Medium -> ForgeTheme.colors.warning
        FlagSeverity.High -> ForgeTheme.colors.error
    }

    ForgeSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ForgeTheme.radii.md),
        color = color.copy(alpha = 0.10f),
        border = BorderStroke(ForgeTheme.borders.thin, color.copy(alpha = 0.50f)),
    ) {
        Row(
            modifier = Modifier.padding(ForgeTheme.spacing.sm),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
        ) {
            ForgeSymbol(
                icon = ForgeIconSpec.vector(MealIcons.Alert, contentDescription = null),
                variant = if (flag.severity == FlagSeverity.High) ForgeSymbolVariant.Danger else ForgeSymbolVariant.Secondary,
                size = ForgeSymbolSize.Small,
                shape = RoundedCornerShape(ForgeTheme.radii.sm),
            )
            Column(verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs)) {
                ForgeText(
                    text = flag.title,
                    fontWeight = FontWeight.SemiBold,
                    style = ForgeTheme.typography.bodyMedium,
                )
                ForgeText(
                    text = flag.message,
                    color = ForgeTheme.colors.onSurfaceVariant,
                    style = ForgeTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun EmptyFlagRow() {
    ForgeSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ForgeTheme.radii.md),
        color = ForgeTheme.colors.success.copy(alpha = 0.10f),
        border = BorderStroke(ForgeTheme.borders.thin, ForgeTheme.colors.success.copy(alpha = 0.50f)),
    ) {
        Row(
            modifier = Modifier.padding(ForgeTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
        ) {
            ForgeCheckbox(
                checked = true,
                onCheckedChange = {},
                enabled = false,
                accessibilityLabel = "No profile concern detected",
            )
            ForgeText(
                text = "No profile concern detected from this estimate.",
                color = ForgeTheme.colors.onSurface,
                style = ForgeTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun HealthProfileCard(
    profile: HealthProfile,
    onProfileChange: (HealthProfile) -> Unit,
) {
    AppCard {
        SectionHeader(
            icon = MealIcons.Profile,
            title = "Health profile",
            subtitle = "Rules used for possible concern flags.",
        )

        Spacer(Modifier.height(ForgeTheme.spacing.md))

        ProfileToggle(
            label = "Nut allergy",
            checked = profile.nutAllergy,
            onCheckedChange = { onProfileChange(profile.copy(nutAllergy = it)) },
        )
        ProfileToggle(
            label = "Lactose sensitivity",
            checked = profile.lactoseSensitive,
            onCheckedChange = { onProfileChange(profile.copy(lactoseSensitive = it)) },
        )
        ProfileToggle(
            label = "Watch sugar",
            checked = profile.watchSugar,
            onCheckedChange = { onProfileChange(profile.copy(watchSugar = it)) },
        )
        ProfileToggle(
            label = "Watch sodium",
            checked = profile.watchSodium,
            onCheckedChange = { onProfileChange(profile.copy(watchSodium = it)) },
        )
    }
}

@Composable
private fun ProfileToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ForgeTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
    ) {
        ForgeText(
            text = label,
            modifier = Modifier.weight(1f),
            style = ForgeTheme.typography.bodyMedium,
        )
        ForgeSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            accessibilityLabel = label,
        )
    }
}

@Composable
private fun MealHistoryCard(history: List<MealEstimate>) {
    AppCard {
        SectionHeader(
            icon = MealIcons.History,
            title = "Recent meals",
            subtitle = "Local session history for now.",
        )

        Spacer(Modifier.height(ForgeTheme.spacing.sm))

        Column(verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm)) {
            history.forEach { estimate ->
                MealHistoryRow(estimate)
            }
        }
    }
}

@Composable
private fun MealHistoryRow(estimate: MealEstimate) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
    ) {
        ForgeSymbol(
            icon = ForgeIconSpec.vector(MealIcons.Food, contentDescription = null),
            variant = ForgeSymbolVariant.Ghost,
            size = ForgeSymbolSize.Small,
        )
        Column(modifier = Modifier.weight(1f)) {
            ForgeText(
                text = estimate.name,
                style = ForgeTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            ForgeText(
                text = "${estimate.nutrition.calories} cal - ${estimate.flags.size} flag(s)",
                color = ForgeTheme.colors.onSurfaceVariant,
                style = ForgeTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun SectionHeader(
    icon: ImageVector,
    title: String,
    subtitle: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.sm),
    ) {
        ForgeSymbol(
            icon = ForgeIconSpec.vector(icon, contentDescription = null),
            variant = ForgeSymbolVariant.Secondary,
            size = ForgeSymbolSize.Medium,
            shape = RoundedCornerShape(ForgeTheme.radii.md),
        )
        Column(modifier = Modifier.weight(1f)) {
            ForgeText(
                text = title,
                style = ForgeTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            ForgeText(
                text = subtitle,
                color = ForgeTheme.colors.onSurfaceVariant,
                style = ForgeTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun AppCard(
    modifier: Modifier = Modifier,
    containerColor: Color = ForgeTheme.colors.surface,
    borderColor: Color = ForgeTheme.colors.border,
    content: @Composable () -> Unit,
) {
    ForgeSurface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ForgeTheme.radii.lg),
        color = containerColor,
        border = BorderStroke(ForgeTheme.borders.thin, borderColor),
    ) {
        Column(
            modifier = Modifier.padding(ForgeTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(ForgeTheme.spacing.xs),
        ) {
            content()
        }
    }
}

private data class HealthProfile(
    val nutAllergy: Boolean = true,
    val lactoseSensitive: Boolean = false,
    val watchSugar: Boolean = true,
    val watchSodium: Boolean = true,
)

private data class MealEstimate(
    val name: String,
    val sourceLabel: String,
    val confidenceLabel: String,
    val nutrition: NutritionEstimate,
    val flags: List<HealthFlag>,
)

private data class NutritionEstimate(
    val calories: Int,
    val proteinGrams: Int,
    val carbsGrams: Int,
    val fatGrams: Int,
    val sugarGrams: Int,
    val sodiumMg: Int,
)

private data class HealthFlag(
    val title: String,
    val message: String,
    val severity: FlagSeverity,
)

private enum class FlagSeverity {
    Low,
    Medium,
    High,
}

private interface MealAnalyzer {
    fun analyze(description: String, portion: String, profile: HealthProfile): MealEstimate
}

private class RuleBasedMealAnalyzer : MealAnalyzer {
    override fun analyze(description: String, portion: String, profile: HealthProfile): MealEstimate {
        val normalized = "$description $portion".lowercase()
        val nutrition = estimateNutrition(normalized)
        val flags = buildList {
            if (profile.nutAllergy && containsAny(normalized, "nut", "peanut", "almond", "walnut", "pesto")) {
                add(
                    HealthFlag(
                        title = "Possible nut allergen",
                        message = "Confirm ingredients before eating. Photos often miss sauces, toppings, and cross-contact.",
                        severity = FlagSeverity.High,
                    )
                )
            }
            if (profile.lactoseSensitive && containsAny(normalized, "milk", "cheese", "cream", "yogurt", "latte")) {
                add(
                    HealthFlag(
                        title = "Possible dairy",
                        message = "This estimate includes dairy-like ingredients. Confirm whether lactose-free options were used.",
                        severity = FlagSeverity.Medium,
                    )
                )
            }
            if (profile.watchSugar && nutrition.sugarGrams >= 24) {
                add(
                    HealthFlag(
                        title = "Higher sugar estimate",
                        message = "Sugar may be elevated from dessert, sauces, juice, honey, or sweet toppings.",
                        severity = FlagSeverity.Medium,
                    )
                )
            }
            if (profile.watchSodium && nutrition.sodiumMg >= 900) {
                add(
                    HealthFlag(
                        title = "Higher sodium estimate",
                        message = "Restaurant meals, cured foods, sauces, and packaged items can push sodium higher than a photo suggests.",
                        severity = FlagSeverity.Low,
                    )
                )
            }
        }

        return MealEstimate(
            name = description.ifBlank { "Untitled meal" },
            sourceLabel = "manual description",
            confidenceLabel = if (description.length > 24 && portion.isNotBlank()) "medium" else "low",
            nutrition = nutrition,
            flags = flags,
        )
    }

    private fun estimateNutrition(text: String): NutritionEstimate {
        val large = containsAny(text, "large", "double", "big")
        val small = containsAny(text, "small", "snack")
        val multiplier = when {
            large -> 1.25f
            small -> 0.72f
            else -> 1f
        }

        val base = when {
            containsAny(text, "burger", "fries", "pizza") -> NutritionEstimate(850, 32, 88, 38, 12, 1250)
            containsAny(text, "salad", "greens", "avocado") -> NutritionEstimate(430, 28, 24, 26, 8, 620)
            containsAny(text, "yogurt", "berries", "honey") -> NutritionEstimate(360, 22, 54, 8, 30, 120)
            containsAny(text, "rice", "bowl", "noodle", "pasta") -> NutritionEstimate(680, 34, 92, 18, 10, 980)
            containsAny(text, "cake", "cookie", "dessert", "ice cream") -> NutritionEstimate(520, 7, 72, 24, 42, 310)
            else -> NutritionEstimate(540, 24, 58, 20, 14, 640)
        }

        return NutritionEstimate(
            calories = (base.calories * multiplier).toInt(),
            proteinGrams = (base.proteinGrams * multiplier).toInt(),
            carbsGrams = (base.carbsGrams * multiplier).toInt(),
            fatGrams = (base.fatGrams * multiplier).toInt(),
            sugarGrams = (base.sugarGrams * multiplier).toInt(),
            sodiumMg = (base.sodiumMg * multiplier).toInt(),
        )
    }

    private fun containsAny(text: String, vararg needles: String): Boolean =
        needles.any { text.contains(it) }
}

private object MealIcons {
    val Spark: ImageVector = ImageVector.Builder(
        name = "Spark",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 2.5f)
            lineTo(14.7f, 9.3f)
            lineTo(21.5f, 12f)
            lineTo(14.7f, 14.7f)
            lineTo(12f, 21.5f)
            lineTo(9.3f, 14.7f)
            lineTo(2.5f, 12f)
            lineTo(9.3f, 9.3f)
            close()
        }
    }.build()

    val Camera: ImageVector = ImageVector.Builder(
        name = "Camera",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(5f, 7f)
            horizontalLineTo(8.2f)
            lineTo(9.8f, 5f)
            horizontalLineTo(14.2f)
            lineTo(15.8f, 7f)
            horizontalLineTo(19f)
            curveTo(20.1f, 7f, 21f, 7.9f, 21f, 9f)
            verticalLineTo(18f)
            curveTo(21f, 19.1f, 20.1f, 20f, 19f, 20f)
            horizontalLineTo(5f)
            curveTo(3.9f, 20f, 3f, 19.1f, 3f, 18f)
            verticalLineTo(9f)
            curveTo(3f, 7.9f, 3.9f, 7f, 5f, 7f)
            close()
            moveTo(12f, 10f)
            curveTo(10.1f, 10f, 8.5f, 11.6f, 8.5f, 13.5f)
            curveTo(8.5f, 15.4f, 10.1f, 17f, 12f, 17f)
            curveTo(13.9f, 17f, 15.5f, 15.4f, 15.5f, 13.5f)
            curveTo(15.5f, 11.6f, 13.9f, 10f, 12f, 10f)
            close()
        }
    }.build()

    val Food: ImageVector = ImageVector.Builder(
        name = "Food",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(4f, 12f)
            curveTo(4f, 8.1f, 7.6f, 5f, 12f, 5f)
            curveTo(16.4f, 5f, 20f, 8.1f, 20f, 12f)
            horizontalLineTo(4f)
            close()
            moveTo(3f, 14f)
            horizontalLineTo(21f)
            verticalLineTo(16f)
            horizontalLineTo(19f)
            curveTo(18.2f, 18.4f, 15.4f, 20f, 12f, 20f)
            curveTo(8.6f, 20f, 5.8f, 18.4f, 5f, 16f)
            horizontalLineTo(3f)
            close()
        }
    }.build()

    val Scale: ImageVector = ImageVector.Builder(
        name = "Scale",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(5f, 5f)
            horizontalLineTo(19f)
            curveTo(20.1f, 5f, 21f, 5.9f, 21f, 7f)
            verticalLineTo(18f)
            curveTo(21f, 19.1f, 20.1f, 20f, 19f, 20f)
            horizontalLineTo(5f)
            curveTo(3.9f, 20f, 3f, 19.1f, 3f, 18f)
            verticalLineTo(7f)
            curveTo(3f, 5.9f, 3.9f, 5f, 5f, 5f)
            close()
            moveTo(12f, 8f)
            curveTo(9.8f, 8f, 8f, 9.8f, 8f, 12f)
            horizontalLineTo(10f)
            curveTo(10f, 10.9f, 10.9f, 10f, 12f, 10f)
            curveTo(13.1f, 10f, 14f, 10.9f, 14f, 12f)
            horizontalLineTo(16f)
            curveTo(16f, 9.8f, 14.2f, 8f, 12f, 8f)
            close()
        }
    }.build()

    val Chart: ImageVector = ImageVector.Builder(
        name = "Chart",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(5f, 19f)
            horizontalLineTo(8f)
            verticalLineTo(11f)
            horizontalLineTo(5f)
            close()
            moveTo(10.5f, 19f)
            horizontalLineTo(13.5f)
            verticalLineTo(5f)
            horizontalLineTo(10.5f)
            close()
            moveTo(16f, 19f)
            horizontalLineTo(19f)
            verticalLineTo(8f)
            horizontalLineTo(16f)
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
            moveTo(12f, 3f)
            lineTo(22f, 20f)
            horizontalLineTo(2f)
            close()
            moveTo(11f, 9f)
            horizontalLineTo(13f)
            verticalLineTo(14f)
            horizontalLineTo(11f)
            close()
            moveTo(11f, 16f)
            horizontalLineTo(13f)
            verticalLineTo(18f)
            horizontalLineTo(11f)
            close()
        }
    }.build()

    val Profile: ImageVector = ImageVector.Builder(
        name = "Profile",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 4f)
            curveTo(14.2f, 4f, 16f, 5.8f, 16f, 8f)
            curveTo(16f, 10.2f, 14.2f, 12f, 12f, 12f)
            curveTo(9.8f, 12f, 8f, 10.2f, 8f, 8f)
            curveTo(8f, 5.8f, 9.8f, 4f, 12f, 4f)
            close()
            moveTo(5f, 20f)
            curveTo(5.6f, 16.8f, 8.5f, 14.5f, 12f, 14.5f)
            curveTo(15.5f, 14.5f, 18.4f, 16.8f, 19f, 20f)
            close()
        }
    }.build()

    val History: ImageVector = ImageVector.Builder(
        name = "History",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 5f)
            curveTo(8.1f, 5f, 5f, 8.1f, 5f, 12f)
            horizontalLineTo(2.5f)
            lineTo(6f, 15.5f)
            lineTo(9.5f, 12f)
            horizontalLineTo(7f)
            curveTo(7f, 9.2f, 9.2f, 7f, 12f, 7f)
            curveTo(14.8f, 7f, 17f, 9.2f, 17f, 12f)
            curveTo(17f, 14.8f, 14.8f, 17f, 12f, 17f)
            curveTo(10.7f, 17f, 9.5f, 16.5f, 8.6f, 15.7f)
            lineTo(7.2f, 17.1f)
            curveTo(8.5f, 18.3f, 10.2f, 19f, 12f, 19f)
            curveTo(15.9f, 19f, 19f, 15.9f, 19f, 12f)
            curveTo(19f, 8.1f, 15.9f, 5f, 12f, 5f)
            close()
            moveTo(11f, 8f)
            horizontalLineTo(13f)
            verticalLineTo(12.3f)
            lineTo(15.8f, 15.1f)
            lineTo(14.4f, 16.5f)
            lineTo(11f, 13.1f)
            close()
        }
    }.build()
}
