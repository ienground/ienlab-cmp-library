import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidLibrary {
        namespace = "zone.ien.utils.example.lib"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }

        androidResources {
            enable = true
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
//        androidMain.dependencies {
//            implementation(libs.activity.compose)
//        }
        commonMain.dependencies {
            implementation(libs.compose.material3)
            implementation(libs.compose.preview)
            implementation(libs.compose.resources)

            implementation(libs.lifecycle.viewmodel)
            implementation(libs.lifecycle.runtime)

            implementation(libs.backdrop)
            implementation(libs.haze)

            implementation(libs.kotlinx.io.core)
            implementation(libs.kdatetime)
            implementation(libs.datastore.pref)

            implementation(libs.bundles.hig)
            implementation(libs.capsule)
            implementation(libs.placeholder)

            implementation(libs.bundles.koin)

            implementation(projects.cmpCommon)
            implementation(projects.cmpAdaptive)
            implementation(projects.cmpUi)
            implementation(projects.cmpDate)
            implementation(projects.cmpIcon)
            implementation(projects.cmpUtils)
            implementation(projects.cmpNavigation)
            implementation(projects.cmpPref)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}