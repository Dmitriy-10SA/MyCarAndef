plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.andef.mycarandef.graph"
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
    //core:ads
    implementation(project(":core:ads"))

    //feature:work:presentation
    implementation(project(":feature:work:presentation"))

    //feature:expense:presentation
    implementation(project(":feature:expense:presentation"))

    //feature:backup:presentation
    implementation(project(":feature:backup:presentation"))

    //feature:map:presentation
    implementation(project(":feature:map:presentation"))

    //feature:car:presentation
    implementation(project(":feature:car:presentation"))

    //feature:car:domain
    implementation(project(":feature:car:domain"))

    //feature:reminder:presentation
    implementation(project(":feature:reminder:presentation"))

    //feature:start:presentation
    implementation(project(":feature:start:presentation"))

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