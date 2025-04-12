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
        maven {
            url = uri("https://cardinalcommerceprod.jfrog.io/artifactory/android")
            credentials {
                username = "braintree_team_sdk"
                password = "cmVmdGtuOjAxOjIwMzgzMzI5Nzg6Q3U0eUx5Zzl5TDFnZXpQMXpESndSN2tBWHhJ"
            }
        }
    }
}

rootProject.name = "STrip"
include(":app")
