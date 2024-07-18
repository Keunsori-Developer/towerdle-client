package com.keunsori.data

import com.keunsori.data.datasource.LocalDataSource
import com.keunsori.data.datasource.MainRemoteDataSource
import com.keunsori.data.datasource.UserRemoteDataSource
import com.keunsori.data.service.GoogleLoginService
import com.keunsori.domain.repository.MainRepository
import com.keunsori.domain.repository.UserRepository
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

@ActivityScoped
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val localDataSource: LocalDataSource,
) : UserRepository {
    override suspend fun test(test: String): String {
        return userRemoteDataSource.example()
    }

    override suspend fun tryLogin(clientId: String): Boolean {
        val googleIdToken = userRemoteDataSource.tryLogin(clientId)
        //TODO: 로그인 API 후 refreshToken 및 accessToken 저장
        localDataSource.setRefreshToken("test_refresh")
        return googleIdToken?.isNotEmpty() ?: false
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