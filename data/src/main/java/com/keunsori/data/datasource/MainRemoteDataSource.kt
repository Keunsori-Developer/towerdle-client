package com.keunsori.data.datasource


import com.keunsori.data.api.MainApiService
import com.keunsori.data.data.response.GetQuizWordResponse
import com.keunsori.data.retrofit.getResponse
import com.keunsori.domain.entity.QuizOption
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRemoteDataSource @Inject constructor(
    private val mainApiService: MainApiService
) {
    suspend fun getQuiz(option: QuizOption): GetQuizWordResponse {
        return mainApiService.getQuizWord(
            option.length,
            option.count,
            option.complexVowel,
            option.complexConsonant
        ).getResponse()
    }
}

