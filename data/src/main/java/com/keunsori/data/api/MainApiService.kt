package com.keunsori.data.api

import com.keunsori.data.data.request.GetQuizWordRequest
import com.keunsori.data.data.request.SendQuizResultRequest
import com.keunsori.data.data.response.CheckWordResponse
import com.keunsori.data.data.response.GetQuizWordResponse
import com.keunsori.data.data.response.SendQuizResultResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface MainApiService {
    @POST("quiz")
    fun getQuizWord(@Body request: GetQuizWordRequest): Call<GetQuizWordResponse>

    @GET("word/{value}")
    fun checkWord(@Path("value") word: String): Call<CheckWordResponse>

    @POST("quiz/{uuid}")
    fun sendQuizResult(
        @Path("uuid") uuid: String,
        @Body request: SendQuizResultRequest
    ): Call<SendQuizResultResponse>
}
