@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)

    //id("kotlin-kapt")
    /*id("java-library")
    id("org.jetbrains.kotlin.jvm")*/
}

android {
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdk"] as Int
        targetSdk = rootProject.extra["targetSdk"] as Int
    }
}

dependencies {

    api(project(":core-model"))
    implementation(project(":core-network"))
    implementation(project(":core-database"))
    testImplementation(project(":core-test"))

    // coroutines
    implementation(libs.coroutines)
    testImplementation(libs.coroutines)
    testImplementation(libs.coroutines.test)

    // network
    implementation(libs.sandwich)

    // di
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // unit test
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
}