pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ienlab-cmp-library"
include(":library")
include(":example")
include(":example:composeApp")
include(":example:iosApp")
include(":cmp-ui")
include(":cmp-date")
include(":cmp-icon")
include(":cmp-adaptive")
include(":cmp-firebase")
include(":cmp-common")
include(":cmp-utils")
include(":cmp-navigation")
