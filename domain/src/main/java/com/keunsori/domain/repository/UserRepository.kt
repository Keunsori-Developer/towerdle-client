package com.keunsori.domain.repository

import com.keunsori.domain.entity.LoginResult
import com.keunsori.domain.entity.ApiResult

interface UserRepository {
    suspend fun tryGoogleLogin(googleIdToken: String): ApiResult<LoginResult>

    suspend fun tryGuestLogin(guestId: String): ApiResult<LoginResult>

    suspend fun initRefreshToken()

    suspend fun refreshAccessToken(): Boolean

    suspend fun getRefreshToken(): String

    suspend fun setRefreshToken(refreshToken: String)

    fun setAccessToken(accessToken: String)

    suspend fun logout()
}