package page.smirnov.ridango.domain.network.adapter

import java.lang.reflect.Type
import page.smirnov.ridango.data.network.model.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter

class ApiCallAdapter<T>(
    private val type: Type,
) : CallAdapter<T, Call<ApiResult<T>>> {

    override fun responseType(): Type = type
    override fun adapt(call: Call<T>): Call<ApiResult<T>> = ApiCall(call)
}
