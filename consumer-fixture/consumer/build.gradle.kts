plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    android {
        namespace = "com.star.forge.consumerfixture"
        compileSdk = 37
        minSdk = 23
    }

    iosArm64()
    iosSimulatorArm64 {
        binaries.framework {
            baseName = "ForgeConsumerFixture"
            isStatic = true
        }
    }

    jvmToolchain(17)

    sourceSets.commonMain.dependencies {
        val forgeVersion = providers.environmentVariable("FORGE_TEST_VERSION").orElse("0.1.0-SNAPSHOT").get()
        implementation("io.github.utkuberaty:forge-kit:$forgeVersion")
        implementation("org.jetbrains.compose.runtime:runtime:1.11.1")
    }
}
