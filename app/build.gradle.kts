plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    //Dagger 2
    id("kotlin-kapt")

    //Room
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.andef.mycarandef"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.andef.mycarandef"
        minSdk = 26
        //noinspection OldTargetApi
        targetSdk = 35
        versionCode = 11
        versionName = "11.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    //Coil
    implementation(libs.coil.compose)

    //Charts, Bars, Pies
    implementation(libs.charts)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.appcompat)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //Jetpack Compose Navigation
    implementation(libs.androidx.navigation.compose)

    //Dagger 2
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    //Splash Screen
    implementation(libs.androidx.core.splashscreen)

    //System Ui Controller
    implementation(libs.accompanist.systemuicontroller)

    //core:design
    implementation(project(":core:design"))

    //osmdroid
    implementation(libs.osmdroid.android)

    //Gson
    implementation(libs.gson)

    //Custom Calendar
    implementation(libs.compose)

    //Google Play Services Location
    implementation(libs.play.services.location)

    //core:data
    implementation(project(":core:data"))

    //core:di
    implementation(project(":core:di:common"))
    implementation(project(":core:di:viewmodel"))

    //core:navigation
    implementation(project(":core:navigation:graph"))
    implementation(project(":core:navigation:routes"))

    //feature:work
    implementation(project(":feature:work:data"))
    implementation(project(":feature:work:domain"))
    implementation(project(":feature:work:di"))
    implementation(project(":feature:work:presentation"))

    //feature:reminder
    implementation(project(":feature:reminder:data"))
    implementation(project(":feature:reminder:domain"))
    implementation(project(":feature:reminder:di"))
    implementation(project(":feature:reminder:presentation"))

    //feature:backup
    implementation(project(":feature:backup:di"))
    implementation(project(":feature:backup:presentation"))
    implementation(project(":feature:backup:domain"))

    //feature:expense
    implementation(project(":feature:expense:data"))
    implementation(project(":feature:expense:domain"))
    implementation(project(":feature:expense:di"))
    implementation(project(":feature:expense:presentation"))

    //feature:map
    implementation(project(":feature:map:di"))
    implementation(project(":feature:map:presentation"))

    //feature:car
    implementation(project(":feature:car:data"))
    implementation(project(":feature:car:domain"))
    implementation(project(":feature:car:di"))
    implementation(project(":feature:car:presentation"))

    //feature:start
    implementation(project(":feature:start:data"))
    implementation(project(":feature:start:domain"))
    implementation(project(":feature:start:di"))
    implementation(project(":feature:start:presentation"))

    //feature:ui-theme
    implementation(project(":feature:ui-theme:data"))
    implementation(project(":feature:ui-theme:domain"))
    implementation(project(":feature:ui-theme:di"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}