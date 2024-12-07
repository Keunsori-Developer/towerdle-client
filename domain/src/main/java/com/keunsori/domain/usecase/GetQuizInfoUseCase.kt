package com.keunsori.domain.usecase

import com.keunsori.domain.entity.QuizInfo
import com.keunsori.domain.entity.QuizOption
import com.keunsori.domain.repository.InGameRepository

class GetQuizInfoUseCase(private val inGameRepository: InGameRepository) {
    suspend operator fun invoke(level: Int): Pair<QuizInfo, CharArray> {
        val quizInfo = inGameRepository.requestQuizWord(QuizOption.getOption(level))
        return quizInfo to ParseStringToCharArrayUseCase().invoke(quizInfo.word)
    }
}
