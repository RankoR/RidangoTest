package page.smirnov.ridango.domain.navigation.interactor

import com.github.terrakok.cicerone.Router

interface CloseCurrentScreen {

    fun exec()
}

internal class CloseCurrentScreenImpl(
    private val router: Router,
) : CloseCurrentScreen {

    override fun exec() {
        router.exit()
    }
}
