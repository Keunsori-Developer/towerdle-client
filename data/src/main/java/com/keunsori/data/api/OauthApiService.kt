package com.keunsori.data.api

import com.keunsori.data.data.request.OauthRequest
import com.keunsori.data.data.request.RefreshRequest
import com.keunsori.data.data.response.OauthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OauthApiService {
    @POST("/auth/login/app/google")
    fun googleLogin(@Body oauthRequest: OauthRequest): Call<OauthResponse>

    @POST("/auth/refresh")
    fun refreshAccessToken(@Body refreshRequest: RefreshRequest): Call<OauthResponse>
}