package com.keunsori.domain.usecase

import com.keunsori.domain.entity.UserInfo
import com.keunsori.domain.repository.UserRepository

class GetUserInfoUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): UserInfo {
        return userRepository.getUserInfo()
    }
}
