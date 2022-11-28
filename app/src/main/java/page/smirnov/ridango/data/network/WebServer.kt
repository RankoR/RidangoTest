package page.smirnov.ridango.data.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer
import tickets.TicketOuterClass

interface WebServer {

    fun start()
    fun enqueueRequest()
}

/**
 * Simple mock implementation
 */
internal class WebServerImpl(
    ioDispatcher: CoroutineDispatcher,
) : WebServer {

    private var currentId = 0

    // Hack for mocking the server (just like all the code in this file)
    private val coroutineScope = CoroutineScope(ioDispatcher)

    private lateinit var mockWebServer: MockWebServer

    override fun start() {
        coroutineScope.launch {
            mockWebServer = MockWebServer()
            mockWebServer.start(31337)
        }
    }

    override fun enqueueRequest() {
        mockWebServer.enqueue(MockResponse().setBody(
            TicketOuterClass.Ticket.newBuilder()
                .setId(++currentId)
                .setProductName("Test") // We don't parse the body in this impl
                .setPrice(1337)
                .build()
                .toByteArray()
                .let { bytes ->
                    Buffer().apply { write(bytes) }
                }
        ))
    }
}
