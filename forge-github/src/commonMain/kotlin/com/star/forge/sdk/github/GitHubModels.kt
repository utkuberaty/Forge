package com.star.forge.sdk.github

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A public GitHub repository, with nullable metadata preserved from the API. */
@Serializable
data class GitHubRepository(
    val id: Long,
    @SerialName("full_name") val fullName: String,
    val description: String? = null,
    val language: String? = null,
    @SerialName("stargazers_count") val stars: Int = 0,
    @SerialName("html_url") val htmlUrl: String,
)

/** One page of repository search results. GitHub caps search at 1,000 results. */
@Serializable
data class GitHubSearchResult(
    @SerialName("total_count") val totalCount: Int,
    @SerialName("incomplete_results") val incompleteResults: Boolean = false,
    val items: List<GitHubRepository>,
)

/** Published release metadata. [body] contains raw Markdown, not rendered HTML. */
@Serializable
data class GitHubRelease(
    val id: Long,
    @SerialName("tag_name") val tagName: String,
    val name: String? = null,
    val body: String? = null,
    @SerialName("published_at") val publishedAt: String? = null,
    @SerialName("html_url") val htmlUrl: String,
    val prerelease: Boolean = false,
)

/** An HTTP failure. Transport failures and coroutine cancellation retain their original types. */
class GitHubApiException(
    val statusCode: Int,
    override val message: String,
    val isRateLimited: Boolean,
    val retryAfterSeconds: Long? = null,
    val rateLimitResetEpochSeconds: Long? = null,
) : Exception(message)
