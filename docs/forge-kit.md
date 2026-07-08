# Forge Kit

Forge Kit starts with primitives and theme only. App-specific components should be built in app modules first, then promoted into Forge only when two or more apps need the same behavior.

The kit is a Compose Multiplatform library. Shared UI should stay in `commonMain` whenever the same primitive should render on Android and iOS.

## Theme Tokens

The theme is intentionally override-friendly:

- `ForgeSpacing` owns layout rhythm.
- `ForgeRadii` owns corner decisions and derives Forge `Shape` tokens.
- `ForgeBorders` owns border widths and reusable `BorderStroke` creation.
- `ForgeColorSchemes` owns Forge color palettes.
- `ForgeKitTheme` provides tokens through composition locals.

Use `ForgeTheme.colors`, `ForgeTheme.spacing`, `ForgeTheme.radii`, and `ForgeTheme.borders` inside primitives or shared UI.

## Color Meanings

Forge colors use product roles:

- `primary`: main actions and the strongest brand/action color.
- `secondary`: supporting actions and quieter emphasis.
- `tertiary`: accent moments that should not compete with primary actions.
- `surface`: cards, sheets, inputs, and normal UI containers.
- `background`: the screen behind surfaces.
- `error`: destructive actions, failed states, and blocking validation.

Forge semantic colors add roles for status and boundaries:

- `success`: completed, available, approved, or valid.
- `warning`: needs attention, risky, pending, or recoverable.
- `info`: neutral guidance or non-blocking status.
- `border`: subtle dividers, field outlines, and card boundaries.
- `borderStrong`: selected, focused, active, or high-contrast outlines.
- `surfaceRaised`: a surface visually above the normal background.
- `scrimSoft`: overlay behind dialogs, sheets, and blocking popovers.

## Primitive Rules

Primitives should:

- Prefer custom Forge-owned rendering and state over Material component wrappers.
- Keep parameters familiar to Compose users.
- Avoid app-specific copy, navigation, data loading, or business logic.
- Expose customization through parameters, not hidden globals.

Good candidates are buttons, symbols, checkboxes, switches, sliders, text fields, surfaces, text, dividers, chips, loaders, and layout helpers.

Buttons, icon buttons, icons, symbols, images, surfaces, text, dividers, checkboxes, switches, sliders, and text fields are Forge-owned primitives.

## Accessibility

Accessibility belongs in the primitive contract, then app screens add product wording around it:

- Interactive primitives expose Compose roles such as button, checkbox, and switch.
- Disabled controls expose disabled semantics as well as disabled visual colors.
- Icon-only actions should pass a meaningful `ForgeIconSpec.contentDescription`.
- `ForgeButton`, `ForgeCheckbox`, `ForgeSwitch`, `ForgeSlider`, and `ForgeTextField` expose accessibility label/state parameters for cases where visible text is not enough.
- `ForgeSlider` exposes range semantics and set-progress support so assistive tech can read and adjust the value.
- `ForgeTextField` exposes label, validation, disabled, read-only, and error semantics while still using Forge-owned visuals.

## Form Style

Forms should use Forge primitives for structure and input state:

- `ForgeTextField` is a custom `BasicTextField` primitive with Forge colors, typography, radii, borders, cursor, validation state, helper/error text, and symbol slots.
- `ForgeTextFieldIcon` passes a `ForgeIconSpec` into leading or trailing field slots and renders it through `ForgeSymbol`, so callers can control symbol size, shape, background variant, colors, enabled state, and click behavior.
- `ForgeTextFieldState` communicates default, success, warning, and error states without Material defaults.
- Field hints animate: when a field is focused or has a value, `label` floats to the top-left. If `label` is not provided, `placeholder` becomes the floating hint instead.

## System Paddings

System inset handling should go through Forge helpers:

- `Modifier.forgeSafeDrawingPadding()` for screen roots that need to avoid system bars and cutouts.
- `Modifier.forgeImePadding()` for content that must move above the on-screen keyboard. Place it before scroll modifiers.
- `Modifier.forgeSystemPadding(...)` when a screen needs a specific inset group such as status bars, navigation bars, safe content, or safe gestures.

Android demo activities should call `enableEdgeToEdge()` and use `android:windowSoftInputMode="adjustResize"` when text input is present.

## Design Style

Forge UI should feel calm, precise, and tool-like:

- Controls use clear geometry, visible states, and restrained motion.
- Filled controls are reserved for primary or confirmed states.
- Outlines and muted surfaces carry structure without making the UI noisy.
- Icons and images enter through Forge props such as `ForgeIconSpec`, `ForgeButtonIcon`, `ForgeTextFieldIcon`, and `ForgeImageSpec`.
- We do not use Material checkboxes, switches, sliders, buttons, icon buttons, text fields, surfaces, text, symbols, or dividers.

## ForgeKitDemo

`ForgeKitDemo` is the living showcase for the kit. It should feel like a small useful app, not a screenshot gallery.

Current shape:

- `:forge-kit-demo` owns shared Android/iOS Compose UI.
- `:forge-kit-demo-android` owns Android application packaging and launch.
- `iosApp` documents the SwiftUI host entry point for the generated framework.

The app is a token workspace where spacing, radius, density, and theme can be adjusted while primitives update in place. New primitives should be added here when they land in Forge.
