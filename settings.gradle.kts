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
    }
}

rootProject.name = "MyCarAndef"
include(":app")

include(":core:design")
include(":core:data")
include(":core:di")
include(":core:navigation")
include(":core:navigation:graph")
include(":core:navigation:routes")

include(":feature:work")
include(":feature:work:data")
include(":feature:work:presentation")
include(":feature:work:di")
include(":feature:work:domain")

include(":feature:expense")
include(":feature:expense:domain")
include(":feature:expense:data")
include(":feature:expense:presentation")
include(":feature:expense:di")

include(":feature:map")
include(":feature:map:data")
include(":feature:map:di")
include(":feature:map:presentation")
include(":feature:map:domain")

include(":feature:car")
include(":feature:car:data")
include(":feature:car:presentation")
include(":feature:car:domain")
include(":feature:car:di")
