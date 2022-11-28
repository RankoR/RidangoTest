import com.google.protobuf.gradle.id
import dependency.Dependencies
import dimension.Dimensions
import extension.addCoreTestLibraries
import extension.addHilt
import version.BuildVersions
import version.addVersionsToBuildConfig

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.protobuf")
}

android {
    compileSdk = BuildVersions.COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = "page.smirnov.ridango"

        minSdk = BuildVersions.MIN_SDK_VERSION
        targetSdk = BuildVersions.TARGET_SDK_VERSION

        versionCode = BuildVersions.VERSION_CODE
        versionName = BuildVersions.versionName

        buildConfigField("String", "API_BASE_URL", "\"\"")
        buildConfigField("String", "API_KEY", "\"\"")

        addVersionsToBuildConfig()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets.getByName("main") {
        kotlin.srcDir("${protobuf.generatedFilesBaseDir}/main/javalite")
    }
    sourceSets.getByName("test") {
        kotlin.srcDir("${protobuf.generatedFilesBaseDir}/main/javalite")
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".test"

            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    flavorDimensions.addAll(
        listOf(
            Dimensions.Stand.NAME
        )
    )

    productFlavors {
        // API stands
        create(Dimensions.Stand.Flavors.Dev.NAME) {
            dimension = Dimensions.Stand.NAME

            versionNameSuffix = "-devStand"

            buildConfigField("String", "API_BASE_URL", "\"${Dimensions.Stand.Flavors.Dev.API_BASE_URL}\"")
            buildConfigField("String", "API_KEY", "\"${Dimensions.Stand.Flavors.Dev.API_KEY}\"")
        }

        create(Dimensions.Stand.Flavors.Prod.NAME) {
            dimension = Dimensions.Stand.NAME

            buildConfigField("String", "API_BASE_URL", "\"${Dimensions.Stand.Flavors.Prod.API_BASE_URL}\"")
            buildConfigField("String", "API_KEY", "\"${Dimensions.Stand.Flavors.Prod.API_KEY}\"")
        }
    }

    compileOptions {
        sourceCompatibility = BuildVersions.JAVA_VERSION
        targetCompatibility = BuildVersions.JAVA_VERSION
    }

    kotlinOptions {
        jvmTarget = BuildVersions.JAVA_VERSION.toString()

        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=kotlin.RequiresOptIn -Xuse-k2"
        )
    }

    kapt {
        correctErrorTypes = true
    }

    packagingOptions {
        resources.excludes.add("DebugProbesKt.bin")
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE-notice.md")
    }

    lint {
        checkReleaseBuilds = false
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"

        unitTests {
            isReturnDefaultValues = true
        }

        packagingOptions {
            jniLibs {
                // Hack for MockK, see: https://github.com/mockk/mockk/issues/297#issuecomment-901924678
                useLegacyPackaging = true
            }
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.9"
    }

    plugins {
        id("javalite") { artifact = "com.google.protobuf:protoc-gen-javalite:3.0.0" }
        id("grpc") { artifact = "io.grpc:protoc-gen-grpc-java:1.51.0" }
    }

    generateProtoTasks {
        all().forEach {
            it.builtins {
                create("java") {
                    option("lite")
                }
            }

            it.plugins {
                create("grpc") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(Dependencies.Libraries.KOTLIN_STD_LIB)

    implementation(Dependencies.Libraries.ANDROID_X_CORE_KTX)
    implementation(Dependencies.Libraries.ANDROID_X_COLLECTION_KTX)

    // Coroutines
    implementation(Dependencies.Libraries.COROUTINES_CORE)
    implementation(Dependencies.Libraries.COROUTINES_ANDROID)

    // AndroidX
    implementation(Dependencies.Libraries.ANDROID_X_APPCOMPAT)
    implementation(Dependencies.Libraries.ANDROID_X_FRAGMENT_KTX)

    // Material
    implementation(Dependencies.Libraries.GOOGLE_MATERIAL)

    // Constraint layout
    implementation(Dependencies.Libraries.ANDROID_X_CONSTRAINT_LAYOUT)

    // AndroidX
    implementation(Dependencies.Libraries.ANDROID_X_LIFECYCLE_VIEW_MODEL_KTX)
    implementation(Dependencies.Libraries.ANDROID_X_LIFECYCLE_RUNTIME_KTX)
    implementation(Dependencies.Libraries.ANDROID_X_LIFECYCLE_VIEW_MODEL_SAVED_STATE)
    implementation(Dependencies.Libraries.ANDROID_X_LIFECYCLE_COMMON_JAVA8)
    implementation(Dependencies.Libraries.ANDROID_X_LIFECYCLE_EXTENSIONS)

    // Cicerone
    implementation(Dependencies.Libraries.CICERONE)

    // Protobuf
    implementation(Dependencies.Libraries.PROTOBUF)

    // Hilt
    addHilt()
    kapt(Dependencies.Libraries.ANDROID_X_HILT_COMPILER)

    // OkHttp & retrofit
    implementation(Dependencies.Libraries.OKHTTP)
    implementation(Dependencies.Libraries.OKHTTP_LOGGING_INTERCEPTOR)
    implementation(Dependencies.Libraries.RETROFIT)
    implementation(Dependencies.Libraries.RETROFIT_CONVERTER_PROTOBUF) {
        exclude(group = "com.google.protobuf")
    }
    implementation(Dependencies.Libraries.MOCK_WEB_SERVER)

    // LeakCanary
    debugImplementation(Dependencies.Libraries.LEAK_CANARY)
    implementation(Dependencies.Libraries.LEAK_CANARY_PLUMBER)

    // Timber
    implementation(Dependencies.Libraries.TIMBER)

    // Tests
    addCoreTestLibraries()
    testImplementation(Dependencies.Libraries.MOCK_WEB_SERVER)
}
