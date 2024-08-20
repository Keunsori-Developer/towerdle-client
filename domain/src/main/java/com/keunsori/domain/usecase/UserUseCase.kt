package com.keunsori.domain.usecase

import com.keunsori.domain.entity.LoginResult
import com.keunsori.domain.entity.ApiResult
import com.keunsori.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        onSuccess: suspend () -> Unit
    ) {
        userRepository.initRefreshToken()
        if (userRepository.refreshAccessToken()) {
            onSuccess.invoke()
        }
    }

    suspend fun tryLogin(googleIdToken: String): ApiResult<LoginResult> {
        return userRepository.tryLogin(googleIdToken = googleIdToken)
    }

    fun setLoginType(isGuest: Boolean){
        userRepository.setLoginType(isGuest = isGuest)
    }

    suspend fun logout() {
        userRepository.logout()
    }
}