package com.keunsori.data.api

import com.keunsori.data.dto.GetQuizWordRequest
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {
    @GET("word?count=6&complexVowel=true&complexConsonant=false")
    fun getQuizWord(): Call<GetQuizWordRequest>
}