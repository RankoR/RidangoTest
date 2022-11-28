package page.smirnov.ridango.domain.main.interactor.navigation

import page.smirnov.ridango.domain.navigation.interactor.screen.OpenMainScreen
import page.smirnov.ridango.presentation.main.MainFragment
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen

internal class OpenMainScreenImpl(
    private val router: Router
) : OpenMainScreen {

    override fun exec() {
        FragmentScreen { MainFragment.newInstance() }.let(router::newRootScreen)
    }
}
