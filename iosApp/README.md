# ForgeKitDemo iOS Host

`ForgeKitDemo` is produced by the `:forge-kit-demo` Kotlin Multiplatform module as an iOS framework and hosted by `iosApp.xcodeproj`.

Build the simulator framework:

```bash
./gradlew :forge-kit-demo:linkDebugFrameworkIosSimulatorArm64
```

Build the iOS host app:

```bash
xcodebuild -project iosApp/iosApp.xcodeproj -scheme ForgeKitDemoIosApp -configuration Debug -destination generic/platform=iOS\ Simulator build
```

The Xcode target runs `./gradlew :forge-kit-demo:embedAndSignAppleFrameworkForXcode` before compiling Swift. Android Studio's Kotlin Multiplatform plugin should discover the shared `ForgeKitDemoIosApp` scheme after opening the repository root and syncing Gradle.

The target uses `iosApp/Info.plist` and sets `CADisableMinimumFrameDurationOnPhone` to `true`. Compose Multiplatform requires this for smooth high-refresh-rate iPhone rendering and will fail fast at startup if the key is missing or false.
