@file:OptIn(ExperimentalCoroutinesApi::class)

package page.smirnov.ridango.di.logging

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import page.smirnov.ridango.domain.core.interactor.IsDebug
import page.smirnov.ridango.domain.logging.ReleaseLoggingTree
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
class LoggingModule {

    @Provides
    fun provideLoggingTrees(
        isDebug: IsDebug,
    ): Collection<Timber.Tree> {
        val defaultTree = if (isDebug.exec()) {
            Timber.DebugTree()
        } else {
            ReleaseLoggingTree(
                minPriority = if (isDebug.exec()) Log.VERBOSE else Log.WARN,
                minAnalyticsLogPriority = Log.ERROR
            )
        }

        return listOf(
            defaultTree,
        )
    }
}
