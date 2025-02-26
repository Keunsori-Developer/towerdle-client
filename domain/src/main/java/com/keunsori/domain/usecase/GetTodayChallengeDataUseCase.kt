package com.keunsori.domain.usecase

import com.keunsori.domain.repository.InGameRepository

class GetTodayChallengeDataUseCase(private val inGameRepository: InGameRepository) {
    suspend operator fun invoke() = inGameRepository.requestTodayChallengeData(System.currentTimeMillis())
}
