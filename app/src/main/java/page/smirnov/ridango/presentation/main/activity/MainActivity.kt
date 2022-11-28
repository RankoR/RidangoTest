package page.smirnov.ridango.presentation.main.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import page.smirnov.ridango.R
import page.smirnov.ridango.domain.navigation.navigator.CustomNavigator
import page.smirnov.ridango.presentation.core_ui.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    protected lateinit var router: Lazy<Router>

    @Inject
    protected lateinit var navigator: CustomNavigator

    @Inject
    protected lateinit var navigatorHolder: NavigatorHolder

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setupNavigator()

        viewModel.handleStart()
    }

    private fun setupNavigator() {
        navigator.activity = this
        navigator.containerId = R.id.fragmentContainer
    }

    override fun onResumeFragments() {
        super.onResumeFragments()

        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()

        super.onPause()
    }

    override fun onBackPressed() {
        router.get().exit()
    }
}
