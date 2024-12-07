package com.keunsori.data.api

import com.keunsori.data.data.request.SendQuizResultRequest
import com.keunsori.data.data.response.CheckWordResponse
import com.keunsori.data.data.response.GetQuizWordResponse
import com.keunsori.data.data.response.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface MainApiService {
    @GET("word")
    fun getQuizWord(
        @Query("length") length: Int,
        @Query("count") count: Int,
        @Query("complexVowel") complexVowel: Boolean,
        @Query("complexConsonant") complexConsonant: Boolean,
    ): Call<GetQuizWordResponse>

    @GET("word/check/{word}")
    fun checkWord(@Path("word") word: String): Call<CheckWordResponse>

    @POST("word/solve")
    fun sendQuizResult(@Body request: SendQuizResultRequest): Call<Response>
}
