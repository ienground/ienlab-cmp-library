plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.vanniktech.mavenPublish)
}

kotlin {
    android {
        namespace = "zone.ien.utils.ui"
        compileSdk {
            version = release(libs.versions.android.compileSdk.get().toInt()) {
                minorApiLevel = libs.versions.android.compileSdkMinor.get().toInt()
            }
        }
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
    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.compilations.getByName("main").cinterops.create("ienBarPlacement") {
            definitionFile.set(project.file("src/nativeInterop/cinterop/ienBarPlacement.def"))
        }
        target.compilations.getByName("main").cinterops.create("ienOverlayAppearance") {
            definitionFile.set(project.file("src/nativeInterop/cinterop/ienOverlayAppearance.def"))
        }
        target.compilations.getByName("test").cinterops.create("ienOverlayAppearanceTest") {
            definitionFile.set(project.file("src/nativeInterop/cinterop/ienOverlayAppearanceTest.def"))
        }
    }
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kdatetime)
            api(libs.kotlinx.datetime)
            implementation(libs.compose.material3)
            implementation(libs.compose.preview)
            implementation(libs.compose.resources)
            api(libs.hig.core)
            implementation(libs.hig)
            implementation(libs.backdrop)
            implementation(libs.capsule)
            implementation(libs.compose.inputactions)

            implementation(projects.cmpCommon)
            implementation(projects.cmpIcon)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.activity.compose)
            implementation(projects.cmpUtils)
        }

        getByName("androidDeviceTest").dependencies {
            implementation(libs.runner)
            implementation(libs.core)
            implementation(libs.junit)
        }

        iosMain.dependencies {
            implementation(projects.cmpUtils)
        }

        iosTest.dependencies {
            implementation(libs.compose.ui.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}

// UIKit 테스트는 시스템 서비스에 연결할 수 있는 부팅된 시뮬레이터에서 실행합니다.
tasks.withType<org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest>().configureEach {
    standalone.set(false)
}

compose.resources {
    publicResClass = true
    generateResClass = auto
}
