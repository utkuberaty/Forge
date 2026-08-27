# Forge Repository Guidance

Forge is a mobile-only Compose Multiplatform UI kit for Android and iOS. Keep reusable code in
`forge/src/commonMain`; Android and iOS source sets are reserved for behavior that genuinely
requires a platform API.

## Architecture

- Kotlin packages remain under `com.star.forge.*`; the public Maven coordinate is
  `io.github.utkuberaty:forge-kit`.
- Theme values follow foundation, semantic, and component-token layers. Resolution order is:
  direct component argument, component token override, semantic/foundation token, Forge default.
- Primitives must not contain raw colors, spacing, type sizes, radii, opacity, elevation, touch
  sizes, or animation durations. Drawing ratios and normalized progress math are allowed.
- Public component state is caller-owned. Forge does not own business validation, navigation,
  persistence, data loading, or product copy.

## Accessibility

- Every interactive component exposes the correct Compose role, enabled/disabled state, and
  selection/range semantics.
- Visual controls may be compact, but their interactive surface must be at least 48dp.
- Do not hard-code spoken state text. Prefer native semantics or caller-provided descriptions.
- Icon-only actions require meaningful caller-provided accessibility labels.

## Public API

- Forge is pre-1.0, but all intentional public API changes must update the Kotlin ABI dump.
- Public declarations use explicit visibility and return types.
- New reusable components must include showcase coverage, behavior tests, semantics tests, and
  documentation in the same change.

## Validation

Use the narrowest relevant command, then run the release checks before publishing:

```bash
./gradlew ktlintCheck :forge-visual-tests:lintDebug :forge-kit-demo-android:lintDebug
./gradlew :forge:check
./gradlew :forge:verifyPrimitiveTokens
./gradlew :forge-visual-tests:verifyRoborazziDebug
./gradlew :forge-kit-demo-android:assembleDebug
./gradlew :forge-kit-demo:linkDebugFrameworkIosSimulatorArm64
./gradlew :forge:iosSimulatorArm64Test
./gradlew :forge:publishToMavenLocal
./gradlew :forge:publishAllPublicationsToGitHubPackagesRepository -PVERSION_NAME=<version>
./gradlew :forge:checkKotlinAbi
python3 scripts/check_ai_guidance.py
```

CI gives Gradle a 3 GiB heap, 1 GiB Metaspace, and at most two workers. Gradle-heavy CI phases
use `--no-daemon` so Android, Apple, and publication checks do not retain idle daemons between
invocations. Linux validates platform-independent and Android work; macOS validates and links iOS.

Validate the repository skill with:

```bash
python3 /Users/utkuyildiz/.codex-personal/skills/.system/skill-creator/scripts/quick_validate.py \
  .codex/skills/learn-forge
```

## Documentation and Skill Synchronization

Changes to public tokens/components, package boundaries, accessibility contracts, supported
platforms, validation commands, publication coordinates, or release workflow must update this
file, `.codex/skills/learn-forge/SKILL.md`, and the relevant skill reference.

GitHub Releases tagged `v<version>` publish immutable KMP artifacts to GitHub Packages using the
workflow `GITHUB_TOKEN`. Never commit package, Maven Central, or signing credentials. Maven Central
publishing is optional until the namespace, credentials, and signing key are configured. If a
release event is not delivered, manually dispatch `release.yml` with the existing tag's version;
the workflow checks out that immutable tag. The manually dispatched workflow supplies its memory
limits on the command line so an older immutable tag receives the current release-runner fix.
Empty CI signing variables must not activate publication signing; signing requires a nonblank PGP
key. Central credentials and signing variables are scoped only to the conditional Maven Central
step, so smoke and GitHub Packages publication never see them.
