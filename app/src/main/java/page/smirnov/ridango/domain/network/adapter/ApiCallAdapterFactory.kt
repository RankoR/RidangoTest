package page.smirnov.ridango.domain.network.adapter

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import page.smirnov.ridango.data.network.model.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit

class ApiCallAdapterFactory : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        if (returnType !is ParameterizedType) {
            return null
        }

        val callTypeParameter = getParameterUpperBound(0, returnType)

        if (getRawType(callTypeParameter) != ApiResult::class.java) {
            return null
        }

        if (callTypeParameter !is ParameterizedType) {
            return null
        }

        val apiResultParameter = getParameterUpperBound(0, callTypeParameter)

        return ApiCallAdapter<Any>(apiResultParameter)
    }
}
