package page.smirnov.ridango.di.network

import javax.inject.Qualifier

@Qualifier
annotation class ApiRetrofit

@Qualifier
internal annotation class ApiOkHttp

@Qualifier
internal annotation class LoggingInterceptor
