package page.smirnov.ridango.data.network

import page.smirnov.ridango.data.network.model.ApiResult
import retrofit2.http.Body
import retrofit2.http.POST
import tickets.TicketOuterClass.Ticket

interface TicketsApi {

    @POST("/")
    suspend fun create(
        @Body ticket: Ticket,
    ): ApiResult<Ticket>
}
