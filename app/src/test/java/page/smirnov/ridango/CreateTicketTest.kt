@file:OptIn(ExperimentalCoroutinesApi::class)

package page.smirnov.ridango

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import page.smirnov.ridango.data.network.repository.TicketsRepository
import page.smirnov.ridango.domain.network.interactor.CreateTicket
import page.smirnov.ridango.domain.network.interactor.CreateTicketImpl
import tickets.TicketOuterClass

class CreateTicketTest {

    private val defaultTicketRequestObject = TicketOuterClass.Ticket.newBuilder()
        .setId(0)
        .setProductName("Test Product Name")
        .setPrice(1337)
        .build()

    private val defaultTicketResponseObject = TicketOuterClass.Ticket.newBuilder()
        .setId(1)
        .setProductName("Test")
        .setPrice(42)
        .build()

    private val ticketsRepository = mockk<TicketsRepository>().apply {
        every { create(any()) } returns flowOf(defaultTicketResponseObject)
    }

    private val createTicket: CreateTicket = CreateTicketImpl(
        ticketsRepository = ticketsRepository,
    )

    /**
     * Not so much to test here — just validate that fields are mapped correctly
     */
    @Test
    fun `tickets fields mapping`() {
        runTest {
            createTicket
                .exec(
                    productName = "Test Product Name",
                    productPrice = 13.37f,
                )
                .test {
                    assertEquals(defaultTicketResponseObject, awaitItem())
                    awaitComplete()
                }
        }

        verify(exactly = 1) {
            ticketsRepository.create(
                ticket = eq(defaultTicketRequestObject),
            )
        }
    }
}
