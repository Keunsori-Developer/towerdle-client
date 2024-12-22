package com.keunsori.domain.usecase

import com.keunsori.domain.repository.InGameRepository

class SendQuizResultUseCase(private val inGameRepository: InGameRepository) {
    suspend fun invoke(uuid: String, attemptCount: Int, success: Boolean) {
        inGameRepository.sendResult(uuid, attemptCount, success)
    }
}
