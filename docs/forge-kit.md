# Forge Kit

Forge Kit is a mobile-only Compose Multiplatform design system for Android and iOS. It provides
Forge-owned primitives, immutable typed tokens, and a shared showcase. Product copy, validation,
navigation, data, and services remain in consuming applications.

## Theme and tokens

`ForgeTokens` combines semantic colors with foundation tokens for spacing, radii, borders,
typography, opacity, motion, elevation, and touch targets. `ForgeComponentTokens` provides
component presets and nullable visual overrides. `ForgeTokenSet` supplies paired light and dark
themes.

```kotlin
val base = ForgeTokenSets.default()
val brand = base.copy(
    light = base.light.copy(
        colors = base.light.colors.copy(primary = brandPrimary),
    ),
)

ForgeKitTheme(tokenSet = brand) {
    ForgeButton(onClick = ::save) {
        ForgeText("Save")
    }
}
```

Store immutable token objects in Compose state to switch themes live. Use `ForgeTheme.tokens` for
the complete active set or convenience accessors such as `ForgeTheme.colors`,
`ForgeTheme.spacing`, and `ForgeTheme.components`.

Values resolve in this order:

1. Direct component argument.
2. Component-token override.
3. Semantic or foundation token.
4. Forge default.

## Components

Forge owns low-level rendering for buttons, icon buttons, symbols, text, images, surfaces,
dividers, text fields, checkboxes, switches, sliders, progress indicators, radio buttons,
selection rows, and segmented controls.

- Buttons accept caller-owned `ForgeButtonState`, including loading state and optional loading
  content.
- Fields accept caller-owned `ForgeFieldFeedback` for helper, checking, valid, and invalid states.
- Sliders expose steps, range semantics, press interactions, and `onValueChangeFinished`.
- Selection rows merge their label, supporting/error content, and control into one interactive
  semantics node.
- Segmented controls use stable string IDs and support disabled items and horizontal scrolling.

Visual controls can be smaller than 48dp, while their interactive surface remains at least 48dp.
Icon-only actions require a meaningful caller-provided accessibility label. Native Compose
checked, selected, disabled, radio, and progress semantics are used instead of hard-coded spoken
state text.

## Layout helpers

- Token-aware padding: `Modifier.forgePadding(...)` and related overloads.
- Safe drawing insets: `Modifier.forgeSafeDrawingPadding()`.
- IME insets: `Modifier.forgeImePadding()`.
- Specific inset groups: `Modifier.forgeSystemPadding(...)`.

Android hosts should enable edge-to-edge and use `adjustResize` when text input is present.

## Showcase

`:forge-kit-demo` contains the shared Android/iOS showcase registry. Sections cover token presets,
custom sizes, component states, personalized light/dark brands, constrained and long content,
RTL, accessibility configuration, and callback logging. `:forge-kit-demo-android` and `iosApp`
are platform hosts for the same registry.

Use the showcase as the primary source of integration examples. Repository development and
release rules live only in [`AGENTS.md`](../AGENTS.md).
