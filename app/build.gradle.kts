plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "algonquin.cst2335.finalprojectandroid"
    compileSdk = 34

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

    viewBinding {
        enable = true
    }

}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation("com.android.volley:volley:1.2.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("androidx.room:room-runtime:2.4.3")
    annotationProcessor ("androidx.room:room-compiler:2.4.3")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")

    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")


//  androidTestImplementation(libs.espresso.contrib)
//  implementation("androidx.test.espresso:espresso-core:3.6.0-alpha03")
//  implementation("androidx.test:runner:1.5.2")
//  implementation("androidx.test:rules:1.6.0-alpha03")
//  implementation("androidx.test.ext:junit:1.2.0-alpha03")

}