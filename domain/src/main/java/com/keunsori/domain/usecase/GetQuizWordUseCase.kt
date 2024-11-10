package com.keunsori.domain.usecase

import com.keunsori.domain.entity.QuizOption
import com.keunsori.domain.repository.InGameRepository

class GetQuizWordUseCase(private val inGameRepository: InGameRepository) {
    suspend operator fun invoke(level: Int): Pair<String, CharArray> {
        val word = inGameRepository.requestQuizWord(QuizOption.getOption(level))
        return Pair(word, ParseStringToCharArrayUseCase().invoke(word))
    }
}
