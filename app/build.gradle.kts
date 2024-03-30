plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "algonquin.cst2335.finalprojectandroid"
    compileSdk = 34
    buildFeatures {
        viewBinding = true
    }
    defaultConfig {
        applicationId = "algonquin.cst2335.finalprojectandroid"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
  //  implementation(fileTree(mapOf("dir" to "C:\\Users\\eilee\\AppData\\Local\\Android\\Sdk\\platforms\\android-34", "include" to listOf("*.aar", "*.jar"), "exclude" to listOf())))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")
    implementation ("com.android.volley:volley:1.2.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")
    androidTestImplementation(libs.espresso.contrib)
    implementation("androidx.test.espresso:espresso-core:3.6.0-alpha03")
    implementation("androidx.test:runner:1.5.2")
    implementation("androidx.test:rules:1.6.0-alpha03")
    implementation("androidx.test.ext:junit:1.2.0-alpha03")
}