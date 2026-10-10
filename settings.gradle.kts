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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "AplicacionNativaOpenCVOpenCL"
include(":app")
include(":opencv")

// Instanciación explícita de java.io.File requerida por el tipado estricto de Kotlin
project(":opencv").projectDir = java.io.File("/home/vlarobbyk/aplicaciones/OpenCV-android-sdk/sdk")