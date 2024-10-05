package com.keunsori.domain.usecase

import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.domain.repository.InGameRepository

class CheckAnswerUseCase(private val inGameRepository: InGameRepository) {
    operator fun invoke(userAnswer: CharArray, realAnswer: CharArray) : QuizInputResult {
        return inGameRepository.checkAnswer(userAnswer, realAnswer)
    }
}
