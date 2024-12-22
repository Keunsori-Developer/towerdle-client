package com.keunsori.domain.usecase

import com.keunsori.domain.entity.QuizInfo
import com.keunsori.domain.entity.QuizLevel
import com.keunsori.domain.repository.InGameRepository

class GetQuizInfoUseCase(private val inGameRepository: InGameRepository) {
    suspend operator fun invoke(level: QuizLevel): Pair<QuizInfo, CharArray> {
        val quizInfo = inGameRepository.requestQuizWord(level)
        return quizInfo to ParseStringToCharArrayUseCase().invoke(quizInfo.word)
    }
}
