package com.keunsori.data.datasource

import com.keunsori.data.api.ApiService
import com.keunsori.data.api.AuthApiService
import com.keunsori.data.data.request.OauthRequest
import com.keunsori.data.data.request.RefreshRequest
import com.keunsori.data.data.response.OauthResponse
import com.keunsori.data.retrofit.getResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val authApiService: AuthApiService
) {
    suspend fun googleLogin(oauthRequest: OauthRequest): OauthResponse {
        return authApiService.googleLogin(oauthRequest).getResponse()
    }

    suspend fun refreshAccessToken(refreshRequest: RefreshRequest): OauthResponse {
        return authApiService.refreshAccessToken(refreshRequest).getResponse()
    }
}
