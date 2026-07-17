import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    wasmJs {
        outputModuleName = "cmp-ui-docs"
        browser {
            commonWebpackConfig {
                outputFileName = "cmp-ui-docs.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain {
            kotlin.srcDir("../example/composeApp/src/commonMain/kotlin/zone/ien/utils/example/ui/screens/designsystem")
            kotlin.exclude("**/ColorTokenScreen.kt")

            dependencies {
                implementation(libs.compose.material3)
                implementation(libs.compose.preview)
                implementation(libs.compose.resources)
                implementation(libs.capsule)
                implementation(projects.cmpUi)
                implementation(projects.cmpIcon)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "zone.ien.utils.docs.generated.resources"
}
