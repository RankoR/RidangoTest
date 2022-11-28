package page.smirnov.ridango.presentation.main

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import page.smirnov.ridango.databinding.FragmentMainBinding
import page.smirnov.ridango.presentation.core_ui.BaseFragment
import page.smirnov.ridango.util.extension.hideKeyboard
import page.smirnov.ridango.util.extension.textFlow
import page.smirnov.ridango.util.setOnSingleClickListener
import timber.log.Timber


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    override val screenName: String = SCREEN_NAME

    override val viewModel: MainViewModel? by viewModels()

    override fun setupView() {
        super.setupView()

        binding?.apply {
            // We probably should restart it on start, but I don't wanna do it in a test task

            lifecycleScope.launch {
                productNameEt
                    .textFlow
                    .collect { productName ->
                        viewModel?.onProductNameChange(productName)
                    }
            }

            lifecycleScope.launch {
                productPriceEt
                    .textFlow
                    .map { it.toFloatOrNull() ?: 0f }
                    .collect { productPrice ->
                        viewModel?.onProductPriceChange(productPrice)
                    }
            }

            submitBtn.setOnSingleClickListener {
                viewModel?.onSubmitClick()
                hideKeyboard()
            }
        }
    }

    override fun setupViewModel() {
        super.setupViewModel()

        viewModel?.apply {
            lifecycleScope.launch {
                launch { isSubmitBtnEnabled.collect(::handleIsSubmitBtnEnabled) }
                launch { messages.collect(::showErrorMessage) }
            }
        }
    }

    override fun onErrorRetryButtonClick() {
        super.onErrorRetryButtonClick()

        viewModel?.onSubmitClick()
    }

    private fun handleIsSubmitBtnEnabled(isEnabled: Boolean) {
        Timber.d("handleIsSubmitBtnEnabled: isEnabled=$isEnabled")

        binding?.submitBtn?.isEnabled = isEnabled
    }

    companion object {

        private const val SCREEN_NAME = "Main"

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}
