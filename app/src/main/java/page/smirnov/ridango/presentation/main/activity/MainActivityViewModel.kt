package page.smirnov.ridango.presentation.main.activity

import page.smirnov.ridango.presentation.core_ui.BaseViewModel
import page.smirnov.ridango.domain.navigation.interactor.screen.OpenMainScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val openMainScreen: OpenMainScreen,
) : BaseViewModel() {

    fun handleStart() {
        openMainScreen.exec()
    }
}
