//plugins {
////    alias(libs.plugins.android.application)
////    alias(libs.plugins.kotlin.android)
////    alias(libs.plugins.kotlin.compose)
//
//    id("com.android.application")
//    id("org.jetbrains.kotlin.android")
////    id("org.jetbrains.kotlin.plugin.compose")
//
//}
//
//android {
//    namespace = "com.example.face_detect_app"
//    compileSdk = 35
//
//    defaultConfig {
//        applicationId = "com.example.face_detect_app"
//        minSdk = 28
//        targetSdk = 35
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//
//    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
//    buildFeatures {
//        compose = true
//    }
//
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.14" // For Kotlin 1.9.24
//    }
//}
//
////dependencies {
////
////    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
////    implementation(composeBom)
////    implementation("androidx.compose.ui:ui")
////    implementation("androidx.compose.material3:material3")
////    implementation("androidx.compose.ui:ui-tooling-preview")
////    debugImplementation("androidx.compose.ui:ui-tooling")
////
////    implementation(libs.androidx.core.ktx)
////    implementation(libs.androidx.appcompat)
////    implementation(libs.material)
////    implementation(libs.androidx.lifecycle.runtime.ktx)
////    implementation(libs.androidx.activity.compose)
////    implementation(platform(libs.androidx.compose.bom))
////    implementation(libs.androidx.ui)
////    implementation(libs.androidx.ui.graphics)
////    implementation(libs.androidx.ui.tooling.preview)
////    implementation(libs.androidx.material3)
////    testImplementation(libs.junit)
////    androidTestImplementation(libs.androidx.junit)
////    androidTestImplementation(libs.androidx.espresso.core)
////    androidTestImplementation(platform(libs.androidx.compose.bom))
////    androidTestImplementation(libs.androidx.ui.test.junit4)
////    debugImplementation(libs.androidx.ui.tooling)
////    debugImplementation(libs.androidx.ui.test.manifest)
////}
//
//dependencies {
//    // Compose BOM
//    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
//    implementation(composeBom)
//    androidTestImplementation(composeBom)
//
//    // Compose dependencies
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3")
//    implementation("androidx.activity:activity-compose:1.9.0")
//
//    // **ADD THIS LINE** - Material Components for XML themes
//    implementation("com.google.android.material:material:1.12.0")
//
//    // Kotlin
//    implementation("androidx.core:core-ktx:1.13.1")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
//    implementation("androidx.appcompat:appcompat:1.7.0")
//
//    // Testing
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//
//    // Debug
//    debugImplementation("androidx.compose.ui:ui-tooling")
//    debugImplementation("androidx.compose.ui:ui-test-manifest")
//}




plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.face_detect_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.face_detect_app"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}
//
//dependencies {
//    // Compose BOM
//    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
//    implementation(composeBom)
//    androidTestImplementation(composeBom)
//
//    // Compose dependencies
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3")
//    implementation("androidx.compose.material:material-icons-extended")
//    implementation("androidx.activity:activity-compose:1.9.0")
//
//    // CameraX - Latest version 1.5.1
//    implementation("androidx.camera:camera-core:1.3.4")
//    implementation("androidx.camera:camera-camera2:1.3.4")
//    implementation("androidx.camera:camera-lifecycle:1.3.4")
//    implementation("androidx.camera:camera-view:1.3.4")
//
//    // ML Kit Face Detection
//    implementation("com.google.mlkit:face-detection:16.1.6")
//
//    // Core and Lifecycle
//    implementation("androidx.core:core-ktx:1.13.1")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
//
//    // Material Components for XML themes
//    implementation("com.google.android.material:material:1.12.0")
//    implementation("androidx.appcompat:appcompat:1.7.0")
//
//    // Accompanist Permissions - Latest version 0.37.0
//    implementation("com.google.accompanist:accompanist-permissions:0.37.0")
//
//    // Testing
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//
//    // Debug
//    debugImplementation("androidx.compose.ui:ui-tooling")
//    debugImplementation("androidx.compose.ui:ui-test-manifest")
//}




dependencies {
    // Existing dependencies...
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Compose dependencies
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.9.0")

    // CameraX
    implementation("androidx.camera:camera-core:1.3.4")
    implementation("androidx.camera:camera-camera2:1.3.4")
    implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-view:1.3.4")

    // ML Kit Face Detection
    implementation("com.google.mlkit:face-detection:16.1.6")

    // Core and Lifecycle
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")

    // Material Components
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Accompanist Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.37.0")

    // Networking - Retrofit + OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Debug
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
