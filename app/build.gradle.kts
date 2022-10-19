
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    /*id("com.android.application")
    id("org.jetbrains.kotlin.android")*/

    /*
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    */

    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)

    id("kotlin-kapt")

}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.example.pokedex_meltdown"
        //minSdk = 21
        minSdk = rootProject.extra["minSdk"] as Int
        targetSdk = rootProject.extra["targetSdk"] as Int
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        /*getByName("release") {
            isMinifyEnabled = true // Enables code shrinking for the release build type.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }*/
    }
    buildFeatures {
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    hilt {
        enableAggregatingTask = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
        //jvmTarget = libs.versions.jvmTarget.toString //이거 빌드에러남.
        //jvmTarget = "${libs.versions.jvmTarget}" // 이거도 안됨.
    }
}

dependencies {
    /*
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.6.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    */

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.transformationLayout)
    implementation(libs.bindables) // data binding skydove
    implementation(libs.androidx.fragment)
    implementation(libs.timber)
    implementation(libs.whatif) // skydove
    implementation(libs.baseAdapter) // skydove
    implementation(libs.rainbow) // skydove
    implementation(libs.androidRibbon) // skydove
    implementation(libs.progressView) // skydove
    implementation(libs.glide) // bumptech
    implementation(libs.glide.palette) // florent37

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Hilt test
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    //testImplementation("com.google.dagger:hilt-android-testing:2.44")
    //kaptTest("com.google.dagger:hilt-android-compiler:2.44")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)
}

kapt {
    correctErrorTypes = true
}