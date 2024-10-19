package com.keunsori.domain.usecase

import com.keunsori.domain.entity.LoginResult
import com.keunsori.domain.entity.ApiResult
import com.keunsori.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        onSuccess: suspend () -> Unit
    ) {
        userRepository.initRefreshToken()

        if (userRepository.refreshAccessToken()) {
            onSuccess.invoke()
        }
    }

    suspend fun tryGoogleLogin(googleIdToken: String): ApiResult<LoginResult> {
        return userRepository.tryGoogleLogin(googleIdToken = googleIdToken)
    }

    suspend fun tryGuestLogin(guestId: String): ApiResult<LoginResult> {
        return userRepository.tryGuestLogin(guestId = guestId)
    }

    suspend fun logout() {
        userRepository.logout()
    }
}