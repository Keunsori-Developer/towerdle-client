package com.keunsori.towerdle.module


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.keunsori.towerdle.App.Companion.dataStore
import com.keunsori.data.api.ApiService
import com.keunsori.data.service.GoogleLoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    fun provideUrl(): String = "http://test"

    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            } // API 로그 출력

        return OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .addInterceptor(Interceptor.invoke { chain ->
                val builder = chain.request().newBuilder()
                builder.addHeader(
                    "Content-Type", "application/json; charset=utf-8"
                )
                chain.proceed(builder.build())
            }).build()
    }

    @Provides
    fun provideApiService(
        url: String,
        okHttpClient: OkHttpClient,
    ): ApiService =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
}
