plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    //Dagger 2
    id("kotlin-kapt")

    //Room
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.andef.mycarandef.di"
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
    //feature:start
    implementation(project(":feature:start:di"))
    implementation(project(":feature:start:domain"))
    implementation(project(":feature:start:data"))

    //feature:car
    implementation(project(":feature:car:di"))
    implementation(project(":feature:car:domain"))
    implementation(project(":feature:car:data"))

    //feature:work
    implementation(project(":feature:work:di"))
    implementation(project(":feature:work:domain"))
    implementation(project(":feature:work:data"))

    //feature:expense
    implementation(project(":feature:expense:di"))
    implementation(project(":feature:expense:domain"))
    implementation(project(":feature:expense:data"))

    //feature:map
    implementation(project(":feature:map:di"))
    implementation(project(":feature:map:domain"))
    implementation(project(":feature:map:data"))

    //Dagger 2
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

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