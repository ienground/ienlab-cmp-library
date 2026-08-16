import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.buildkonfig)
}

val localProps = Properties().apply { rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) } }


kotlin {
    android {
        namespace = "zone.ien.utils.example.lib"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_25)
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
           implementation(projects.cmpFilekit)
           implementation(projects.cmpFirebase)
       }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}

buildkonfig {
    packageName = "zone.ien.utils.example"
    defaultConfigs {
        val clientId = localProps.getProperty("GCP_WEB_CLIENT_ID")
            ?: project.findProperty("GCP_WEB_CLIENT_ID") as? String
            ?: System.getenv("GCP_WEB_CLIENT_ID")
            ?: ""
        buildConfigField(STRING, "GCP_WEB_CLIENT_ID", clientId)
    }
}
