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
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "MyCarAndef"
include(":app")

include(":core:design")
include(":core:data")
include(":core:di:common")
include(":core:di:viewmodel")
include(":core:navigation")
include(":core:navigation:graph")
include(":core:navigation:routes")
include(":core:utils")

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
include(":feature:map:di")
include(":feature:map:presentation")

include(":feature:car")
include(":feature:car:data")
include(":feature:car:presentation")
include(":feature:car:domain")
include(":feature:car:di")

include(":feature:start")
include(":feature:start:data")
include(":feature:start:domain")
include(":feature:start:presentation")
include(":feature:start:di")

include(":feature:ui-theme")
include(":feature:ui-theme:data")
include(":feature:ui-theme:domain")
include(":feature:ui-theme:di")

include(":feature:reminder")
include(":feature:reminder:data")
include(":feature:reminder:di")
include(":feature:reminder:domain")
include(":feature:reminder:presentation")

include(":feature:backup")
include(":feature:backup:di")
include(":feature:backup:presentation")
include(":feature:backup:domain")
