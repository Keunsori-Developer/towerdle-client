package com.keunsori.data.data.response

data class OauthResponse(
    val accessToken: String,
    val refreshToken: String
) : BaseResponse()