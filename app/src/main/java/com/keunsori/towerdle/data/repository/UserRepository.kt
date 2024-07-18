package com.keunsori.towerdle.data.repository

import com.keunsori.towerdle.data.datasource.LocalDataSource
import com.keunsori.towerdle.data.datasource.UserRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    suspend fun verifyRefreshToken(): Boolean{
        val refreshToken = localDataSource.getRefreshToken()
        return if(refreshToken.isNotEmpty()){
            if(true){ //TODO: 검증 API 후 성공하면 accessToken 저장
                localDataSource.setAccessToken("test_access")
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    suspend fun tryLogin(googleIdToken: String): Boolean{
        //TODO: 로그인 API 후 refreshToken 및 accessToken 저장
        localDataSource.setRefreshToken("test_refresh")
        return googleIdToken.isNotEmpty()
    }

    suspend fun logout(){
        localDataSource.deleteToken()
    }
}