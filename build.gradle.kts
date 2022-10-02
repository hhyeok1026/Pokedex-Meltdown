// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {

    id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
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
extra["sdkVersion"] = 28 as Int
rootProject.extra["sdkVersion"] as Int

*/
extra["sdkVersion"] = 21

