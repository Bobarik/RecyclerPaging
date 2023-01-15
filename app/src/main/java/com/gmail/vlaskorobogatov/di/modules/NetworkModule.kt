package com.gmail.vlaskorobogatov.di.modules

import com.gmail.vlaskorobogatov.BuildConfig
import com.gmail.vlaskorobogatov.di.qualifiers.DefaultNetworkApi
import com.gmail.vlaskorobogatov.network.calladapters.ResultCallAdapterFactory
import com.gmail.vlaskorobogatov.network.services.LoadDataService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

private const val BASE_URL = "http://dev.bonusmoney.pro"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @DefaultNetworkApi
    fun provideBaseUrl(): HttpUrl {
        return BASE_URL.toHttpUrl()
    }

    @Provides
    @Singleton
    @DefaultNetworkApi
    fun provideOkHttpClient(
        @DefaultNetworkApi builder: OkHttpClient.Builder,
    ): OkHttpClient {
        return builder.build()
    }

    @Provides
    @Singleton
    @DefaultNetworkApi
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    this.addInterceptor(
                        HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BASIC)
                    )
                }
            }
    }

    @Provides
    @Singleton
    @DefaultNetworkApi
    fun provideRetrofit(
        @DefaultNetworkApi builder: Retrofit.Builder,
        @DefaultNetworkApi baseUrl: HttpUrl,
        @DefaultNetworkApi client: OkHttpClient,
        @DefaultNetworkApi gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return builder
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @DefaultNetworkApi
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
    }

    @Provides
    @Singleton
    @DefaultNetworkApi
    fun provideGson(
        @DefaultNetworkApi builder: GsonBuilder,
    ): Gson {
        return builder.create()
    }

    @Provides
    @Singleton
    @DefaultNetworkApi
    fun provideGsonBuilder(): GsonBuilder {
        return GsonBuilder()
    }

    @Provides
    @Singleton
    @DefaultNetworkApi
    fun provideGsonConverterFactory(
        @DefaultNetworkApi gson: Gson,
    ): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    @DefaultNetworkApi
    fun provideCoroutineContext(): CoroutineContext {
        val job = SupervisorJob()
        return (Dispatchers.IO + job)
    }

    @Provides
    @Singleton
    @DefaultNetworkApi
    fun provideService(@DefaultNetworkApi retrofit: Retrofit): LoadDataService {
        return retrofit.create(LoadDataService::class.java)
    }
}