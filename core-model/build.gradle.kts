@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.parcelize)
  alias(libs.plugins.ksp)
}

android {
  compileSdk = rootProject.extra["compileSdk"] as Int

  defaultConfig {
    minSdk = rootProject.extra["minSdk"] as Int
    targetSdk = rootProject.extra["targetSdk"] as Int
  }
}

dependencies {

  // json parsing
  implementation(libs.moshi)
  ksp(libs.moshi.codegen)

  // logger
  //api(libs.timber)
  api(libs.timber)
}