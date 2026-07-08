package com.star.forge

/**
 * Root metadata for the Forge library family.
 *
 * App modules should use packages like `com.star.{appname}`. Shared library
 * modules should stay under `com.star.forge.*`.
 */
object Forge {
    /** Maven/package group reserved for Forge libraries. */
    const val GROUP = "com.star.forge"

    /** Package family for the reusable Forge UI kit. */
    const val KIT_PACKAGE = "com.star.forge.kit"
}
