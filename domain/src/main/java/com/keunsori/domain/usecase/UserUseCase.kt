package com.keunsori.domain.usecase

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
                userRepository.test(test)
            }
            onResult(deferred.await())
        }
    }

    suspend fun test(test: String): String {
        return userRepository.test(test)
    }

    suspend fun verifyRefreshToken(): Boolean{
        val refreshToken = userRepository.getRefreshToken()
        return if(refreshToken.isNotEmpty()){
            if(true){ //TODO: 검증 API 후 성공하면 accessToken 저장
                userRepository.setAccessToken("test_access")
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    suspend fun tryLogin(googleIdToken: String): Boolean{
        return userRepository.tryLogin(googleIdToken = googleIdToken)
    }

    suspend fun logout(){
        userRepository.logout()
    }
}