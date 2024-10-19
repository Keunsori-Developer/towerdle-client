package com.keunsori.data.data.response


data class OauthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: User
) : BaseResponse()


data class User(
    val id: String,
    val email: String,
    val name: String,
)