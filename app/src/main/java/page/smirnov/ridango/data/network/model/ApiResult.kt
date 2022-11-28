package page.smirnov.ridango.data.network.model

import java.io.IOException
import okhttp3.Headers

sealed class ApiResult<T>(
    val isSuccessful: Boolean,
) {

    data class Success<T>(
        val code: Int,
        val headers: Headers,
        val body: T,
    ) : ApiResult<T>(isSuccessful = true)

    data class ApiError<T>(
        val code: Int,
        val headers: Headers,
    ) : ApiResult<T>(isSuccessful = false)

    data class NetworkError<T>(
        val throwable: IOException,
    ) : ApiResult<T>(isSuccessful = false)

    data class UnknownError<T>(
        val throwable: Throwable,
    ) : ApiResult<T>(isSuccessful = false)

    fun toResult(): Result<T> {
        return when (this) {
            is Success -> Result.success(body)
            is ApiError -> Result.failure(ApiException(code, headers.toMultimap()))
            is NetworkError -> Result.failure(throwable)
            is UnknownError -> Result.failure(throwable)
        }
    }
}
