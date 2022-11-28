package page.smirnov.ridango.domain.network.adapter

import java.io.IOException
import okhttp3.Request
import okio.Timeout
import page.smirnov.ridango.data.network.model.ApiResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiCall<T>(
    private val wrappedCall: Call<T>,
) : Call<ApiResult<T>> {

    override fun execute(): Response<ApiResult<T>> {
        return try {
            Response.success(wrappedCall.execute().process())
        } catch (t: Throwable) {
            Response.success(t.process())
        }
    }

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        wrappedCall.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(this@ApiCall, Response.success(response.process()))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(this@ApiCall, Response.success(t.process()))
            }
        })
    }

    private fun Response<T>.process(): ApiResult<T> {
        return if (isSuccessful) {
            body()
                ?.let { body: T ->
                    return ApiResult.Success(
                        code = code(),
                        headers = headers(),
                        body = body
                    )
                }
                ?: let {
                    ApiResult.UnknownError(Throwable("Body is null"))
                }
        } else {
            ApiResult.ApiError(
                code = code(),
                headers = headers() // No body handling currently
            )
        }
    }

    private fun Throwable.process(): ApiResult<T> {
        return when (this) {
            is IOException -> ApiResult.NetworkError(this)
            else -> ApiResult.UnknownError(this)
        }
    }

    override fun isExecuted() = wrappedCall.isExecuted
    override fun cancel() = wrappedCall.cancel()
    override fun clone() = ApiCall(wrappedCall)
    override fun isCanceled() = wrappedCall.isCanceled
    override fun request(): Request = wrappedCall.request()
    override fun timeout(): Timeout = wrappedCall.timeout()
}
