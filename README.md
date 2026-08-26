# Forge Kit

<p align="center">
  <img src="docs/assets/forge-logo.svg" alt="Forge logo" width="96" height="96" />
</p>

Forge Kit is a customizable Compose Multiplatform UI kit for Android and iOS. It provides
low-level, accessible mobile primitives backed by immutable typed design tokens rather than
Material component wrappers or raw design values.

The public package is `io.github.utkuberaty:forge-kit`. Kotlin APIs remain under
`com.star.forge.kit.*`.

## Platforms

- Android API 23 and newer.
- iOS ARM64 devices.
- iOS Simulator ARM64.

Desktop, web, car, and Swift Package Manager distribution are outside the supported scope.

## Installation

Forge `0.1.0` is published to GitHub Packages. GitHub requires authentication when downloading
public packages, so provide a classic personal access token with `read:packages` through Gradle
properties or environment variables:

```kotlin
repositories {
    google()
    maven {
        url = uri("https://maven.pkg.github.com/utkuberaty/Forge")
        credentials {
            username = providers.gradleProperty("gpr.user").orNull ?: System.getenv("GITHUB_ACTOR")
            password = providers.gradleProperty("gpr.key").orNull ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

commonMain.dependencies {
    implementation("io.github.utkuberaty:forge-kit:0.1.0")
}
```

During development, publish `0.1.0-SNAPSHOT` locally with `./gradlew :forge:publishToMavenLocal`.

## Typed personalization

Forge separates foundation tokens, semantic roles, and component tokens. A direct component
argument has highest priority, followed by a component override, a semantic/foundation token, and
the Forge default.

```kotlin
val defaults = ForgeTokenSets.default()
val brand = ForgeTokenSet(
    light = defaults.light.copy(
        colors = defaults.light.colors.copy(primary = Color(0xFF7B2CBF)),
        spacing = defaults.light.spacing.copy(md = 18.dp),
    ),
    dark = defaults.dark.copy(
        colors = defaults.dark.colors.copy(primary = Color(0xFFD8B4FE)),
    ),
)

ForgeKitTheme(tokenSet = brand) {
    ForgeButton(onClick = {}) { ForgeText("Continue") }
}
```

Token objects are immutable and can be stored in Compose state for live theme switching. Forge
does not use JSON token loading, mutable global themes, or a built-in visual token editor.

## Components

Forge includes buttons, icon buttons, symbols, text, images, surfaces, dividers, fields,
checkboxes, switches, sliders, progress indicators, radio buttons, selection rows, and segmented
controls. Interactive visuals may be compact, but every touch target is at least 48dp.

The shared showcase under `:forge-kit-demo` renders the same registry on Android and iOS and
includes default/personalized brands, light/dark modes, RTL, long content, state examples, and an
event log.

## Validation

```bash
./gradlew ktlintCheck :forge-visual-tests:lintDebug :forge-kit-demo-android:lintDebug
./gradlew :forge:verifyPrimitiveTokens :forge:check :forge:checkKotlinAbi
./gradlew :forge-visual-tests:verifyRoborazziDebug
./gradlew :forge-kit-demo-android:assembleDebug
./gradlew :forge-kit-demo:linkDebugFrameworkIosSimulatorArm64
python3 scripts/check_ai_guidance.py
```

The standalone `consumer-fixture` is intentionally outside the main build. CI publishes Forge to
an isolated Maven repository, then compiles that fixture using only the public coordinates.

## Release

Publishing a GitHub Release tagged `v<version>` validates Android/iOS builds, tests, ABI, AI
guidance, screenshots, and isolated package consumption before publishing all KMP artifacts to
GitHub Packages. The workflow uses the repository-scoped `GITHUB_TOKEN`; no package token is
committed. Maven Central publication runs additionally when its credentials and PGP material are
available as GitHub Actions secrets.

See [CONTRIBUTING.md](CONTRIBUTING.md), [CHANGELOG.md](CHANGELOG.md), and
[the roadmap](docs/roadmap.md). Forge is licensed under Apache License 2.0.
