package page.smirnov.ridango.di.network

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import page.smirnov.ridango.data.network.TicketsApi
import page.smirnov.ridango.data.network.WebServer
import page.smirnov.ridango.data.network.WebServerImpl
import page.smirnov.ridango.data.network.repository.TicketsRepository
import page.smirnov.ridango.data.network.repository.TicketsRepositoryImpl
import page.smirnov.ridango.di.core.DispatcherIO
import page.smirnov.ridango.domain.core.interactor.IsDebug
import page.smirnov.ridango.domain.network.adapter.ApiCallAdapterFactory
import page.smirnov.ridango.domain.network.interactor.CreateTicket
import page.smirnov.ridango.domain.network.interactor.CreateTicketImpl
import page.smirnov.ridango.domain.network.interactor.GetApiBaseUrl
import page.smirnov.ridango.domain.network.interactor.GetApiBaseUrlImpl
import retrofit2.Retrofit
import retrofit2.converter.protobuf.ProtoConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideGetApiBaseUrl(): GetApiBaseUrl = GetApiBaseUrlImpl()

    @Provides
    @Reusable
    @LoggingInterceptor
    fun provideLoggingInterceptor(
        isDebug: IsDebug,
    ): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = if (isDebug.exec()) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @ApiOkHttp
    fun provideOkHttpClient(
        @LoggingInterceptor
        loggingInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(API_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(API_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(API_READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Reusable
    fun provideApiCallAdapterFactory(): ApiCallAdapterFactory = ApiCallAdapterFactory()

    @Provides
    @ApiRetrofit
    fun provideRetrofit(
        @ApiOkHttp
        okHttpClient: OkHttpClient,
        getApiBaseUrl: GetApiBaseUrl,
        apiCallAdapterFactory: ApiCallAdapterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(getApiBaseUrl.exec())
            .addConverterFactory(ProtoConverterFactory.create())
            .addCallAdapterFactory(apiCallAdapterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Reusable
    fun provideTicketsApi(
        @ApiRetrofit
        retrofit: Retrofit,
    ): TicketsApi {
        return retrofit.create(TicketsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTicketsRepository(
        webServer: WebServer,
        ticketsApi: TicketsApi,
        @DispatcherIO
        ioDispatcher: CoroutineDispatcher,
    ): TicketsRepository = TicketsRepositoryImpl(
        webServer = webServer,
        ticketsApi = ticketsApi,
        ioDispatcher = ioDispatcher,
    )

    @Provides
    fun provideCreateTicket(
        ticketsRepository: TicketsRepository,
    ): CreateTicket = CreateTicketImpl(
        ticketsRepository = ticketsRepository,
    )

    @Provides
    @Singleton
    fun provideWebServer(
        @DispatcherIO
        ioDispatcher: CoroutineDispatcher,
    ): WebServer = WebServerImpl(
        ioDispatcher = ioDispatcher,
    )

    private companion object {

        private const val API_CONNECT_TIMEOUT = 60L
        private const val API_WRITE_TIMEOUT = 90L
        private const val API_READ_TIMEOUT = 120L
    }
}
