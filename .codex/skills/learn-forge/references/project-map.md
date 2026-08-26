# Forge Project Map

## Modules

- `:forge`: public Compose Multiplatform library for Android and iOS.
- `:forge-kit-demo`: shared component showcase and iOS framework.
- `:forge-kit-demo-android`: Android host for the shared showcase.
- `:forge-visual-tests`: Android Roborazzi screenshot-regression suite and committed baselines.
- `iosApp`: SwiftUI host for the shared demo framework.

## Packages

- `com.star.forge.kit.theme`: foundation, semantic, component tokens, and theme providers.
- `com.star.forge.kit.primitives`: low-level and composed Forge UI controls.
- `com.star.forgekitdemo`: reusable examples and interactive showcase cases.

## Focused Commands

```bash
./gradlew ktlintCheck :forge-visual-tests:lintDebug :forge-kit-demo-android:lintDebug
./gradlew :forge:check
./gradlew :forge:verifyPrimitiveTokens
./gradlew :forge-visual-tests:verifyRoborazziDebug
./gradlew :forge:assemble
./gradlew :forge-kit-demo-android:assembleDebug
./gradlew :forge-kit-demo:linkDebugFrameworkIosSimulatorArm64
./gradlew :forge:publishToMavenLocal
./gradlew :forge:checkKotlinAbi
python3 scripts/check_ai_guidance.py
```
