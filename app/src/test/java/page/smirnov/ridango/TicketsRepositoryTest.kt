@file:OptIn(ExperimentalCoroutinesApi::class)

package page.smirnov.ridango

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.Headers
import org.junit.Test
import page.smirnov.ridango.data.network.TicketsApi
import page.smirnov.ridango.data.network.WebServer
import page.smirnov.ridango.data.network.model.ApiException
import page.smirnov.ridango.data.network.model.ApiResult
import page.smirnov.ridango.data.network.repository.TicketsRepository
import page.smirnov.ridango.data.network.repository.TicketsRepositoryImpl
import tickets.TicketOuterClass

class TicketsRepositoryTest {

    private val ticketsApi = mockk<TicketsApi>()
    private val webServer = mockk<WebServer>().apply {
        every { enqueueRequest() } returns Unit
    }

    private val testDispatcher = StandardTestDispatcher()

    private val ticketsRepository: TicketsRepository = TicketsRepositoryImpl(
        webServer = webServer,
        ticketsApi = ticketsApi,
        ioDispatcher = testDispatcher,
    )

    private val defaultTicketRequestObject = TicketOuterClass.Ticket.newBuilder()
        .setId(0)
        .setProductName("Test")
        .setPrice(42)
        .build()

    private val defaultTicketResponseObject = TicketOuterClass.Ticket.newBuilder()
        .setId(1)
        .setProductName("Test")
        .setPrice(42)
        .build()

    @Test
    fun `normal response`() {
        coEvery { ticketsApi.create(any()) } returns ApiResult.Success(
            code = 200,
            headers = Headers.headersOf(),
            body = defaultTicketResponseObject,
        )

        runTest(testDispatcher) {
            ticketsRepository
                .create(defaultTicketRequestObject)
                .test {
                    assertEquals(defaultTicketResponseObject, awaitItem())
                    awaitComplete()
                }
        }

        coVerify(exactly = 1) {
            ticketsApi.create(ticket = eq(defaultTicketRequestObject))
        }
    }

    @Test
    fun `one server failure and success on retry`() {
        coEvery { ticketsApi.create(any()) } returns ApiResult.ApiError(
            code = 500,
            headers = Headers.headersOf(),
        ) andThen ApiResult.Success(
            code = 200,
            headers = Headers.headersOf(),
            body = defaultTicketResponseObject,
        )

        runTest(testDispatcher) {
            ticketsRepository
                .create(defaultTicketRequestObject)
                .test {
                    assertEquals(defaultTicketResponseObject, awaitItem())
                    awaitComplete()
                }
        }

        coVerify(exactly = 2) {
            ticketsApi.create(ticket = eq(defaultTicketRequestObject))
        }
    }

    @Test
    fun `one network failure and success on retry`() {
        coEvery { ticketsApi.create(any()) } returns ApiResult.NetworkError(
            throwable = IOException(),
        ) andThen ApiResult.Success(
            code = 200,
            headers = Headers.headersOf(),
            body = defaultTicketResponseObject,
        )

        runTest(testDispatcher) {
            ticketsRepository
                .create(defaultTicketRequestObject)
                .test {
                    assertEquals(defaultTicketResponseObject, awaitItem())
                    awaitComplete()
                }
        }

        coVerify(exactly = 2) {
            ticketsApi.create(ticket = eq(defaultTicketRequestObject))
        }
    }

    @Test
    fun `one client failure with no retry`() {
        coEvery { ticketsApi.create(any()) } returns ApiResult.ApiError(
            code = 400,
            headers = Headers.headersOf(),
        )

        runTest(testDispatcher) {
            ticketsRepository
                .create(defaultTicketRequestObject)
                .test {
                    val error = awaitError()
                    assertEquals(ApiException::class.java, error.javaClass)

                    val apiException = error as ApiException

                    assertEquals(400, apiException.code)
                }
        }

        coVerify(exactly = 1) {
            ticketsApi.create(ticket = eq(defaultTicketRequestObject))
        }
    }

    @Test
    fun `one unknown failure with no retry`() {
        coEvery { ticketsApi.create(any()) } returns ApiResult.UnknownError(
            throwable = Throwable(),
        )

        runTest(testDispatcher) {
            ticketsRepository
                .create(defaultTicketRequestObject)
                .test {
                    awaitError()
                }
        }

        coVerify(exactly = 1) {
            ticketsApi.create(ticket = eq(defaultTicketRequestObject))
        }
    }


    @Test
    fun `server errors until giving up with retries`() {
        coEvery { ticketsApi.create(any()) } returns ApiResult.ApiError(
            code = 500,
            headers = Headers.headersOf(),
        )

        runTest(testDispatcher) {
            ticketsRepository
                .create(defaultTicketRequestObject)
                .test {
                    val error = awaitError()
                    assertEquals(ApiException::class.java, error.javaClass)

                    val apiException = error as ApiException

                    assertEquals(500, apiException.code)
                }
        }

        coVerify(exactly = 6) { // 6 = one call + 5 retries
            ticketsApi.create(ticket = eq(defaultTicketRequestObject))
        }
    }

}
