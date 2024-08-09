package com.keunsori.data.retrofit

import com.keunsori.data.api.ApiService
import com.keunsori.data.api.OauthApiService
import com.keunsori.data.data.request.OauthRequest
import com.keunsori.data.data.response.OauthResponse
import com.keunsori.data.datasource.LocalDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OauthRetrofitService @Inject constructor() {
    private var baseUrl = "https://api.randommagic.xyz"


    private val interceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        } // API 로그 출력

    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(interceptor)
        .build()

    var service: OauthApiService = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OauthApiService::class.java)
}