package com.keunsori.data.api

import com.keunsori.data.data.response.GetQuizWordResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface MainApiService {
    @GET("word")
    fun getQuizWord(
        @Query("length") length: Int,
        @Query("count") count: Int,
        @Query("complexVowel") complexVowel: Boolean,
        @Query("complexConsonant") complexConsonant: Boolean,
    ): Call<GetQuizWordResponse>
}
