package com.keunsori.towerdle.domain.usecase

import com.keunsori.towerdle.data.repository.InGameRepository
import com.keunsori.towerdle.domain.entity.QuizInputResult

class CheckAnswerUseCase(private val inGameRepository: InGameRepository) {
    operator fun invoke(input: CharArray, answer: CharArray): QuizInputResult {
        return inGameRepository.checkAnswer(input, answer)
    }
}
