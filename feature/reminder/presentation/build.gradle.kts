plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    //Dagger 2
    id("kotlin-kapt")
}

android {
    namespace = "com.andef.mycar.reminder.presentation"
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    //core:utils
    implementation(project(":core:utils"))

    //ads
    implementation(libs.mobileads)

    //Coil
    implementation(libs.coil.compose)

    //Custom Calendar
    implementation(libs.compose)

    //feature:reminder:domain
    implementation(project(":feature:reminder:domain"))

    //feature:car:domain
    implementation(project(":feature:car:domain"))

    //Dagger 2
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    //core:design
    implementation(project(":core:design"))

    //core:di:viewmodel
    implementation(project(":core:di:viewmodel"))

    //core:navigation:routes
    implementation(project(":core:navigation:routes"))

    //Jetpack Compose Navigation
    implementation(libs.androidx.navigation.compose)

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