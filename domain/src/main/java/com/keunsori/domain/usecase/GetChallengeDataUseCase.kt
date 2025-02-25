package com.keunsori.domain.usecase

import com.keunsori.domain.repository.InGameRepository

class GetChallengeDataUseCase(private val inGameRepository: InGameRepository) {
    suspend operator fun invoke() = inGameRepository.requestChallengeData(System.currentTimeMillis())
}
