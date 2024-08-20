package com.keunsori.data.repository

import com.keunsori.data.data.request.OauthRequest
import com.keunsori.data.data.request.RefreshRequest
import com.keunsori.data.datasource.LocalDataSource
import com.keunsori.data.datasource.UserRemoteDataSource
import com.keunsori.domain.entity.ApiResult
import com.keunsori.domain.entity.LoginResult
import com.keunsori.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val localDataSource: LocalDataSource,
) : UserRepository {
    override suspend fun tryLogin(googleIdToken: String): ApiResult<LoginResult> {
        return try {
            val res =
                userRemoteDataSource.googleLogin(oauthRequest = OauthRequest(jwt = googleIdToken))

            localDataSource.setAccessToken(accessToken = res.accessToken)
            localDataSource.setRefreshToken(refreshToken = res.refreshToken)
            ApiResult.Success(
                data = LoginResult(
                    accessToken = res.accessToken, refreshToken = res.refreshToken
                )
            )
        } catch (e: Exception) {
            ApiResult.Fail(
                exception = e
            )
        }
    }

    override suspend fun initRefreshToken() {
        localDataSource.initRefreshToken()
    }

    override suspend fun refreshAccessToken(): Boolean {
        val refreshToken = localDataSource.refreshToken
        return try {
            if (refreshToken.isNotEmpty()) {
                val res =
                    userRemoteDataSource.refreshAccessToken(
                        refreshRequest = RefreshRequest(
                            refreshToken = refreshToken
                        )
                    )
                localDataSource.setAccessToken(accessToken = res.accessToken)
                localDataSource.setRefreshToken(refreshToken = res.refreshToken)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getRefreshToken(): String {
        return localDataSource.refreshToken
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        localDataSource.setRefreshToken(refreshToken = refreshToken)
    }

    override fun setAccessToken(accessToken: String) {
        localDataSource.setAccessToken(accessToken = accessToken)
    }

    override fun setLoginType(isGuest: Boolean) {
        localDataSource.setLoginType(isGuest = isGuest)
    }

    override suspend fun logout() {
        localDataSource.deleteToken()
    }
}