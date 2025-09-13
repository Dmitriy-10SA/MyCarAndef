plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    //Room
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.andef.mycarandef.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    //feature:work
    implementation(project(":feature:work:data"))
    implementation(project(":feature:work:domain"))

    //feature:expense
    implementation(project(":feature:expense:data"))
    implementation(project(":feature:expense:domain"))

    //feature:reminder
    implementation(project(":feature:reminder:data"))
    implementation(project(":feature:reminder:domain"))

    //feature:car
    implementation(project(":feature:car:data"))
    implementation(project(":feature:car:domain"))

    //feature:backup:domain
    implementation(project(":feature:backup:domain"))

    //feature:start
    implementation(project(":feature:start:data"))
    implementation(project(":feature:start:domain"))

    //feature:ui-theme
    implementation(project(":feature:ui-theme:data"))
    implementation(project(":feature:ui-theme:domain"))

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.appcompat)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}