
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    /*
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    */

    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
    //id("kotlin-kapt")

    //alias(libs.plugins.kotlin.parcelize)
}

android {
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig {
        applicationId = "com.example.pokedex_meltdown"
        //minSdk = 21
        minSdk = rootProject.extra["minSdk"] as Int
        targetSdk = rootProject.extra["targetSdk"] as Int
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"] as String

        //testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "com.example.pokedex_meltdown.AppTestRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
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

    implementation(project(":core-data"))
    //implementation(project(mapOf("path" to ":core-model")))

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.transformationLayout)
    implementation(libs.bindables) // data binding skydove
    implementation(libs.androidx.fragment)

    implementation(libs.whatif) // skydove
    implementation(libs.recyclerview)
    implementation(libs.baseAdapter) // skydove
    implementation(libs.rainbow) // skydove
    implementation(libs.androidRibbon) // skydove
    implementation(libs.progressView) // skydove
    implementation(libs.glide) // bumptech
    implementation(libs.glide.palette) // florent37
    implementation(libs.bundler) // bundler

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Hilt test
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    //testImplementation("com.google.dagger:hilt-android-testing:2.44")
    //kaptTest("com.google.dagger:hilt-android-compiler:2.44")


    // unit test
    testImplementation(libs.junit) //기본
    testImplementation(libs.turbine)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.androidx.junit) //기본
    androidTestImplementation(libs.androidx.espresso) //기본
    androidTestImplementation(libs.android.test.runner)

    // ===============================
    // 안넣고 있던거 일단 추가.

    // modules for unit test
    testImplementation(project(":core-network"))
    testImplementation(project(":core-database"))
    testImplementation(project(":core-test"))
    androidTestImplementation(project(":core-test"))

    // 내 프로젝트에는 필요없어보이는데..?
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.startup)

}

kapt {
    correctErrorTypes = true
}