package page.smirnov.ridango.di.core

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import page.smirnov.ridango.domain.core.interactor.IsDebug
import page.smirnov.ridango.domain.core.interactor.IsDebugImpl

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {

    @Provides
    @Reusable
    @DispatcherDefault
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Reusable
    @DispatcherIO
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Reusable
    @DispatcherMain
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Reusable
    fun provideIsDebug(): IsDebug = IsDebugImpl()
}
