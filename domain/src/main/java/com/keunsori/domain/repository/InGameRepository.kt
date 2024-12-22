package com.keunsori.domain.repository

import com.keunsori.domain.entity.QuizInfo
import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.domain.entity.QuizLevel

interface InGameRepository {
    /**
     * 출제할 단어 정보를 가져옵니다.
     */
    suspend fun requestQuizWord(level: QuizLevel): QuizInfo

    /**
     * 사용자가 입력한 정답이 실제 정답과 일치하는지를 확인합니다.
     */
    fun checkAnswer(input: CharArray, realAnswer: CharArray): QuizInputResult

    /**
     * 퀴즈 결과를 서버로 전달합니다.
     *
     */
    suspend fun sendResult(uuid: String, attemptCount: Int, success: Boolean)
}
