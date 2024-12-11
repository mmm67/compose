plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    alias(libs.plugins.ksp)
    kotlin("plugin.serialization")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.example.habittrackerapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.habittrackerapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    ksp {
        arg("dagger.hilt.android.internal.disableAndroidSuperclassValidation", "true")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.camera.view)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))

    // Room Dependencies
    implementation (libs.androidx.room.runtime)
    implementation (libs.androidx.room.ktx)
    annotationProcessor (libs.androidx.room.room.compiler)
    ksp(libs.androidx.room.room.compiler)
    implementation (libs.kotlinx.serialization.json)


    // When using the BoM, don't specify versions in Firebase dependencies
    implementation(libs.firebase.analytics)
    implementation (libs.google.firebase.analytics)
    implementation (libs.firebase.auth)
    implementation (libs.firebase.firestore.ktx)


    implementation(libs.androidx.material.icons.extended)

    // CameraX
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.video)


    // Coil
    implementation(libs.coil.compose)


    implementation (libs.kotlinx.serialization.json)

    // Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)

    // WorkManager
    implementation (libs.androidx.work.runtime.ktx)
    implementation (libs.androidx.material.icons.extended.v140)

}