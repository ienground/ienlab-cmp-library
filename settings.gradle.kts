pluginManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "ienlab-cmp-library"
include(":example")
include(":example:composeApp")
include(":example:androidApp")
include(":example:iosApp")
include(":cmp-ui")
include(":cmp-date")
include(":cmp-icon")
include(":cmp-adaptive")
include(":cmp-firebase")
include(":cmp-common")
include(":cmp-utils")
include(":cmp-navigation")
