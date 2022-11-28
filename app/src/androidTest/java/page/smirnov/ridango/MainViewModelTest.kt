@file:OptIn(ExperimentalCoroutinesApi::class)

package page.smirnov.ridango

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.IOException
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import page.smirnov.ridango.data.network.model.ApiException
import page.smirnov.ridango.domain.network.interactor.CreateTicket
import page.smirnov.ridango.presentation.core_ui.ScreenState
import page.smirnov.ridango.presentation.main.MainViewModel
import tickets.TicketOuterClass

class MainViewModelTest : BaseInstrumentedTest() {

    private val testDispatcher = StandardTestDispatcher()
    private val createTicket = mockk<CreateTicket>()

    private val defaultTicketResponseObject = TicketOuterClass.Ticket.newBuilder()
        .setId(1)
        .setProductName("Test")
        .setPrice(42)
        .build()

    private val viewModel = MainViewModel(
        createTicket = createTicket,
        ioDispatcher = testDispatcher,
        mainDispatcher = testDispatcher,
    )

    @Test
    fun testFieldsValidation() {
        assertFalse(viewModel.isSubmitBtnEnabled.value)

        viewModel.onProductNameChange("Test")
        assertFalse(viewModel.isSubmitBtnEnabled.value)

        viewModel.onProductPriceChange(0f)
        assertFalse(viewModel.isSubmitBtnEnabled.value)

        viewModel.onProductPriceChange(1f)
        assertTrue(viewModel.isSubmitBtnEnabled.value)

        viewModel.onProductNameChange("")
        assertFalse(viewModel.isSubmitBtnEnabled.value)
    }

    @Test
    fun testSuccessfulSubmit() {
        every {
            createTicket.exec(
                productName = any(),
                productPrice = any(),
            )
        } returns flowOf(defaultTicketResponseObject)

        assertEquals(ScreenState.Content, viewModel.screenState.value)

        viewModel.enterDataAndSubmit()

        testDispatcher.scheduler.advanceUntilIdle()

        runTest(testDispatcher) {
            viewModel
                .messages
                .test {
                    assertEquals("Ticket is created, id=${defaultTicketResponseObject.id}", awaitItem())
                }
        }

        assertEquals(ScreenState.Content, viewModel.screenState.value)

        verify(exactly = 1) {
            createTicket
                .exec(
                    productName = "Test Product",
                    productPrice = 313.37f,
                )
        }
    }

    @Test
    fun testNetworkErrorAndSuccessfullyRetriedSubmit() {
        every {
            createTicket.exec(
                productName = any(),
                productPrice = any(),
            )
        } returns flow { throw IOException() } andThen flowOf(defaultTicketResponseObject)

        assertEquals(ScreenState.Content, viewModel.screenState.value)

        viewModel.enterDataAndSubmit()

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(ScreenState.Error(errorResId = R.string.error_network_failure), viewModel.screenState.value)

        viewModel.onSubmitClick()
        testDispatcher.scheduler.advanceUntilIdle()

        runTest(testDispatcher) {
            viewModel
                .messages
                .test {
                    assertEquals("Ticket is created, id=${defaultTicketResponseObject.id}", awaitItem())
                }
        }

        assertEquals(ScreenState.Content, viewModel.screenState.value)

        verify(exactly = 2) {
            createTicket
                .exec(
                    productName = "Test Product",
                    productPrice = 313.37f,
                )
        }
    }

    @Test
    fun testBackendErrorAndSuccessfullyRetriedSubmit() {
        every {
            createTicket.exec(
                productName = any(),
                productPrice = any(),
            )
        } returns flow { throw ApiException(code = 500, headers = emptyMap()) } andThen flowOf(defaultTicketResponseObject)

        assertEquals(ScreenState.Content, viewModel.screenState.value)

        viewModel.enterDataAndSubmit()

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(ScreenState.Error(errorResId = R.string.error_server_failure), viewModel.screenState.value)

        viewModel.onSubmitClick()
        testDispatcher.scheduler.advanceUntilIdle()

        runTest(testDispatcher) {
            viewModel
                .messages
                .test {
                    assertEquals("Ticket is created, id=${defaultTicketResponseObject.id}", awaitItem())
                }
        }

        assertEquals(ScreenState.Content, viewModel.screenState.value)

        verify(exactly = 2) {
            createTicket
                .exec(
                    productName = "Test Product",
                    productPrice = 313.37f,
                )
        }
    }

    private fun MainViewModel.enterDataAndSubmit() {
        onProductNameChange("Test Product")
        onProductPriceChange(313.37f)
        onSubmitClick()
    }

}
