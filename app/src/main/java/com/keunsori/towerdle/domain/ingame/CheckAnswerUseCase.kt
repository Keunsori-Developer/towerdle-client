package com.keunsori.towerdle.domain.ingame

import com.keunsori.towerdle.data.repository.InGameRepository

class CheckAnswerUseCase(private val inGameRepository: InGameRepository) {
    operator fun invoke(input: CharArray, answer: CharArray): Boolean {
        return inGameRepository.checkAnswerIsCorrect(input, answer)
    }
}
