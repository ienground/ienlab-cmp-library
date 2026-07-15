plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.vanniktech.mavenPublish)
}

kotlin {
    android {
        namespace = "zone.ien.utils.pref"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources {
            enable = true
        }

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.material3)
            implementation(libs.compose.resources)
//            implementation(libs.hig.core)
            implementation(libs.hig)
            implementation(libs.hig.adaptive)
            implementation(libs.backdrop)
            implementation(libs.datastore.pref)

            implementation(projects.cmpCommon)
            implementation(projects.cmpIcon)
            implementation(projects.cmpUtils)
            implementation(projects.cmpAdaptive)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
        }

        getByName("androidDeviceTest").dependencies {
            implementation(libs.runner)
            implementation(libs.core)
            implementation(libs.junit)
        }

        iosMain.dependencies {
        }
    }
}