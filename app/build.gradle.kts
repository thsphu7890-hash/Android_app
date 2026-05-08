plugins {
    // Sửa dấu gạch ngang thành dấu chấm như dưới đây
    alias(libs.plugins.android.application)
}
android {
    namespace = "com.example.hitcapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hitcapp"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Các thư viện mặc định của hệ thống
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Glide (Sửa lại cho đồng nhất 1 phiên bản)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // Nếu dùng Kotlin, ông nên dùng 'ksp' hoặc 'annotationProcessor' cho Glide
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // Retrofit (Chuyển sang cú pháp Kotlin DSL chuẩn)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}



