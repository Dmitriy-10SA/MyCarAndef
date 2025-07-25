plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
dependencies {
    //feature:car:domain
    implementation(project(":feature:car:domain"))

    //feature:expense:domain
    implementation(project(":feature:expense:domain"))

    //feature:reminder:domain
    implementation(project(":feature:reminder:domain"))

    //feature:start:domain
    implementation(project(":feature:start:domain"))

    //feature:work:domain
    implementation(project(":feature:work:domain"))

    //feature:ui-theme:domain
    implementation(project(":feature:ui-theme:domain"))
}