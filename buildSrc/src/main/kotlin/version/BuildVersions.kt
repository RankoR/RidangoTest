@file:Suppress("UnstableApiUsage")

package version

import com.android.build.api.dsl.DefaultConfig
import org.gradle.api.JavaVersion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object BuildVersions {
    const val COMPILE_SDK_VERSION = 33
    const val MIN_SDK_VERSION = 23
    const val TARGET_SDK_VERSION = 33

    private const val VERSION_MAJOR = 1
    private const val VERSION_MINOR = 0
    private const val VERSION_PATCH = 0

    private const val VERSION_BUILD = 1

    const val VERSION_CODE = 1_000_000_000 + 1000 * (10000 * VERSION_MAJOR + 100 * VERSION_MINOR + VERSION_PATCH) + VERSION_BUILD

    private val buildDate = SimpleDateFormat("yy-MM-dd-HHmm", Locale.ROOT).format(Date())

    val versionName = "$VERSION_MAJOR.$VERSION_MINOR.$VERSION_PATCH-$buildDate"

    val JAVA_VERSION = JavaVersion.VERSION_11
}

fun DefaultConfig.addVersionsToBuildConfig() {
    buildConfigField("int", "VERSION_CODE", "${BuildVersions.VERSION_CODE}")
    buildConfigField("String", "VERSION_NAME", "\"${BuildVersions.versionName}\"")
}
