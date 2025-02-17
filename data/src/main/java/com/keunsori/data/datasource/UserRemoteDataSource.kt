package com.keunsori.data.datasource

import android.util.Log
import com.keunsori.data.api.MainApiService
import com.keunsori.data.api.AuthApiService
import com.keunsori.data.data.request.GuestRequest
import com.keunsori.data.data.request.OauthRequest
import com.keunsori.data.data.request.RefreshRequest
import com.keunsori.data.data.response.GetUserInfoResponse
import com.keunsori.data.data.response.GuestResponse
import com.keunsori.data.data.response.OauthResponse
import com.keunsori.data.retrofit.getResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(
    private val mainApiService: MainApiService,
    private val authApiService: AuthApiService
) {
    suspend fun googleLogin(oauthRequest: OauthRequest): OauthResponse {
        return authApiService.googleLogin(oauthRequest).getResponse()
    }

    suspend fun guestLogin(guestRequest: GuestRequest): GuestResponse {
        return authApiService.guestLogin(guestRequest).getResponse()
    }

    suspend fun refreshAccessToken(refreshRequest: RefreshRequest): OauthResponse {
        return authApiService.refreshAccessToken(refreshRequest).getResponse()
    }

    suspend fun getUserInfo(): GetUserInfoResponse {
        return mainApiService.getUserInfo().getResponse()
    }
}
