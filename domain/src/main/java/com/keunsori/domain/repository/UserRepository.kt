package com.keunsori.domain.repository

interface UserRepository {
    suspend fun test(test: String): String

    suspend fun tryLogin(googleIdToken: String): Boolean

    suspend fun logout()

    suspend fun getRefreshToken(): String

    suspend fun setRefreshToken(refreshToken: String)

    fun setAccessToken(accessToken: String)


}