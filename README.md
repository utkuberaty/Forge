# Forge

<p align="center">
  <img src="docs/assets/forge-logo.svg" alt="Forge logo" width="96" height="96" />
</p>

Forge is the shared Kotlin foundation for `com.star.*` apps and SDKs.

The first module is `:forge`, published under the package family `com.star.forge.*`. It is a Compose Multiplatform library for Android and iOS. Its UI kit lives under `com.star.forge.kit` and currently contains only:

- `com.star.forge.kit.theme`: configurable spacing, radii, borders, colors, typography, and `ForgeKitTheme`.
- `com.star.forge.kit.primitives`: Forge-owned primitives for buttons, icon buttons, icons, symbols, images, checkboxes, switches, sliders, surfaces, text, text fields, and dividers.

Forge primitives should be custom first. We do not use Material buttons, icon buttons, checkboxes, switches, sliders, text fields, surfaces, text, symbols, or dividers.

Accessibility is part of the primitive API. Interactive primitives expose roles, disabled states, labels where needed, text field validation semantics, and slider range/set-progress semantics. Apps should still provide product-specific labels through `ForgeIconSpec.contentDescription` or each primitive's accessibility parameters.

Forms use `ForgeTextField`, a custom `BasicTextField` primitive with Forge-owned validation states, symbol props, colors, borders, helper/error text, and an animated floating hint. Leading and trailing field slots render through `ForgeSymbol`, so they can be plain, styled, or clickable. When a field has no explicit label, its placeholder can become the floating top-left hint.

The first app surface is `ForgeKitDemo`, a useful kit workspace rather than a throwaway demo:

- `:forge-kit-demo`: shared Compose Multiplatform UI and iOS framework.
- `:forge-kit-demo-android`: Android launcher app for the shared demo UI.
- `iosApp`: lightweight SwiftUI host example for the generated iOS framework.

## Package Direction

Apps should use `com.star.{appname}`.

Shared library code should stay under `com.star.forge.*`:

- `com.star.forge.kit` for UI primitives and theme.
- `com.star.forge.sdk` for future app-facing SDK modules.
- `com.star.forge.toolkit` only if a future module needs developer tools rather than app UI.

## Example

```kotlin
ForgeKitTheme(
    spacing = ForgeSpacing(md = 18.dp),
    radii = ForgeRadii(md = 14.dp),
) {
    ForgeButton(onClick = {}) {
        ForgeText("Continue")
    }
}
```

## Build

```bash
./gradlew :forge:assemble
./gradlew :forge-kit-demo-android:assembleDebug
./gradlew :forge-kit-demo:linkDebugFrameworkIosSimulatorArm64
```

## IDE Setup

Open the repository root, not an individual module, in Android Studio.

Install the JetBrains Kotlin Multiplatform IDE plugin in Android Studio. It adds KMP project discovery, environment checks, Android and iOS run configurations, Compose Multiplatform support, and basic Swift navigation/debugging.

Forge currently uses:

- Android Gradle plugin `9.3.0-rc01`
- Gradle wrapper `9.6.1`
- Kotlin `2.4.0`
- Compose Multiplatform `1.11.1`
- Android compile/target SDK `37`

The `AndroidArtifact.getPrivacySandboxSdkInfo()` sync error is an Android Studio and AGP tooling-model mismatch. The Gradle wrapper was already new enough, so the project uses the published AGP 9.3 preview line while the final 9.3 artifact is not yet available from Google Maven.

Useful sync checks:

```bash
./gradlew prepareKotlinIdeaImport :forge:buildKotlinToolingMetadata :forge-kit-demo:buildKotlinToolingMetadata
./gradlew :forge:assemble :forge-kit-demo-android:assembleDebug :forge-kit-demo:linkDebugFrameworkIosSimulatorArm64
```
