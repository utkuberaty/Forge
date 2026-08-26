# Tokens and Components

Forge resolves component design through three layers: foundation tokens, semantic color roles,
and component tokens. Per-call arguments override component tokens, which override semantic and
foundation values, which override Forge defaults.

Use immutable `ForgeTokens` and `ForgeTokenSet` values. Projects personalize light and dark token
sets with typed Kotlin `copy` calls and can switch them through Compose state. Do not add JSON,
mutable globals, string-keyed token maps, or silent fallback colors.

Primitives must use token values for colors, spacing, typography, radii, borders, opacity, motion,
elevation, sizes, and touch targets. Mathematical ratios used to draw a checkmark, thumb, or track
are not design tokens.

Keep component state hoisted. Loading labels, validation messages, accessibility descriptions,
and all other product wording belong to callers. Promote app components into Forge only when their
behavior is product-agnostic and reusable.
