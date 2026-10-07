package com.star.forge.sdk.github

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GitHubClientTest {
    @Test
    fun searchEncodesQualifiersAndPreservesNullableFields() =
        runTest {
            val http =
                HttpClient(
                    MockEngine { request ->
                        assertEquals("api.github.com", request.url.host)
                        assertEquals("/search/repositories", request.url.encodedPath)
                        assertEquals("kmp language:Kotlin + ui", request.url.parameters["q"])
                        assertEquals("2", request.url.parameters["page"])
                        assertEquals("20", request.url.parameters["per_page"])
                        assertEquals("application/vnd.github+json", request.headers["Accept"])
                        assertEquals("2026-03-10", request.headers["X-GitHub-Api-Version"])
                        assertNull(request.headers["Authorization"])
                        respond(
                            """{"total_count":21,"incomplete_results":true,"items":[{"id":1,"full_name":"owner/repo","description":null,"language":null,"stargazers_count":42,"html_url":"https://github.com/owner/repo","unknown":true}]}""",
                        )
                    },
                )
            try {
                val result = GitHubClient(http).searchRepositories(" kmp language:Kotlin + ui ", page = 2)
                assertEquals(21, result.totalCount)
                assertTrue(result.incompleteResults)
                assertEquals(42, result.items.single().stars)
                assertNull(result.items.single().description)
            } finally {
                http.close()
            }
        }

    @Test
    fun releasesReadMarkdownAndAnEmptyRepositoryIsNotAnError() =
        runTest {
            var calls = 0
            val http =
                HttpClient(
                    MockEngine { request ->
                        assertEquals("/repos/skydoves/GithubFollows/releases", request.url.encodedPath)
                        assertEquals("10", request.url.parameters["per_page"])
                        calls++
                        respond(
                            if (calls ==
                                1
                            ) {
                                """[{"id":2,"tag_name":"v1","name":null,"body":"## Changes","published_at":null,"html_url":"https://github.com/skydoves/GithubFollows/releases/tag/v1","prerelease":true}]"""
                            } else {
                                "[]"
                            },
                        )
                    },
                )
            try {
                val client = GitHubClient(http)
                val release = client.releases("skydoves", "GithubFollows").single()
                assertEquals("## Changes", release.body)
                assertTrue(release.prerelease)
                assertTrue(client.releases("skydoves", "GithubFollows").isEmpty())
            } finally {
                http.close()
            }
        }

    @Test
    fun rateLimitMetadataIsExposedWithoutRetrying() =
        runTest {
            var calls = 0
            val http =
                HttpClient(
                    MockEngine {
                        calls++
                        respond(
                            """{"message":"API rate limit exceeded"}""",
                            HttpStatusCode.Forbidden,
                            headersOf(
                                "X-RateLimit-Remaining" to listOf("0"),
                                "Retry-After" to listOf("60"),
                                "X-RateLimit-Reset" to listOf("1800000000"),
                            ),
                        )
                    },
                ) { expectSuccess = true }
            try {
                val failure = assertFailsWith<GitHubApiException> { GitHubClient(http).searchRepositories("kotlin") }
                assertTrue(failure.isRateLimited)
                assertEquals(60L, failure.retryAfterSeconds)
                assertEquals(1800000000L, failure.rateLimitResetEpochSeconds)
                assertEquals(1, calls)
            } finally {
                http.close()
            }
        }

    @Test
    fun ordinaryForbiddenResponseIsNotMisreportedAsRateLimit() =
        runTest {
            val http = HttpClient(MockEngine { respond("""{"message":"Forbidden"}""", HttpStatusCode.Forbidden) })
            try {
                val failure = assertFailsWith<GitHubApiException> { GitHubClient(http).searchRepositories("kotlin") }
                assertFalse(failure.isRateLimited)
                assertEquals(403, failure.statusCode)
            } finally {
                http.close()
            }
        }

    @Test
    fun invalidInputsDoNotSendNetworkRequests() =
        runTest {
            val http = HttpClient(MockEngine { error("No request should be sent") })
            try {
                val client = GitHubClient(http)
                assertFailsWith<IllegalArgumentException> { client.searchRepositories(" ") }
                assertFailsWith<IllegalArgumentException> { client.searchRepositories("kotlin", page = 0) }
                assertFailsWith<IllegalArgumentException> { client.searchRepositories("kotlin", page = 51) }
                assertFailsWith<IllegalArgumentException> { client.releases("owner", "../repo") }
                assertFailsWith<IllegalArgumentException> { client.releases("owner", "repo", pageSize = 101) }
            } finally {
                http.close()
            }
        }

    @Test
    fun cancellationIsPropagatedAndInjectedClientRemainsUsable() =
        runTest {
            val http = HttpClient(MockEngine { throw CancellationException("Cancelled request") })
            try {
                val client = GitHubClient(http)
                client.close()
                assertFailsWith<CancellationException> { client.searchRepositories("kotlin") }
            } finally {
                http.close()
            }
        }
}
