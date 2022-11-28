package page.smirnov.ridango.presentation.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import page.smirnov.ridango.di.core.DispatcherIO
import page.smirnov.ridango.di.core.DispatcherMain
import page.smirnov.ridango.domain.network.interactor.CreateTicket
import page.smirnov.ridango.presentation.core_ui.BaseViewModel
import page.smirnov.ridango.presentation.core_ui.ScreenState
import page.smirnov.ridango.util.extension.onFailureLog
import timber.log.Timber

@HiltViewModel
class MainViewModel @Inject constructor(
    private val createTicket: CreateTicket,
    @DispatcherIO
    private val ioDispatcher: CoroutineDispatcher,
    @DispatcherMain
    private val mainDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val productName = MutableStateFlow("")
    private val productPrice = MutableStateFlow(0f)

    private val _isSubmitBtnEnabled = MutableStateFlow(false)

    val isSubmitBtnEnabled = _isSubmitBtnEnabled.asStateFlow()

    private val _messages = Channel<String>() // TODO: Send res id or better some kind of enum
    val messages = _messages.consumeAsFlow()

    fun onProductNameChange(productName: String) {
        Timber.d("Product name changed: $productName")

        this.productName.value = productName

        updateSubmitBtnState()
    }

    fun onProductPriceChange(productPrice: Float) {
        Timber.d("Product price changed: $productPrice")

        this.productPrice.value = productPrice

        updateSubmitBtnState()
    }

    fun onSubmitClick() {
        Timber.d("Submit clicked")

        setScreenState(ScreenState.Loading)

        viewModelScope.launch(ioDispatcher) {
            createTicket
                .exec(
                    productName = productName.value,
                    productPrice = productPrice.value,
                )
                .onFailureLog()
                .handleBasicErrors(setScreenState = true) // For now we don't handle 4xx errors
                .collect { ticket ->
                    setScreenState(ScreenState.Content)

                    _messages.send("Ticket is created, id=${ticket.id}")
                }
        }
    }

    // There should be a better way with combining flows
    // But I don't have time to dig into combining State flows
    private fun updateSubmitBtnState() {
        _isSubmitBtnEnabled.value = productName.value.isNotBlank() && productPrice.value > 0f
    }
}
