plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.home.reactiveform"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.home.reactiveform"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //rx-java
    implementation("io.reactivex.rxjava2:rxjava:2.2.19")
    //RxBinding adalah library yang digunakan untuk membuat Observable dari komponen User Interface seperti EditText, SearchView dan Radio Group.
    implementation("com.jakewharton.rxbinding2:rxbinding:2.0.0")
    // opsional : library tambahan dengan tambahan extension function pada Kotlin
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    // library untuk memudahkan threading di Android
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
}