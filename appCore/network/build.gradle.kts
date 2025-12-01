plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    // serialization
    id("org.jetbrains.kotlin.plugin.serialization")
    // DI
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.bonjur.network"
    compileSdk = 36

    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false

            buildConfigField("String", "API_BASE_URL", "\"https://test-api.bonjur.app\"")
            buildConfigField("String", "ENVIRONMENT", "\"TEST\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
        }

        create("staging") {
            isMinifyEnabled = false

            buildConfigField("String", "API_BASE_URL", "\"https://api.bonjur.app\"")
            buildConfigField("String", "ENVIRONMENT", "\"STAGING\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
        }

        release {
            isMinifyEnabled = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "API_BASE_URL", "\"https://api.bonjur.app\"")
            buildConfigField("String", "ENVIRONMENT", "\"RELEASE\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "false")
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
    // App modules
    implementation(project(":appCore:storage"))

    // Ktor
    implementation("io.ktor:ktor-client-android:2.3.7")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("io.ktor:ktor-client-logging:2.3.7")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}