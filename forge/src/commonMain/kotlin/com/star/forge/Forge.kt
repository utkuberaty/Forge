package com.star.forge

/**
 * Root metadata for the Forge library family.
 *
 * App modules should use packages like `com.star.{appname}`. Shared library
 * modules should stay under `com.star.forge.*`.
 */
public object Forge {
    /** Maven/package group reserved for Forge libraries. */
    public const val GROUP: String = "io.github.utkuberaty"

    /** Package family for the reusable Forge UI kit. */
    public const val KIT_PACKAGE: String = "com.star.forge.kit"
}
