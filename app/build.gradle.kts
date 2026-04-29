plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.alttekrarkelimeoyunu"
    // API uyumluluğu için 34 veya 35 olması en sağlıklısı
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.alttekrarkelimeoyunu"
        minSdk = 24
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

    // Compose kullanmıyorsan burayı kapatabilirsin ama kalsa da zarar vermez
    buildFeatures {
        compose = false
    }
}

dependencies {
    // Temel Android Kütüphaneleri
    implementation(libs.androidx.core.ktx)
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.androidx.activity)

    // --- API İÇİN GEREKLİ RETROFIT (ASIL GÜÇ BURADA) ---
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Test Birimleri
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}