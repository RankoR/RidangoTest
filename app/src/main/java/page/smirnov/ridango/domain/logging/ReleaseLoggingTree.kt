package page.smirnov.ridango.domain.logging

import android.util.Log
import timber.log.Timber

internal class ReleaseLoggingTree(
    private val minPriority: Int,
    private val minAnalyticsLogPriority: Int
) : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority >= minPriority
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // On some MTK devices it may be null and crash
        @Suppress("SENSELESS_COMPARISON")
        if (message == null) {
            return
        }

        Log.println(priority, tag, message)

        val priorityText = when (priority) {
            Log.ERROR -> "E"
            Log.WARN -> "W"
            Log.INFO -> "I"
            Log.DEBUG -> "D"
            Log.VERBOSE -> "V"
            else -> "?"
        }

        if (isLoggableToAnalytics(tag, priority) && message.isNotBlank()) {
            // TODO
            // Analytics.log("$priorityText $message")
        }
    }

    private fun isLoggableToAnalytics(tag: String?, priority: Int): Boolean {
        return priority >= minAnalyticsLogPriority
    }
}
