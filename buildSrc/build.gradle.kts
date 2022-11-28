plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    mavenCentral()
    google()
    maven("https://plugins.gradle.org/m2/")
}

private object Dependencies {
    object BuildScript {

        const val TOOLS_GRADLE = "com.android.tools.build:gradle:${Versions.TOOLS_GRADLE}"
        const val KOTLIN_GRADLE = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"

        const val HILT = "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}"

        const val PROTOBUF = "com.google.protobuf:protobuf-gradle-plugin:${Versions.PROTOBUF}"

        const val GRADLE_VERSIONS = "com.github.ben-manes:gradle-versions-plugin:${Versions.GRADLE_VERSIONS}"
        const val SPOTLESS = "com.diffplug.spotless:spotless-plugin-gradle:${Versions.SPOTLESS}"

        private object Versions {

            const val TOOLS_GRADLE = "7.3.1"
            const val KOTLIN = "1.7.21"

            const val GRADLE_VERSIONS = "0.42.0"
            const val SPOTLESS = "6.5.2"

            const val GOOGLE_SERVICES = "4.3.13"

            const val HILT = "2.44.2"
            const val PROTOBUF = "0.9.1"
        }
    }
}

dependencies {
    implementation(Dependencies.BuildScript.TOOLS_GRADLE)
    implementation(Dependencies.BuildScript.KOTLIN_GRADLE)

    implementation(Dependencies.BuildScript.HILT)

    implementation(Dependencies.BuildScript.PROTOBUF)

    implementation(Dependencies.BuildScript.SPOTLESS)
    implementation(Dependencies.BuildScript.GRADLE_VERSIONS)
}
