package dependency

object Dependencies {

    // Libraries
    object Libraries {

        // Kotlin
        const val KOTLIN_STD_LIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"

        // Coroutines
        const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
        const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"


        // AndroidX
        const val ANDROID_X_CORE_KTX = "androidx.core:core-ktx:${Versions.ANDROID_X_CORE_KTX}"
        const val ANDROID_X_COLLECTION_KTX = "androidx.collection:collection-ktx:${Versions.ANDROID_X_COLLECTION_KTX}"
        const val ANDROID_X_APPCOMPAT = "androidx.appcompat:appcompat:${Versions.ANDROID_X_APPCOMPAT}"
        const val ANDROID_X_CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.ANDROID_X_CONSTRAINT_LAYOUT}"
        const val ANDROID_X_FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${Versions.ANDROID_X_FRAGMENT_KTX}"
        const val ANDROID_X_LIFECYCLE_RUNTIME_KTX = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.ANDROID_X_LIFECYCLE}"

        const val ANDROID_X_LIFECYCLE_VIEW_MODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.ANDROID_X_LIFECYCLE}"
        const val ANDROID_X_LIFECYCLE_VIEW_MODEL_SAVED_STATE = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.ANDROID_X_LIFECYCLE}"
        const val ANDROID_X_LIFECYCLE_COMMON_JAVA8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.ANDROID_X_LIFECYCLE}"
        const val ANDROID_X_LIFECYCLE_EXTENSIONS = "androidx.lifecycle:lifecycle-extensions:${Versions.ANDROID_X_LIFECYCLE_EXTENSIONS}"

        // Google material
        const val GOOGLE_MATERIAL = "com.google.android.material:material:${Versions.GOOGLE_MATERIAL}"

        // Hilt
        const val HILT_ANDROID = "com.google.dagger:hilt-android:${Versions.HILT}"
        const val HILT_COMPILER = "com.google.dagger:hilt-compiler:${Versions.HILT}"
        const val HILT_ANDROID_COMPILER = "com.google.dagger:hilt-android-compiler:${Versions.HILT}"
        const val ANDROID_X_HILT_COMPILER = "androidx.hilt:hilt-compiler:${Versions.ANDROID_X_HILT}"
        const val ANDROID_X_HILT_WORK = "androidx.hilt:hilt-work:${Versions.ANDROID_X_HILT}"

        // OkHttp & retrofit
        const val OKHTTP = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
        const val OKHTTP_LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"
        const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
        const val RETROFIT_CONVERTER_PROTOBUF = "com.squareup.retrofit2:converter-protobuf:${Versions.RETROFIT}"
        const val MOCK_WEB_SERVER = "com.squareup.okhttp3:mockwebserver:${Versions.MOCK_WEB_SERVER}"

        // Timber
        const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"

        // Leak Canary
        const val LEAK_CANARY = "com.squareup.leakcanary:leakcanary-android:${Versions.LEAK_CANARY}"
        const val LEAK_CANARY_PLUMBER = "com.squareup.leakcanary:plumber-android:${Versions.LEAK_CANARY}"

        // Cicerone
        const val CICERONE = "com.github.terrakok:cicerone:${Versions.CICERONE}"

        // Protobuf
        const val PROTOBUF = "com.google.protobuf:protobuf-javalite:${Versions.PROTOBUF}"

        // Test
        const val JUNIT = "junit:junit:${Versions.JUNIT}"
        const val COROUTINES_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES}"
        const val ANDROIDX_TEST_ORCHESTRATOR = "androidx.test:orchestrator:${Versions.ANDROIDX_TEST_ORCHESTRATOR}"
        const val ANDROIDX_TEST_CORE = "androidx.test:core:${Versions.ANDROIDX_TEST}"
        const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
        const val ANDROIDX_TEST_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST}"
        const val ANDROIDX_TEST_EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST_EXT_JUNIT}"
        const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"
        const val MOCKK_ANDROID = "io.mockk:mockk-android:${Versions.MOCKK}"
        const val MOCKK_AGENT_JVM = "io.mockk:mockk-agent-jvm:${Versions.MOCKK}"
        const val TURBINE = "app.cash.turbine:turbine:${Versions.TURBINE}"

        /**
         * Versions
         */
        object Versions {

            const val KOTLIN = "1.7.21"

            const val COROUTINES = "1.6.4"

            // AndroidX
            const val ANDROID_X_CORE_KTX = "1.9.0"
            const val ANDROID_X_COLLECTION_KTX = "1.2.0"
            const val ANDROID_X_APPCOMPAT = "1.5.1"
            const val ANDROID_X_CONSTRAINT_LAYOUT = "2.1.4"
            const val ANDROID_X_FRAGMENT_KTX = "1.5.4"
            const val ANDROID_X_LIFECYCLE = "2.5.1"
            const val ANDROID_X_LIFECYCLE_EXTENSIONS = "2.2.0"

            const val GOOGLE_MATERIAL = "1.7.0"

            const val HILT = "2.44.2"
            const val ANDROID_X_HILT = "1.0.0"

            const val CICERONE = "7.1"

            const val OKHTTP = "4.10.0"
            const val RETROFIT = "2.9.0"
            const val MOCK_WEB_SERVER = "4.10.0"

            const val PROTOBUF = "3.21.9"

            const val TIMBER = "5.0.1"
            const val LEAK_CANARY = "2.10"

            // Tests
            const val JUNIT = "4.13.2"
            const val ANDROIDX_TEST_ORCHESTRATOR = "1.4.2"
            const val ANDROIDX_TEST = "1.5.0"
            const val ANDROIDX_TEST_EXT_JUNIT = "1.1.3"
            const val MOCKK = "1.13.2"
            const val TURBINE = "0.12.1"
        }
    }
}
