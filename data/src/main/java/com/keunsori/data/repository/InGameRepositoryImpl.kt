package com.keunsori.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.keunsori.data.datasource.MainRemoteDataSource
import com.keunsori.domain.entity.QuizInfo
import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.domain.entity.QuizLevel
import com.keunsori.domain.entity.WordDefinition
import com.keunsori.domain.repository.InGameRepository
import javax.inject.Inject

internal class InGameRepositoryImpl @Inject constructor(
    private val remoteDataSource: MainRemoteDataSource
) : InGameRepository {
    /**
     * 출제할 단어 정보를 가져옵니다.
     */
    override suspend fun requestQuizWord(level: QuizLevel): QuizInfo {
        val result = remoteDataSource.getQuiz(level.name)
        return with(result) {
            QuizInfo(
                uuid = uuid,
                word = word.value,
                length = word.length,
                count = word.count,
                definitions = Gson().fromJson(
                    word.definitions,
                    object : TypeToken<List<WordDefinition>>() {}.type
                ),
                maxAttempts = difficulty.maxAttempts
            )
        }
    }

    /**
     * 사용자가 입력한 정답이 실제 정답과 일치하는지를 확인합니다.
     */
    override fun checkAnswer(input: CharArray, realAnswer: CharArray): QuizInputResult {
        val answerSize = realAnswer.size
        val inputSize = input.size

        // 1. 정답 입력한 자모 갯수 체크
        if (input.size != realAnswer.size) return QuizInputResult(
            isValidWord = false,
            listOf(QuizInputResult.Element.empty),
            false
        )

        // 2. 유효한 단어인지 체크
        //TODO

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

        return QuizInputResult(
            isValidWord = true,
            list = elements.toList(),
            correct = input.contentEquals(realAnswer)
        )
    }

    /**
     * 퀴즈 결과를 서버로 전달합니다.
     *
     */
    override suspend fun sendResult(uuid: String, attemptCount: Int, success: Boolean) {
        remoteDataSource.sendQuizResult(uuid, attemptCount, success)
    }
}
