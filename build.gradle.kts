plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply  false
    alias(libs.plugins.vanniktech.mavenPublish) apply false
    alias(libs.plugins.android.lint) apply false
    alias(libs.plugins.compose.compiler) apply false
}

subprojects {
    plugins.withId("com.vanniktech.maven.publish") {
        configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
            publishToMavenCentral()

            // Artifact ID만 각 프로젝트의 이름으로 자동 설정
            group = "zone.ien.utils"
            version = libs.versions.lib.version.name.get()
            println("${group} ${project.name} ${version}")

            coordinates(group.toString(), project.name, version.toString())

            pom {
                name = project.name
                description = "IENGROUND Kotlin & Compose Multiplatform Helper."
                inceptionYear = "2026"
                url = "https://github.com/ienground/ienlab-cmp-library"
                licenses {
                    license {
                        name = "Apache-2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                    }
                }
                developers {
                    developer {
                        id = "ienground"
                        name = "Ericano Rhee"
                        url = "my@ien.zone"
                    }
                }
                scm {
                    url = "https://github.com/ienground/ienlab-cmp-library.git"
                    connection = "scm:git:https://github.com/ienground/ienlab-cmp-library.git"
                    developerConnection = "scm:git:https://github.com/ienground/ienlab-cmp-library.git"
                }
            }
        }
    }
}