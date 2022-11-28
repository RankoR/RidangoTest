package page.smirnov.ridango.data.network.model


data class ApiException(
    val code: Int,
    val headers: Map<String, List<String>>,
) : Exception() {

    val isClient: Boolean
        get() = code in 400..499

    val isServer: Boolean
        get() = code in 500..599

    override fun toString(): String {
        return "ApiException(code=$code, isClient=$isClient, isServer=$isServer)"
    }
}
