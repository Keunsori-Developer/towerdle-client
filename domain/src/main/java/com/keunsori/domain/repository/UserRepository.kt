package com.keunsori.domain.repository

import com.keunsori.domain.entity.LoginResult
import com.keunsori.domain.entity.ApiResult
import com.keunsori.domain.entity.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun autoGoogleLogin(): ApiResult<LoginResult>

    suspend fun tryGoogleLogin(googleIdToken: String ): ApiResult<LoginResult>

    suspend fun tryGuestLogin(): ApiResult<LoginResult>

    suspend fun initRefreshToken()

    suspend fun refreshAccessToken(): Boolean

    suspend fun setRefreshToken(refreshToken: String)

    fun setAccessToken(accessToken: String)

    suspend fun logout()

    fun getIsGoogleLoggedIn(): Flow<Boolean?>

    suspend fun getUserInfo(): ApiResult<UserInfo>
}