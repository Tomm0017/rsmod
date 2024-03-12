rootProject.name = "OpenRune-Server"

plugins {
    id("de.fayard.refreshVersions") version("0.51.0")
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":util")
include(":net")
include(":game")
include(":game:plugins")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            files("../gradle/libs.versions.toml")
        }
    }
}