package com.keunsori.data.retrofit

import android.util.Log
import com.keunsori.data.api.ApiService
import com.keunsori.data.data.request.RefreshRequest
import com.keunsori.data.datasource.LocalDataSource
import com.keunsori.data.datasource.UserRemoteDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitService @Inject constructor(
    userLocalDataSource: LocalDataSource,
    tokenAuthenticator: TokenAuthenticator
) {
    private var baseUrl = "https://api.randommagic.xyz"


    private val interceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        } // API 로그 출력

    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(interceptor)
        .addInterceptor(OauthInterceptor(userLocalDataSource))
        .authenticator(tokenAuthenticator)
        .build()

    var service: ApiService = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}

class OauthInterceptor(
    private val userLocalDataSource: LocalDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            .addHeader(
                "Authorization",
                "Bearer ${userLocalDataSource.accessToken}"
            ).build()

        return chain.proceed(builder)
    }
}

@Singleton
class TokenAuthenticator @Inject constructor(
    private val loginApiService: OauthRetrofitService,
    private val localDataSource: LocalDataSource
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        Log.i("Authenticator", response.toString())
        Log.i("Authenticator", "토큰 재발급 시도")
        return try {
            runBlocking {
                val newAccessToken = loginApiService.service.refreshAccessToken(
                    refreshRequest = RefreshRequest(refreshToken = localDataSource.getRefreshToken())
                ).getResponse()
                if (newAccessToken != null){
                    localDataSource.setRefreshToken(newAccessToken.refreshToken)
                    localDataSource.setAccessToken(newAccessToken.accessToken)
                    Log.i("Authenticator", "토큰 재발급 성공 : $newAccessToken")
                    response.request.newBuilder()
                        .removeHeader("Authorization").apply {
                            addHeader("Authorization", "Bearer ${newAccessToken.accessToken}")
                        }.build()
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null // 만약 토큰 재발급이 실패했다면 헤더에 아무것도 추가하지 않는다.
        }
    }
}


