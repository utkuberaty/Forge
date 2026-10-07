package com.star.forge.kit.theme

/** Internal implementation of Forge's documented customization precedence. */
internal fun <T> resolveForgeToken(
    direct: T?,
    component: T?,
    semantic: T?,
    forgeDefault: T,
): T = direct ?: component ?: semantic ?: forgeDefault
