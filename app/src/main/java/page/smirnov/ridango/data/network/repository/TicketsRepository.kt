package page.smirnov.ridango.data.network.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import page.smirnov.ridango.data.network.TicketsApi
import page.smirnov.ridango.data.network.WebServer
import page.smirnov.ridango.util.api.apiResultFlow
import tickets.TicketOuterClass.Ticket

interface TicketsRepository {

    fun create(ticket: Ticket): Flow<Ticket>
}

internal class TicketsRepositoryImpl(
    private val webServer: WebServer,
    private val ticketsApi: TicketsApi,
    private val ioDispatcher: CoroutineDispatcher,
) : TicketsRepository {

    override fun create(ticket: Ticket): Flow<Ticket> {
        webServer.enqueueRequest() // Normally it shouldn't be here

        return apiResultFlow {
            ticketsApi.create(ticket)
        }.flowOn(ioDispatcher)
    }
}
