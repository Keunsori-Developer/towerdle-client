package com.keunsori.towerdle.data.repository

import android.util.Log
import com.keunsori.towerdle.data.datasource.LocalDataSource
import com.keunsori.towerdle.data.datasource.UserRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    suspend fun getRefreshToken(): String {
        return localDataSource.getRefreshToken()
    }

    suspend fun verifyAccessToken(refreshToken: String): Boolean{
        localDataSource.setAccessToken("test_access")
        return true //TODO: 검증 API 후 accessToken 저장
    }

    suspend fun tryLogin(googleIdToken: String): Boolean{
        localDataSource.setRefreshToken("test_refresh")
        return googleIdToken.isNotEmpty() //TODO: 로그인 API 후 refreshToken 및 accessToken 저장
    }

    suspend fun logout(){
        localDataSource.deleteToken()
    }
}