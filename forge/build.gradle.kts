plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.star.forge"
        compileSdk = libs.versions.androidCompileSdk.get().toInt()
        minSdk = libs.versions.androidMinSdk.get().toInt()
    }

    iosArm64()
    iosSimulatorArm64()

    jvmToolchain(17)

    sourceSets {
        commonMain.dependencies {
            api(libs.jetbrains.compose.runtime)
            api(libs.jetbrains.compose.foundation)
            api(libs.jetbrains.compose.ui)
        }
    }
}
