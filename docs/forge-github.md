# Forge GitHub SDK

`:forge-github` provides public APIs under `com.star.forge.sdk.github`. It is independent of the UI kit and supports Android, iOS, and JVM.

## Consume from another repository

Forge's source is public. These modules are not currently published to Maven Central; RepoScout uses a Gradle composite build with explicit substitutions:

```kotlin
includeBuild("../Forge") {
    dependencySubstitution {
        substitute(module("com.star.forge:forge")).using(project(":forge"))
        substitute(module("com.star.forge:forge-github")).using(project(":forge-github"))
    }
}
```

Then add `implementation("com.star.forge:forge-github:0.1.0")` to the app's `commonMain` dependencies. The composite build resolves this coordinate to local public source, not a remote artifact.

## Read public repositories and releases

```kotlin
val client = GitHubClient()
try {
    val search = client.searchRepositories("language:Kotlin compose", page = 1, pageSize = 20)
    val releases = client.releases("skydoves", "Balloon", page = 1, pageSize = 10)
} finally {
    client.close()
}
```

Call suspend functions from a coroutine. Android hosts must declare `android.permission.INTERNET`. Default transports are OkHttp on Android/JVM and Darwin on iOS. Requests use HTTPS, `application/vnd.github+json`, a User-Agent, API version `2026-03-10`, and a 20-second timeout.

Inject `GitHubClient(httpClient)` to configure transport or supply Ktor's `MockEngine`. The caller owns an injected client; `GitHubClient.close()` only closes the default client it creates. Coroutine cancellation propagates unchanged.

## Response and error contracts

- `GitHubSearchResult` preserves `totalCount`, `incompleteResults`, and typed items. Search supports GitHub qualifiers, safely encodes query parameters, and rejects pages beyond the first 1,000 results. Page size is 1–100.
- `GitHubRepository` preserves nullable description/language and includes the full name, stars, ID, and browser URL.
- `GitHubRelease` preserves raw Markdown notes, nullable title/date, tag, ID, browser URL, and pre-release status. A repository without releases returns an empty list.
- `GitHubApiException` exposes HTTP status, GitHub's message, rate-limit classification, retry seconds, and reset epoch seconds where supplied. Transport and decoding failures retain their original types.

There is no automatic retry. Public unauthenticated requests have GitHub rate limits, including a separate search limit. Apps should submit searches explicitly, retain useful results, and let users retry after limits reset. See [rate limits](https://docs.github.com/en/rest/using-the-rest-api/rate-limits-for-the-rest-api) and [API best practices](https://docs.github.com/en/rest/using-the-rest-api/best-practices-for-using-the-rest-api).

## Verify

```bash
./gradlew :forge-github:jvmTest
./gradlew :forge-github:assembleAndroidMain :forge-github:compileKotlinIosSimulatorArm64
```

MockEngine tests cover encoded qualifiers and pagination, nullable/unknown fields, release parsing, empty releases, HTTP errors, rate limits, input validation, cancellation, and injected-client ownership. [RepoScout](https://github.com/utkuberaty/RepoScout) demonstrates real calls and shared UI on Android and iOS.
