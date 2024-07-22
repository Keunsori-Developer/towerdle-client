package com.keunsori.towerdle.data.repository

import com.keunsori.towerdle.domain.entity.QuizInputResult
import javax.inject.Inject

class InGameRepository @Inject constructor(

) {
    suspend fun requestQuizWord(): String {
        return ""
    }

    fun checkAnswer(input: CharArray, answer: CharArray): QuizInputResult {
        val answerSize = answer.size
        val inputSize = input.size
        if (input.isEmpty()) return QuizInputResult(listOf(QuizInputResult.Element.empty), false)

        val elements = Array(answerSize) { QuizInputResult.Element.empty }

        for (i in 0 until answerSize) {
            if (inputSize <= i) break
            elements[i] = when {
                input[i] == answer[i] -> QuizInputResult.Element(
                    input[i],
                    QuizInputResult.Type.MATCHED
                )

                answer.contains(input[i]) -> QuizInputResult.Element(
                    input[i],
                    QuizInputResult.Type.WRONG_SPOT
                )

                else -> QuizInputResult.Element(input[i], QuizInputResult.Type.NOT_EXIST)
            }
        }

        return QuizInputResult(list = elements.toList(), correct = input.contentEquals(answer))
    }
}
