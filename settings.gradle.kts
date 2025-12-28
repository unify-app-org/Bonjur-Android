pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Bonjur"
include(":app")
include(":appFeatures")
include(":appCore")
include(":appFeatures:auth")
include(":appCore:designSystem")
include(":appCore:network")
include(":appCore:storage")
include(":appCore:navigation")
include(":appCore:appUtils")
include(":appCore:appFoundation")
