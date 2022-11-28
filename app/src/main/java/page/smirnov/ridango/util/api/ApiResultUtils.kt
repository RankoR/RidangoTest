package page.smirnov.ridango.util.api

import java.io.IOException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import page.smirnov.ridango.data.network.model.ApiException
import page.smirnov.ridango.data.network.model.ApiResult
import timber.log.Timber

class ApiCallRetryPolicy(
    val retryCount: Int,
    val retryDelay: Long,
    val retryDelayIncrement: Long,
) {

    companion object {

        private const val DEFAULT_RETRY_COUNT = 5
        private const val DEFAULT_RETRY_DELAY = 1000L
        private const val DEFAULT_RETRY_DELAY_INCREMENT = 1000L

        val default = ApiCallRetryPolicy(
            retryCount = DEFAULT_RETRY_COUNT,
            retryDelay = DEFAULT_RETRY_DELAY,
            retryDelayIncrement = DEFAULT_RETRY_DELAY_INCREMENT
        )
    }
}

fun <T> Flow<T>.retryApiCallWithPolicy(policy: ApiCallRetryPolicy): Flow<T> {
    return retryWhen { cause, attempt ->
        // Retry only on network or backend errors
        val shouldRetry = attempt < policy.retryCount && ((cause is IOException) || (cause is ApiException && cause.isServer))

        Timber.w("API Call failed with $cause, will retry = $shouldRetry")

        if (shouldRetry) {
            val retryDelay = policy.retryDelay + policy.retryDelayIncrement * attempt

            Timber.d("Will retry with delay = $retryDelay ms")

            delay(retryDelay)
        }

        shouldRetry
    }
}

inline fun <T> apiResultFlow(
    expectedCodes: IntRange = 200..299,
    retryPolicy: ApiCallRetryPolicy = ApiCallRetryPolicy.default,
    crossinline block: suspend () -> ApiResult<T>,
): Flow<T> {
    return flow {
        when (val result = block()) {
            is ApiResult.Success -> {
                if (result.code in expectedCodes) {
                    emit(result.body)
                } else {
                    throw ApiException(
                        code = result.code,
                        headers = result.headers.toMultimap(),
                    )
                }
            }
            is ApiResult.ApiError -> {
                throw ApiException(
                    code = result.code,
                    headers = result.headers.toMultimap(),
                )
            }
            is ApiResult.NetworkError -> {
                throw result.throwable
            }
            is ApiResult.UnknownError -> {
                throw result.throwable
            }
        }
    }.retryApiCallWithPolicy(retryPolicy)
}

inline fun <T> apiResultFlow(
    expectedCode: Int,
    retryPolicy: ApiCallRetryPolicy = ApiCallRetryPolicy.default,
    crossinline block: suspend () -> ApiResult<T>,
): Flow<T> {
    return apiResultFlow(expectedCode..expectedCode, retryPolicy, block)
}
