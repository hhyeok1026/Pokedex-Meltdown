// Top-level build file where you can add configuration options common to all sub-projects/modules.

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    /*id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false*/

}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

buildscript  {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.agp)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.hilt.plugin)
    }

    val majorVersion = 0
    val minorVersion = 0
    val patchVersion = 1
    val versionName = "$majorVersion.$minorVersion.$patchVersion"
    val versionCode = 1

    extra.apply {
        set("compileSdk", 32)
        set("targetSdk", 32)
        set("minSdk", 23)
        set("majorVersion", majorVersion)
        set("minorVersion", minorVersion)
        set("patchVersion", patchVersion)
        set("versionName", versionName)
        set("versionCode", versionCode)
    }
}

/*
buildscript {
    extra.apply{
        set("sdkVersion", 28)
    }
}
또는

extra["sdkVersion"] = 28
rootProject.extra["sdkVersion"]
?

가져올때는
// 그냥 바로 밖에 써뒀을때는
extra["sdkVersion"] = 28 as Int

// 다른 블록에 묶어뒀을 경우.
rootProject.extra["sdkVersion"] as Int
*/

//extra["sdkVersion"] = 21