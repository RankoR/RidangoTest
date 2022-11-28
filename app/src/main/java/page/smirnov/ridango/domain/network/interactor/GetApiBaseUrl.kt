package page.smirnov.ridango.domain.network.interactor

import page.smirnov.ridango.BuildConfig

interface GetApiBaseUrl {
    fun exec(): String
}

internal class GetApiBaseUrlImpl : GetApiBaseUrl {

    override fun exec(): String {
        return BuildConfig.API_BASE_URL
    }
}
