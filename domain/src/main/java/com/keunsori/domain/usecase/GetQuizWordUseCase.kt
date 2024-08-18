package com.keunsori.domain.usecase

import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.domain.repository.InGameRepository

class GetQuizWordUseCase(private val inGameRepository: InGameRepository) {
    suspend operator fun invoke(): CharArray {
        val word = inGameRepository.requestQuizWord()
        return ParseStringToCharArrayUseCase().invoke(word)
    }
}
