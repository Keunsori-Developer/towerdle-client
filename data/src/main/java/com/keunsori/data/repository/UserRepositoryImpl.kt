package com.keunsori.data.repository

import android.util.Log
import com.keunsori.data.datasource.LocalDataSource
import com.keunsori.data.datasource.UserRemoteDataSource
import com.keunsori.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val localDataSource: LocalDataSource,
) : UserRepository {
    override suspend fun test(test: String): String {
        return userRemoteDataSource.example()
    }

    override suspend fun tryLogin(googleIdToken: String): Boolean {
        //TODO: 로그인 API 후 refreshToken 및 accessToken 저장
        Log.d("UserRepositoryImpl","tryLogin")
        localDataSource.setRefreshToken("test_refresh")
        return true
    }

    override suspend fun logout() {
        localDataSource.deleteToken()
    }

    override suspend fun getRefreshToken(): String {
        return localDataSource.getRefreshToken()
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        localDataSource.setRefreshToken(refreshToken = refreshToken)
    }

    override fun setAccessToken(accessToken: String) {
        localDataSource.setAccessToken(accessToken = accessToken)
    }
}