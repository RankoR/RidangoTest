package page.smirnov.ridango

import android.app.Application
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import page.smirnov.ridango.data.network.WebServer
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    @Inject
    protected lateinit var loggingTrees: Lazy<Collection<Timber.Tree>>

    @Inject
    protected lateinit var webServer: WebServer

    override fun onCreate() {
        super.onCreate()

        setupLogging()
        setupWebServer()
    }

    private fun setupLogging() {
        loggingTrees.get().forEach { tree ->
            Timber.plant(tree)
        }
    }

    private fun setupWebServer() {
        webServer.start()
    }
}
