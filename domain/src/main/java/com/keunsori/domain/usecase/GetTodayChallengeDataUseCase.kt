package com.keunsori.domain.usecase

import com.keunsori.domain.repository.InGameRepository

class GetTodayChallengeDataUseCase(private val inGameRepository: InGameRepository) {
    suspend operator fun invoke(date: String) = inGameRepository.requestTodayChallengeData(date)
}
