package com.keunsori.data.api

import com.keunsori.data.dto.GetQuizWordResponse
import retrofit2.Call
import retrofit2.http.GET


interface MainApiService {
    @GET("word?count=6&complexVowel=false&complexConsonant=false")
    fun getQuizWord(): Call<GetQuizWordResponse>
}
