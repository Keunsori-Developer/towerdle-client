package com.keunsori.data.repository

import com.keunsori.data.data.request.GuestRequest
import com.keunsori.data.data.request.OauthRequest
import com.keunsori.data.data.request.RefreshRequest
import com.keunsori.data.datasource.LocalDataSource
import com.keunsori.data.datasource.UserRemoteDataSource
import com.keunsori.domain.entity.ApiResult
import com.keunsori.domain.entity.LoginResult
import com.keunsori.domain.entity.QuizLevel
import com.keunsori.domain.entity.User
import com.keunsori.domain.entity.UserInfo
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
                    user = User(id = res.user.id, name = res.user.name, email = res.user.email)
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
                    user = User(id = res.user.id, name = res.user.name, email = res.user.email)
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

    override suspend fun getUserInfo(): UserInfo {
        val userInfo = userRemoteDataSource.getUserInfo()
        return UserInfo(
            solveCount = userInfo.quizStats.solveCount,
            lastSolve = userInfo.quizStats.lastSolve ?: "",
            detailedStats = mapOf(
                QuizLevel.EASY to if (userInfo.quizStats.details.easy == null) UserInfo.DetailedStats() else UserInfo.DetailedStats(
                    solvedCnt = userInfo.quizStats.details.easy.solvedCnt,
                    totalCnt = userInfo.quizStats.details.easy.totalCnt,
                    solvedAttemptsStats = listOf(
                        userInfo.quizStats.details.easy.solvedAttemptsStats.firstAttempt,
                        userInfo.quizStats.details.easy.solvedAttemptsStats.secondAttempt,
                        userInfo.quizStats.details.easy.solvedAttemptsStats.thirdAttempt,
                        userInfo.quizStats.details.easy.solvedAttemptsStats.fourthAttempt,
                        userInfo.quizStats.details.easy.solvedAttemptsStats.fifthAttempt,
                        userInfo.quizStats.details.easy.solvedAttemptsStats.sixthAttempt,
                        userInfo.quizStats.details.easy.solvedAttemptsStats.seventhAttempt
                    ),
                    solveStreak = userInfo.quizStats.details.easy.solveStreak
                ),
                QuizLevel.MEDIUM to if (userInfo.quizStats.details.medium == null) UserInfo.DetailedStats() else UserInfo.DetailedStats(
                    solvedCnt = userInfo.quizStats.details.medium.solvedCnt,
                    totalCnt = userInfo.quizStats.details.medium.totalCnt,
                    solvedAttemptsStats = listOf(
                        userInfo.quizStats.details.medium.solvedAttemptsStats.firstAttempt,
                        userInfo.quizStats.details.medium.solvedAttemptsStats.secondAttempt,
                        userInfo.quizStats.details.medium.solvedAttemptsStats.thirdAttempt,
                        userInfo.quizStats.details.medium.solvedAttemptsStats.fourthAttempt,
                        userInfo.quizStats.details.medium.solvedAttemptsStats.fifthAttempt,
                        userInfo.quizStats.details.medium.solvedAttemptsStats.sixthAttempt
                    ),
                    solveStreak = userInfo.quizStats.details.medium.solveStreak
                ),
                QuizLevel.HARD to if (userInfo.quizStats.details.hard == null) UserInfo.DetailedStats() else UserInfo.DetailedStats(
                    solvedCnt = userInfo.quizStats.details.hard.solvedCnt,
                    totalCnt = userInfo.quizStats.details.hard.totalCnt,
                    solvedAttemptsStats = listOf(
                        userInfo.quizStats.details.hard.solvedAttemptsStats.firstAttempt,
                        userInfo.quizStats.details.hard.solvedAttemptsStats.secondAttempt,
                        userInfo.quizStats.details.hard.solvedAttemptsStats.thirdAttempt,
                        userInfo.quizStats.details.hard.solvedAttemptsStats.fourthAttempt,
                        userInfo.quizStats.details.hard.solvedAttemptsStats.fifthAttempt,
                        userInfo.quizStats.details.hard.solvedAttemptsStats.sixthAttempt
                    ),
                    solveStreak = userInfo.quizStats.details.hard.solveStreak
                ),
                QuizLevel.VERYHARD to if (userInfo.quizStats.details.veryHard == null) UserInfo.DetailedStats() else UserInfo.DetailedStats(
                    solvedCnt = userInfo.quizStats.details.veryHard.solvedCnt,
                    totalCnt = userInfo.quizStats.details.veryHard.totalCnt,
                    solvedAttemptsStats = listOf(
                        userInfo.quizStats.details.veryHard.solvedAttemptsStats.firstAttempt,
                        userInfo.quizStats.details.veryHard.solvedAttemptsStats.secondAttempt,
                        userInfo.quizStats.details.veryHard.solvedAttemptsStats.thirdAttempt,
                        userInfo.quizStats.details.veryHard.solvedAttemptsStats.fourthAttempt,
                        userInfo.quizStats.details.veryHard.solvedAttemptsStats.fifthAttempt,
                        userInfo.quizStats.details.veryHard.solvedAttemptsStats.sixthAttempt
                    ),
                    solveStreak = userInfo.quizStats.details.veryHard.solveStreak
                )
            )
        )
    }

    override suspend fun logout() {
        localDataSource.deleteToken()
    }
}