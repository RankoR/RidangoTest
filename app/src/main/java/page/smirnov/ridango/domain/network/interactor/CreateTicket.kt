package page.smirnov.ridango.domain.network.interactor

import kotlinx.coroutines.flow.Flow
import page.smirnov.ridango.data.network.repository.TicketsRepository
import tickets.TicketOuterClass.Ticket

interface CreateTicket {

    // TODO: Replace arguments with an encapsulating <Args> class
    fun exec(productName: String, productPrice: Float): Flow<Ticket>
}

internal class CreateTicketImpl(
    private val ticketsRepository: TicketsRepository,
) : CreateTicket {

    override fun exec(productName: String, productPrice: Float): Flow<Ticket> {
        return Ticket
            .newBuilder()
            .setId(0)
            .setProductName(productName)
            .setPrice((productPrice * 100f).toInt())
            .build()
            .let(ticketsRepository::create)
    }
}
