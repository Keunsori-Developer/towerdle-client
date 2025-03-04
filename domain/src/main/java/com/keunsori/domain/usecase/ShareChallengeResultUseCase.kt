package com.keunsori.domain.usecase

import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.domain.repository.InGameRepository

class ShareChallengeResultUseCase(private val inGameRepository: InGameRepository) {
    suspend fun invoke(date: String, quizInputResult: List<List<QuizInputResult.Element>>): String {
        return inGameRepository.getStringForShareChallengeResult(date, quizInputResult)
    }
}
