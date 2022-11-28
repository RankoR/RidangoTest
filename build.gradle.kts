import plugin.BuildPlugins

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }

    dependencies {
        val kotlinVersion = "1.7.21"

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion") // For some reason it doesn't work from buildSrc
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion") // For some reason it doesn't work from buildSrc
    }
}

plugins.apply(BuildPlugins.UPDATE_DEPENDENCIES)

allprojects {
    plugins.apply(BuildPlugins.SPOTLESS)
}
