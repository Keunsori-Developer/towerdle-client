package com.keunsori.domain.repository

import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.domain.entity.QuizOption

interface InGameRepository {
    /**
     * 출제할 단어를 가져옵니다.
     */
    suspend fun requestQuizWord(option: QuizOption): String

    /**
     * 사용자가 입력한 정답이 실제 정답과 일치하는지를 확인합니다.
     */
    fun checkAnswer(input: CharArray, realAnswer: CharArray): QuizInputResult
}