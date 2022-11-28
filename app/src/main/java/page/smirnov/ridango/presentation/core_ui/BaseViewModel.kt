package page.smirnov.ridango.presentation.core_ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import page.smirnov.ridango.R
import page.smirnov.ridango.data.network.model.ApiException
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Content)
    internal val screenState = _screenState.asStateFlow()

    private val _showErrorMessage = MutableSharedFlow<String>()
    internal val showErrorMessage = _showErrorMessage.asSharedFlow()

    private val _showErrorMessageResId = MutableSharedFlow<Int>()
    internal val showErrorMessageResId = _showErrorMessageResId.asSharedFlow()

    private val _showMessageResId = MutableSharedFlow<Int>()
    internal val showMessageResId = _showErrorMessageResId.asSharedFlow()

    open fun setScreenState(state: ScreenState) {
        _screenState.value = state
    }

    open fun showErrorMessage(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _showErrorMessage.emit(message)
        }
    }

    open fun showErrorMessage(
        @StringRes
        messageResId: Int,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            _showErrorMessageResId.emit(messageResId)
        }
    }

    open fun showErrorMessage(throwable: Throwable) {
        showErrorMessage(throwable.message.orEmpty())
    }

    open fun showMessage(
        @StringRes
        messageResId: Int,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            _showMessageResId.emit(messageResId)
        }
    }

    /**
     * Handles network and server errors
     */
    protected fun <T> Flow<T>.handleBasicErrors(setScreenState: Boolean = false): Flow<T> {
        return this.catch { throwable ->
            when {
                throwable is IOException -> {
                    Timber.d("Handling basic errors: I/O exception")

                    if (setScreenState) {
                        setScreenState(ScreenState.Error(errorResId = R.string.error_network_failure))
                    } else {
                        showErrorMessage(R.string.error_network_failure)
                    }
                }

                throwable is ApiException && throwable.isServer -> {
                    Timber.d("Handling basic errors: server exception")

                    if (setScreenState) {
                        setScreenState(ScreenState.Error(errorResId = R.string.error_server_failure))
                    } else {
                        showErrorMessage(R.string.error_server_failure)
                    }
                }
                else -> throw throwable
            }
        }
    }
}
