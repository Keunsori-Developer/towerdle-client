package com.keunsori.data.repository

import com.keunsori.data.data.request.GuestRequest
import com.keunsori.data.data.request.OauthRequest
import com.keunsori.data.data.request.RefreshRequest
import com.keunsori.data.datasource.LocalDataSource
import com.keunsori.data.datasource.UserRemoteDataSource
import com.keunsori.domain.entity.ApiResult
import com.keunsori.domain.entity.LoginResult
import com.keunsori.domain.entity.User
import com.keunsori.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val localDataSource: LocalDataSource,
) : UserRepository {
    override suspend fun autoGoogleLogin(): ApiResult<LoginResult> {
        return try {
            val res =
                userRemoteDataSource.refreshAccessToken(refreshRequest = RefreshRequest(refreshToken = localDataSource.refreshToken))

            localDataSource.setAccessToken(accessToken = res.accessToken)
            localDataSource.setRefreshToken(refreshToken = res.refreshToken)

            ApiResult.Success(
                data = LoginResult(
                    accessToken = res.accessToken,
                    refreshToken = res.refreshToken,
                    user = User(
                        id = res.user.id,
                        name = res.user.name,
                        email = res.user.email
                    )
                )
            )
        } catch (e: Exception) {
            ApiResult.Fail(
                exception = e
            )
        }
    }

    override suspend fun tryGoogleLogin(googleIdToken: String): ApiResult<LoginResult> {
        return try {
            val res =
                userRemoteDataSource.googleLogin(oauthRequest = OauthRequest(jwt = googleIdToken))

            localDataSource.setAccessToken(accessToken = res.accessToken)
            localDataSource.setRefreshToken(refreshToken = res.refreshToken)
            localDataSource.googleLogin()
            ApiResult.Success(
                data = LoginResult(
                    accessToken = res.accessToken,
                    refreshToken = res.refreshToken,
                    user = User(res.user.id, res.user.name, res.user.email)
                )
            )
        } catch (e: Exception) {
            ApiResult.Fail(
                exception = e
            )
        }
    }

    override suspend fun tryGuestLogin(): ApiResult<LoginResult> {
        return try {
            val res =
                userRemoteDataSource.guestLogin(
                    guestRequest = GuestRequest(
                        guestId = localDataSource.guestIdToken.first() ?: ""
                    )
                )
            if (res.isNewUser) {
                localDataSource.guestLogin(res.providerId)
            }

            localDataSource.setAccessToken(accessToken = res.accessToken)
            localDataSource.setRefreshToken(refreshToken = res.refreshToken)
            ApiResult.Success(
                data = LoginResult(
                    accessToken = res.accessToken,
                    refreshToken = res.refreshToken,
                    user = User(res.user.id, res.user.name, res.user.email)
                )
            )
        } catch (e: Exception) {
            ApiResult.Fail(
                exception = e
            )
        }
    }

    override suspend fun initRefreshToken() {
        localDataSource.init()
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

    override suspend fun setRefreshToken(refreshToken: String) {
        localDataSource.setRefreshToken(refreshToken = refreshToken)
    }

    override fun setAccessToken(accessToken: String) {
        localDataSource.setAccessToken(accessToken = accessToken)
    }

    override fun getIsGoogleLoggedIn(): Flow<Boolean?> {
        return localDataSource.isGoogleLoggedIn
    }

    override suspend fun logout() {
        localDataSource.deleteToken()
        tryGuestLogin()
    }
}