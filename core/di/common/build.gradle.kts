plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    //Dagger 2
    id("kotlin-kapt")

    //Room
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.andef.mycarandef.common"
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
    //Dagger 2
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.appcompat)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //core:di:viewmodel
    implementation(project(":core:di:viewmodel"))

    //feature:car
    implementation(project(":feature:car:data"))
    implementation(project(":feature:car:domain"))
    implementation(project(":feature:car:di"))
    implementation(project(":feature:car:presentation"))

    //feature:backup
    implementation(project(":feature:backup:di"))
    implementation(project(":feature:backup:presentation"))
    implementation(project(":feature:backup:domain"))

    //feature:reminder
    implementation(project(":feature:reminder:data"))
    implementation(project(":feature:reminder:domain"))
    implementation(project(":feature:reminder:di"))
    implementation(project(":feature:reminder:presentation"))

    //feature:expense
    implementation(project(":feature:expense:data"))
    implementation(project(":feature:expense:domain"))
    implementation(project(":feature:expense:di"))
    implementation(project(":feature:expense:presentation"))

    //feature:map
    implementation(project(":feature:map:di"))
    implementation(project(":feature:map:presentation"))

    //feature:start
    implementation(project(":feature:start:data"))
    implementation(project(":feature:start:domain"))
    implementation(project(":feature:start:di"))
    implementation(project(":feature:start:presentation"))

    //feature:work
    implementation(project(":feature:work:data"))
    implementation(project(":feature:work:domain"))
    implementation(project(":feature:work:di"))
    implementation(project(":feature:work:presentation"))

    //feature:ui-theme
    implementation(project(":feature:ui-theme:data"))
    implementation(project(":feature:ui-theme:domain"))
    implementation(project(":feature:ui-theme:di"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}