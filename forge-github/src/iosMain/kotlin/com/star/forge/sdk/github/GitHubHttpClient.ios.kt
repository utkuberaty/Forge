package com.star.forge.sdk.github

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

internal actual fun createGitHubHttpClient(): HttpClient = HttpClient(Darwin) { configureGitHub() }
