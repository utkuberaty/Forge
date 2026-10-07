package com.star.forge.sdk.github

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Read-only GitHub REST client for public repositories. No credentials are required.
 *
 * The default client uses OkHttp on Android/JVM and Darwin on iOS, with a 20-second
 * timeout. Inject an [httpClient] for tests or application-specific configuration.
 * The caller owns an injected client; [close] only closes a client created here.
 * Methods do not retry automatically or swallow coroutine cancellation.
 */
class GitHubClient private constructor(
    private val httpClient: HttpClient,
    private val ownsClient: Boolean,
) {
    constructor() : this(createGitHubHttpClient(), true)
    constructor(httpClient: HttpClient) : this(httpClient, false)

    private val json = Json { ignoreUnknownKeys = true }

    /** Supports GitHub search qualifiers; [pageSize] is 1–100 and search pages stop at 1,000 items. */
    suspend fun searchRepositories(
        query: String,
        page: Int = 1,
        pageSize: Int = 20,
    ): GitHubSearchResult {
        require(query.isNotBlank()) { "A search query is required." }
        validatePage(page, pageSize)
        require((page.toLong() - 1) * pageSize < 1_000) { "GitHub search is limited to 1,000 results." }
        return json.decodeFromString(request("search/repositories", page, pageSize, query.trim()))
    }

    /** Lists one page of published releases for an owner/repository pair. */
    suspend fun releases(
        owner: String,
        repository: String,
        page: Int = 1,
        pageSize: Int = 10,
    ): List<GitHubRelease> {
        require(owner.matches(Regex("[A-Za-z0-9-]+"))) { "Invalid GitHub owner." }
        require(repository.matches(Regex("[A-Za-z0-9_.-]+")) && repository != "." && repository != "..") {
            "Invalid GitHub repository name."
        }
        validatePage(page, pageSize)
        return json.decodeFromString(request("repos/$owner/$repository/releases", page, pageSize))
    }

    fun close() {
        if (ownsClient) httpClient.close()
    }

    private suspend fun request(path: String, page: Int, pageSize: Int, query: String? = null): String {
        val response = httpClient.get("https://api.github.com/$path") {
            // Return error responses to this client even if the injected HttpClient expects success.
            expectSuccess = false
            header("Accept", "application/vnd.github+json")
            header("X-GitHub-Api-Version", "2026-03-10")
            header("User-Agent", "Forge-GitHub/0.1")
            parameter("page", page)
            parameter("per_page", pageSize)
            if (query != null) parameter("q", query)
        }
        val body = response.bodyAsText()
        if (response.status.value !in 200..299) {
            val message = runCatching { json.decodeFromString<ErrorResponse>(body).message }
                .getOrNull() ?: "GitHub request failed (${response.status.value})."
            val retryAfter = response.headers["Retry-After"]?.toLongOrNull()
            val rateLimited = response.status.value == 429 ||
                (response.status.value == 403 &&
                    (response.headers["X-RateLimit-Remaining"] == "0" || retryAfter != null ||
                        message.contains("rate limit", ignoreCase = true)))
            throw GitHubApiException(
                statusCode = response.status.value,
                message = message,
                isRateLimited = rateLimited,
                retryAfterSeconds = retryAfter,
                rateLimitResetEpochSeconds = response.headers["X-RateLimit-Reset"]?.toLongOrNull(),
            )
        }
        return body
    }

    private fun validatePage(page: Int, pageSize: Int) {
        require(page > 0) { "Page must be positive." }
        require(pageSize in 1..100) { "Page size must be between 1 and 100." }
    }

    @Serializable
    private data class ErrorResponse(val message: String)
}

internal expect fun createGitHubHttpClient(): HttpClient

internal fun HttpClientConfig<*>.configureGitHub() {
    install(HttpTimeout) {
        requestTimeoutMillis = 20_000
        connectTimeoutMillis = 15_000
        socketTimeoutMillis = 20_000
    }
}
