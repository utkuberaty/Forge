package com.star.forge.sdk.github

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

internal actual fun createGitHubHttpClient(): HttpClient = HttpClient(OkHttp) { configureGitHub() }
