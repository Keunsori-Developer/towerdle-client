package com.keunsori.data.repository

import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.domain.repository.InGameRepository


internal class InGameRepositoryImpl : InGameRepository {
    /**
     * 출제할 단어를 가져옵니다.
     */
    override suspend fun requestQuizWord(): String {
        return "분홍" // TODO: 수정
    }

    /**
     * 사용자가 입력한 정답이 실제 정답과 일치하는지를 확인합니다.
     */
    override fun checkAnswer(input: CharArray, realAnswer: CharArray): QuizInputResult {
        val answerSize = realAnswer.size
        val inputSize = input.size
        if (input.isEmpty()) return QuizInputResult(listOf(QuizInputResult.Element.empty), false)

        val elements = Array(answerSize) { QuizInputResult.Element.empty }

        for (i in 0 until answerSize) {
            if (inputSize <= i) break
            elements[i] = when {
                input[i] == realAnswer[i] -> QuizInputResult.Element(
                    input[i], QuizInputResult.Type.MATCHED
                )

                realAnswer.contains(input[i]) -> QuizInputResult.Element(
                    input[i], QuizInputResult.Type.WRONG_SPOT
                )

                else -> QuizInputResult.Element(input[i], QuizInputResult.Type.NOT_EXIST)
            }
        }

        return QuizInputResult(list = elements.toList(), correct = input.contentEquals(realAnswer))
    }
}