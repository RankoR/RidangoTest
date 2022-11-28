package page.smirnov.ridango.di.navigation

import page.smirnov.ridango.domain.navigation.interactor.CloseCurrentScreen
import page.smirnov.ridango.domain.navigation.interactor.CloseCurrentScreenImpl
import page.smirnov.ridango.domain.navigation.navigator.CustomNavigator
import page.smirnov.ridango.domain.navigation.navigator.CustomNavigatorImpl
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NavigationModule {

    @Provides
    @Singleton
    fun provideCicerone(): Cicerone<Router> {
        return Cicerone.create()
    }

    @Provides
    @Singleton
    fun provideRouter(
        cicerone: Cicerone<Router>,
    ): Router = cicerone.router

    @Provides
    @Singleton
    fun provideNavigationHolder(
        cicerone: Cicerone<Router>,
    ): NavigatorHolder = cicerone.getNavigatorHolder()

    @Provides
    @Singleton
    fun provideNavigator(): CustomNavigator = CustomNavigatorImpl()

    @Provides
    @Reusable
    fun provideCloseCurrentScreen(
        router: Router,
    ): CloseCurrentScreen = CloseCurrentScreenImpl(router)
}
