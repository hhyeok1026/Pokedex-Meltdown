// Top-level build file where you can add configuration options common to all sub-projects/modules.

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    /*id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false*/
    //alias(libs.plugins.spotless)

    // plugin으로 작성한거만 되는듯.
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false

    // 보류
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false

    alias(libs.plugins.spotless) apply false
}

buildscript  {
   /* repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {

        // plugin에 작성된거는 안되는듯.
        //classpath(libs.plugins.android.application)
        //classpath(libs.plugins.android.library)
        //classpath(libs.plugins.kotlin.android)

        // 이거는 가능.
        //classpath(libs.agp)
        //classpath(libs.kotlin.gradlePlugin)
        //classpath(libs.hilt.plugin)
    }
*/
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
    repositories {
        google()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    }
}

/*tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}*/

subprojects {
    apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlin.time.ExperimentalTime",
        )
    }

    extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            /*ktlint().setUseExperimental(true).editorConfigOverride(
                kotlin.collections.mapOf(
                    "indent_size" to "2",
                    "continuation_indent_size" to "2"
                )
            )*/
            licenseHeaderFile(rootProject.file("spotless/spotless.license.kt"))
            trimTrailingWhitespace()
            endWithNewline()
        }
        format("kts") {
            target("**/*.kts")
            targetExclude("$buildDir/**/*.kts")
            licenseHeaderFile(rootProject.file("spotless/spotless.license.kt"), "(^(?![\\/ ]\\*).*$)")
        }
        format("xml") {
            target("**/*.xml")
            targetExclude("**/build/**/*.xml")
            licenseHeaderFile(rootProject.file("spotless/spotless.license.xml"), "(<[^!?])")
        }
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