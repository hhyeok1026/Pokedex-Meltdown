pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        jcenter()
    }
}

rootProject.name = "Pokedex-Meltdown"
include(":app")
include(":core-model")
include(":core-data")
include(":core-database")
include(":core-network")
include(":core-test")
include(":benchmark")
