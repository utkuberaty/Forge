plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dokka)
    alias(libs.plugins.vanniktech.maven.publish)
}

dokka {
    dokkaPublications.html {
        moduleName.set("Forge Kit")
        moduleVersion.set(project.version.toString())
    }
}

kotlin {
    android {
        namespace = "com.star.forge"
        compileSdk =
            libs.versions.androidCompileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.androidMinSdk
                .get()
                .toInt()
        withHostTest {}
    }

    iosArm64()
    iosSimulatorArm64()

    jvmToolchain(17)
    explicitApi()

    @OptIn(org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class)
    abiValidation()

    sourceSets {
        commonMain.dependencies {
            api(libs.jetbrains.compose.runtime)
            api(libs.jetbrains.compose.foundation)
            api(libs.jetbrains.compose.ui)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.jetbrains.compose.ui.test)
        }
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    if (providers.gradleProperty("signingInMemoryKey").isPresent) {
        signAllPublications()
    }
    pom {
        name.set("Forge Kit")
        description.set("Customizable Compose Multiplatform mobile UI primitives for Android and iOS.")
        inceptionYear.set("2026")
        url.set("https://github.com/utkuberaty/Forge")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("utkuberaty")
                name.set("Utku Yıldız")
                url.set("https://github.com/utkuberaty")
            }
        }
        scm {
            url.set("https://github.com/utkuberaty/Forge")
            connection.set("scm:git:git://github.com/utkuberaty/Forge.git")
            developerConnection.set("scm:git:ssh://git@github.com/utkuberaty/Forge.git")
        }
    }
}

publishing {
    repositories {
        maven {
            name = "smoke"
            url =
                uri(
                    providers
                        .gradleProperty("forgeSmokeRepository")
                        .orElse(layout.buildDirectory.dir("smoke-repository").map { it.asFile.toURI().toString() }),
                )
        }
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/utkuberaty/Forge")
            credentials {
                username =
                    providers
                        .gradleProperty("gpr.user")
                        .orElse(providers.environmentVariable("GITHUB_ACTOR"))
                        .orNull
                password =
                    providers
                        .gradleProperty("gpr.key")
                        .orElse(providers.environmentVariable("GITHUB_TOKEN"))
                        .orNull
            }
        }
    }
}

val verifyPrimitiveTokens =
    tasks.register("verifyPrimitiveTokens") {
        group = "verification"
        description = "Rejects raw design values in Forge primitive source."
        val primitiveDirectory = layout.projectDirectory.dir("src/commonMain/kotlin/com/star/forge/kit/primitives")
        inputs.dir(primitiveDirectory)
        doLast {
            val forbidden =
                listOf(
                    Regex("""\b\d+(?:\.\d+)?\.(?:dp|sp)\b"""),
                    Regex("""Color\s*\(\s*0x"""),
                    Regex("""alpha\s*=\s*\d+(?:\.\d+)?f?"""),
                    Regex("""tween\s*\(\s*\d+"""),
                )
            val violations =
                inputs.files.files
                    .asSequence()
                    .flatMap { it.walkTopDown() }
                    .filter { it.isFile && it.extension == "kt" }
                    .flatMap { file ->
                        file.readLines().asSequence().mapIndexedNotNull { index, line ->
                            if (forbidden.any { it.containsMatchIn(line) }) {
                                "${file.path}:${index + 1}: $line"
                            } else {
                                null
                            }
                        }
                    }.toList()
            check(violations.isEmpty()) {
                "Raw design values are not allowed in primitive source:\n${violations.joinToString("\n")}"
            }
        }
    }

tasks.named("check") {
    dependsOn(verifyPrimitiveTokens)
}

// Compose Multiplatform's common UI-test runner requires a real rendering host. The behavior and
// semantics suite runs on iOS Simulator in CI; Android host tests retain the pure common tests.
tasks.withType<org.gradle.api.tasks.testing.Test>().configureEach {
    exclude("**/ForgeSemanticsTest*")
}
