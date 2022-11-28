package page.smirnov.ridango.di.main

import page.smirnov.ridango.domain.main.interactor.navigation.OpenMainScreenImpl
import page.smirnov.ridango.domain.navigation.interactor.screen.OpenMainScreen
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    fun provideOpenMainScreen(
        router: Router
    ): OpenMainScreen = OpenMainScreenImpl(router = router)
}
