package com.keunsori.data.datasource

import com.keunsori.data.api.ApiService
import com.keunsori.data.api.OauthApiService
import com.keunsori.data.data.request.OauthRequest
import com.keunsori.data.data.request.RefreshRequest
import com.keunsori.data.data.response.OauthResponse
import com.keunsori.data.retrofit.OauthRetrofitService
import com.keunsori.data.retrofit.RetrofitService
import com.keunsori.data.retrofit.getResponse
import com.keunsori.domain.entity.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(
    private val apiService: RetrofitService,
    private val oauthApiService: OauthRetrofitService
) {
    suspend fun googleLogin(oauthRequest: OauthRequest): OauthResponse {
        return oauthApiService.service.googleLogin(oauthRequest).getResponse()
    }

    suspend fun refreshAccessToken(refreshRequest: RefreshRequest): OauthResponse {
        return oauthApiService.service.refreshAccessToken(refreshRequest).getResponse()
    }
}
