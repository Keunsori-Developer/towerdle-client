package com.keunsori.data.datasource

import com.keunsori.data.api.MainApiService
import com.keunsori.data.data.request.GetQuizWordRequest
import com.keunsori.data.data.request.SendQuizResultRequest
import com.keunsori.data.data.response.CheckWordResponse
import com.keunsori.data.data.response.GetQuizWordResponse
import com.keunsori.data.retrofit.getResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRemoteDataSource @Inject constructor(
    private val mainApiService: MainApiService
) {
    suspend fun getQuiz(level: String): GetQuizWordResponse {
        return mainApiService.getQuizWord(GetQuizWordRequest(level)).getResponse()
    }

    suspend fun checkWord(word: String): CheckWordResponse {
        return mainApiService.checkWord(word).getResponse()
    }

    suspend fun sendQuizResult(uuid: String, attemptCount: Int, success: Boolean): Boolean {
        val request =
            SendQuizResultRequest(attempts = attemptCount, solved = success)
        return mainApiService.sendQuizResult(uuid = uuid, request)
            .getResponse().statusCode in 200..299
    }
}

