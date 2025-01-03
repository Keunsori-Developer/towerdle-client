package com.keunsori.domain.usecase

import com.keunsori.domain.entity.LoginResult
import com.keunsori.domain.entity.ApiResult
import com.keunsori.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class UserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
    ) {
        userRepository.initRefreshToken()
    }

    suspend fun autoGoogleLogin(): ApiResult<LoginResult> {
        return userRepository.autoGoogleLogin()
    }

    suspend fun tryGoogleLogin(googleIdToken: String): ApiResult<LoginResult> {
        return userRepository.tryGoogleLogin(googleIdToken = googleIdToken)
    }

    suspend fun tryGuestLogin(): ApiResult<LoginResult> {
        return userRepository.tryGuestLogin()
    }

    fun getIsGoogleLoggedIn(): Flow<Boolean?> {
        return userRepository.getIsGoogleLoggedIn()
    }

    suspend fun logout() {
        userRepository.logout()
    }
}