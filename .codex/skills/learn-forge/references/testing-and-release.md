# Testing and Release

Every interactive component needs behavior and semantics coverage for enabled/disabled state,
callback count, role, label, and selection/range state. Direction-sensitive components also need
RTL coverage. Token tests cover defaults, personalization, and override precedence.
Android visual regression baselines live in `forge-visual-tests/src/test/snapshots` and cover
default/personalized brands, light/dark modes, size presets, loading/feedback, long content, RTL,
and disabled controls. Verify them with `:forge-visual-tests:verifyRoborazziDebug`; intentionally
update them with `:forge-visual-tests:recordRoborazziDebug` only after visual review.

Before release, build Android and iOS, check the Kotlin ABI, validate the repository skill, publish
to an isolated Maven repository, and compile a standalone consumer that resolves only
`io.github.utkuberaty:forge-kit`.

CI commands include `ktlintCheck`, Android `lintDebug` for the visual suite and demo host,
`:forge:verifyPrimitiveTokens`, `:forge:check`, `:forge:checkKotlinAbi`,
`:forge-visual-tests:verifyRoborazziDebug`, Android demo assembly, iOS compilation/linking, and
`python3 scripts/check_ai_guidance.py`. The external consumer lives in `consumer-fixture` and must
never add `project(":forge")`.

Compose behavior and semantics tests use the common test API and execute on iOS Simulator. Android
host tests execute the pure token, validation, feedback, and slider suites.

Public releases are immutable and originate from GitHub Releases tagged `v<version>`. The release
workflow publishes all KMP artifacts to the GitHub Packages Gradle registry with its scoped
`GITHUB_TOKEN`. Consumers authenticate with a classic personal access token carrying
`read:packages`. If the release event is not delivered, manually dispatch `release.yml` with the
existing tag's version; the workflow checks out that tag before publishing. Maven Central
publication is also configured and runs only when Central and PGP secrets are present. Credentials
and signing material are supplied only through CI secrets.
