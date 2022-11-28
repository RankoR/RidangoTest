package page.smirnov.ridango.presentation.core_ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import page.smirnov.ridango.R
import page.smirnov.ridango.util.setOnSingleClickListener
import timber.log.Timber

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>,
) : Fragment() {

    protected abstract val screenName: String

    protected var binding: VB? = null

    protected abstract val viewModel: BaseViewModel?

    protected open var contentView: View? = null
    protected open var loaderView: View? = null
    protected open var errorView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflate
            .invoke(inflater, container, false)
            .also { binding = it }
            .let(::requireNotNull)
            .root
            .also { rootView ->
                contentView = rootView.findViewById(R.id.contentLayout)
                loaderView = rootView.findViewById(R.id.loaderLayout)
                errorView = rootView.findViewById(R.id.errorLayout)
                errorView?.findViewById<View>(R.id.retryBtn)
                    ?.setOnSingleClickListener { onErrorRetryButtonClick() }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupViewModel()
    }

    protected open fun setupView() {}

    protected open fun setupViewModel() {
        viewModel?.apply {
            launchRepeatingOn(Lifecycle.State.STARTED) {
                launch { screenState.collect(::changeScreenState) }
                launch { showErrorMessage.collect { this@BaseFragment.showErrorMessage(it) } }
                launch { showErrorMessageResId.collect { this@BaseFragment.showErrorMessage(it) } }
                launch { showMessageResId.collect { this@BaseFragment.showMessage(it) } }
            }
        }
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    protected fun launchRepeatingOn(
        state: Lifecycle.State,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(state, block)
        }
    }

    override fun onStart() {
        super.onStart()

        reportScreenViewStart()
    }

    override fun onStop() {
        reportScreenViewEnd()

        super.onStop()
    }

    override fun onDestroyView() {
        binding = null

        super.onDestroyView()
    }

    protected open fun reportScreenViewStart() {
        activity?.let {
            // TODO
            // Analytics.reportScreenViewStart(it, screenName)
        }
    }

    protected open fun reportScreenViewEnd() {
        // TODO
        // Analytics.reportScreenViewEnd(screenName)
    }

    protected open fun onErrorRetryButtonClick() {}

    protected open fun changeScreenState(state: ScreenState) {
        Timber.d("Set screen state: $state for $screenName")

        when (state) {
            is ScreenState.Content -> {
                contentView?.isVisible = true
                loaderView?.isVisible = false
                errorView?.isVisible = false
            }

            is ScreenState.Loading -> {
                contentView?.isVisible = false
                loaderView?.isVisible = true
                errorView?.isVisible = false
            }

            is ScreenState.Error -> {
                contentView?.isVisible = false
                loaderView?.isVisible = false
                errorView?.isVisible = true

                setErrorState(state)
            }
        }
    }

    private fun setErrorState(state: ScreenState.Error) {
        val errorText = when {
            state.errorResId != null -> getString(state.errorResId)
            state.errorText != null -> state.errorText
            else -> getString(R.string.error_unknown)
        }

        errorView?.findViewById<TextView>(R.id.errorTextTv)?.text = errorText
    }

    protected open fun showErrorMessage(messageResId: Int) {
        if (messageResId < 0) {
            return
        }

        showErrorMessage(getString(messageResId))
    }

    protected open fun showErrorMessage(message: String) {
        Timber.v("Show error: $message")

        if (message.isEmpty()) {
            return
        }

        showToast(message) // TODO
    }

    protected open fun showMessage(messageResId: Int) {
        Timber.d("Show message with res id = $messageResId")

        if (messageResId < 0) {
            return
        }

        showToast(getString(messageResId))
    }

    protected open fun showToast(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_LONG).show()
        }
    }
}
