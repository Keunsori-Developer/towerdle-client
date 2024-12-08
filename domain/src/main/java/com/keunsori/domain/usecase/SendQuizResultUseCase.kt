package com.keunsori.domain.usecase

import com.keunsori.domain.repository.InGameRepository

class SendQuizResultUseCase(private val inGameRepository: InGameRepository) {
    suspend fun invoke(quizId: String, attemptCount: Int, success: Boolean) {
        inGameRepository.sendResult(quizId, attemptCount, success)
    }
}
