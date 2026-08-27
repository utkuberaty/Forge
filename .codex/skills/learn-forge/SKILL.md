---
name: learn-forge
description: Learn, integrate, customize, test, publish, or develop the Forge Compose Multiplatform mobile UI kit, including ForgeKitTheme, typed tokens, primitives, component state, accessibility, and releases. Do not use for unrelated generic Compose work.
---

# Learn Forge

Use this skill to make repository-specific decisions for Forge consumers, component developers,
and release maintainers.

## Workflow

1. When working in the Forge repository, read `AGENTS.md` first.
2. Identify the role:
   - App consumer: integrating Forge or creating a personalized token set.
   - Component developer: changing tokens, primitives, semantics, or the showcase.
   - Release maintainer: validating ABI, package metadata, signing, or Maven publication.
3. Read `references/project-map.md` for module/package boundaries and focused build commands.
4. For token or component work, read `references/tokens-and-components.md`.
5. For tests, CI, package consumption, or releases, read `references/testing-and-release.md`.
6. Prefer examples in `:forge-kit-demo`; trust current repository code over this skill if they
   diverge.
7. Run the narrowest relevant validation command before the full release checks.
   Run formatting and Android static analysis with `ktlintCheck` and the Android `lintDebug`
   tasks listed in `references/testing-and-release.md`.
   Run Compose behavior/semantics tests with `:forge:iosSimulatorArm64Test`.
   Run Android screenshot regression checks with `:forge-visual-tests:verifyRoborazziDebug`.
   Keep Android checks on Linux and Apple linking/tests on macOS; use the repository's bounded
   Gradle memory and worker settings instead of combining every platform in one invocation.
   Release tags publish through GitHub Actions to GitHub Packages; never put package credentials
   in repository files. If release-event delivery fails, dispatch `release.yml` with the existing
   release version so it checks out the immutable tag. Treat empty signing variables as absent;
   only a nonblank PGP key enables signing.

## Invariants

- Forge targets mobile Android and iOS only.
- Product copy, validation logic, navigation, data, and services stay in applications.
- Design decisions resolve through typed tokens; primitives do not introduce raw design values.
- Interactive surfaces are at least 48dp and expose correct Compose semantics.
- Public API or release changes update the relevant agent guidance in the same change.
