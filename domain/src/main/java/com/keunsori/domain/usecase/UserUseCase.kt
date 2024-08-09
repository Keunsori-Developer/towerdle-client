package com.keunsori.domain.usecase

import com.keunsori.domain.entity.LoginResult
import com.keunsori.domain.entity.ApiResult
import com.keunsori.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(
        test: String,
        scope: CoroutineScope,
        onResult: (String) -> Unit = {}
    ) {
        scope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) {
                test
            }
            onResult(deferred.await())
        }
    }

    suspend fun verifyRefreshToken(): Boolean {
        return userRepository.refreshAccessToken()
    }

    suspend fun tryLogin(googleIdToken: String): ApiResult<LoginResult> {
        return userRepository.tryLogin(googleIdToken = googleIdToken)
    }

    suspend fun logout() {
        userRepository.logout()
    }
}