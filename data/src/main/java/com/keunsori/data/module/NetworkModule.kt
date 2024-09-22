package com.keunsori.data.module

import android.util.Log
import com.keunsori.data.api.ApiService
import com.keunsori.data.api.AuthApiService
import com.keunsori.data.data.request.RefreshRequest
import com.keunsori.data.datasource.LocalDataSource
import com.keunsori.data.retrofit.getResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainInterceptorOkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.randommagic.xyz"

    @Provides
    fun provideTokenAuthenticator(
        localDataSource: LocalDataSource,
        authApiService: AuthApiService
    ): Authenticator {
        return Authenticator { _, response ->
            Log.i("Authenticator", response.toString())
            Log.i("Authenticator", "토큰 재발급 시도")
            try {
                synchronized(this) {
                    val newAccessToken = authApiService.refreshAccessToken(
                        refreshRequest = RefreshRequest(
                            refreshToken = localDataSource.refreshToken
                        )
                    ).execute()

                    val res = newAccessToken.body()
                    if (res != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            localDataSource.setRefreshToken(res.refreshToken)
                        }
                        localDataSource.setAccessToken(res.accessToken)
                        Log.i("Authenticator", "토큰 재발급 성공 : $newAccessToken")
                        response.request.newBuilder()
                            .removeHeader("Authorization").apply {
                                addHeader("Authorization", "Bearer ${res.accessToken}")
                            }.build()
                    } else null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null // 만약 토큰 재발급이 실패했다면 헤더에 아무것도 추가하지 않는다.
            }
        }
    }

    @AuthInterceptorOkHttpClient
    @Provides
    fun provideAuthInterceptorOkHttpClient(): OkHttpClient {
        val interceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @MainInterceptorOkHttpClient
    @Provides
    fun provideMainInterceptorOkHttpClient(
        localDataSource: LocalDataSource,
        tokenAuthenticator: Authenticator
    ): OkHttpClient {
        val interceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        return OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .addInterceptor(Interceptor.invoke { chain ->
                val builder = chain.request().newBuilder()
                if (localDataSource.accessToken.isNotEmpty()) {
                    builder.addHeader(
                        "Authorization",
                        "Bearer ${localDataSource.accessToken}"
                    )
                }
                chain.proceed(builder.build())
            })
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    fun provideAuthApiService(
        @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient,
    ): AuthApiService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    @Provides
    fun provideMainApiService(
        @MainInterceptorOkHttpClient okHttpClient: OkHttpClient,
    ): ApiService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
