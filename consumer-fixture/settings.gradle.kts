pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        val smokeRepository = providers.environmentVariable("FORGE_TEST_REPOSITORY").orNull
            ?: error("FORGE_TEST_REPOSITORY must point to the isolated Maven repository")
        maven { url = uri(smokeRepository) }
        google()
        mavenCentral()
    }
}

rootProject.name = "forge-consumer-fixture"
include(":consumer")
