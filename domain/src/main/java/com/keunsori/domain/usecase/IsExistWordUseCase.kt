package com.keunsori.domain.usecase

import com.keunsori.domain.repository.InGameRepository

class IsExistWordUseCase(private val inGameRepository: InGameRepository) {
    suspend operator fun invoke(userAnswer: CharArray): Boolean {
        return inGameRepository.isExistWord(userAnswer)
    }
}
