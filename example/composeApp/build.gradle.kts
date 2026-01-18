import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
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
        androidMain.dependencies {
            implementation(libs.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.compose.material3)
            implementation(libs.compose.preview)
            implementation(libs.compose.resources)

            implementation(libs.lifecycle.viewmodel)
            implementation(libs.lifecycle.runtime)

            implementation(libs.backdrop)
            implementation(libs.haze)

            implementation(libs.kotlinx.io.core)

//            implementation(project(":cmp-common"))
            implementation(project(":cmp-adaptive"))
            implementation(project(":cmp-ui"))
            implementation(project(":cmp-date"))
//            implementation(project(":cmp-firebase"))
            implementation(project(":cmp-icon"))
            implementation(project(":cmp-utils"))
            implementation(project(":cmp-navigation"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "zone.ien.utils.example"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "zone.ien.utils.example"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = libs.versions.lib.version.name.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.compose.ui.tooling)
}

