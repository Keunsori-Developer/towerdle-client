package com.keunsori.data.data.response

data class GetQuizWordResponse(
    val uuid: String,
    val word: QuizWordDto,
    val difficulty: QuizDifficultyDto
) : BaseResponse()

data class QuizWordDto(
    val value: String,
    val definitions: String,
    val length: Int,
    val count: Int
)

data class QuizDifficultyDto(
    val lengthMin: Int,
    val lengthMax: Int,
    val countMin: Int,
    val countMax: Int,
    val complexVowel: Boolean,
    val complexConsonant: Boolean,
    val maxAttempts: Int
)
