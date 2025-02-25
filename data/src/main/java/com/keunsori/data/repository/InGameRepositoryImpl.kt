package com.keunsori.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.keunsori.data.data.ChallengeModeRawData
import com.keunsori.data.datasource.LocalDataSource
import com.keunsori.data.datasource.MainRemoteDataSource
import com.keunsori.data.retrofit.ServiceException
import com.keunsori.domain.entity.ChallengeModeData
import com.keunsori.domain.entity.QuizInfo
import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.domain.entity.QuizLevel
import com.keunsori.domain.entity.WordDefinition
import com.keunsori.domain.repository.InGameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

internal class InGameRepositoryImpl @Inject constructor(
    private val remoteDataSource: MainRemoteDataSource,
    private val localDataSource: LocalDataSource
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
        //TODO: exception
    }

    /**
     * 사용자가 입력한 값이 존재하는 단어인지 확인합니다.
     *
     */
    override suspend fun isExistWord(input: CharArray): Boolean {
        return try {
            val checkResult = remoteDataSource.checkWord(input.joinToString(""))
            checkResult.value.isNotEmpty()
        } catch (e: Exception) {
            false
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

    /**
     * 오늘 날짜에 해당하는 챌린지 모드 데이터를 가져옵니다.
     *
     */
    override suspend fun requestChallengeData(timestamp: Long): ChallengeModeData {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(timestamp)
        val savedData = localDataSource.getChallengeModeData().first()?.map {
            Gson().fromJson(it, ChallengeModeRawData::class.java)
        }?.sortedBy { it.index }

        try {
            val result = withContext(Dispatchers.IO) {
                remoteDataSource.getQuiz("CHALLENGE")
            }
            val quizInfo = with(result) {
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


            return if (savedData == null) {
                ChallengeModeData.NotStarted(todayDate, quizInfo)
            } else {
                ChallengeModeData.InGoing(
                    savedData.map {
                        QuizInputResult(
                            false,
                            it.input.map { input ->
                                QuizInputResult.Element(
                                    letter = input.letter,
                                    type = QuizInputResult.Type.entries.first { t -> t.ordinal == input.type })
                            }, false
                        )
                    }, date = todayDate, quizInfo = quizInfo
                )
            }
        } catch (e: Exception) {
            if (e is ServiceException && e.code == 400) {
                // 이미 챌린지 끝
                return ChallengeModeData.Finished(savedData?.map {
                    it.input.map { input ->
                        QuizInputResult.Element(
                            letter = input.letter,
                            type = QuizInputResult.Type.entries.first { t -> t.ordinal == input.type })
                    }
                } ?: emptyList(), todayDate)
            }

            return ChallengeModeData.FailedToGetModeData
        }
    }

    /**
     * 챌린지 모드에서 유저가 입력한 답을 기기에 저장합니다.
     *
     * @param input
     */
    override suspend fun saveChallengeData(input: List<QuizInputResult.Element>) {
        TODO("Not yet implemented")
    }
}
