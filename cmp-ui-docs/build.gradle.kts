import org.gradle.api.tasks.Sync
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

val sharedDesignSystemSource = rootProject.file(
    "example/composeApp/src/commonMain/kotlin/zone/ien/utils/example/ui/screens/designsystem/DesignSystemScreen.kt",
)
val generatedDesignSystemSource = layout.buildDirectory.dir(
    "generated/sharedDesignSystem/kotlin/zone/ien/utils/example/ui/screens/designsystem",
)
val syncDesignSystemSource = tasks.register<Sync>("syncDesignSystemSource") {
    from(sharedDesignSystemSource)
    into(generatedDesignSystemSource)
}

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
            kotlin.srcDir(syncDesignSystemSource)

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
