package com.keunsori.data.data.response

data class GuestResponse (
    val accessToken: String,
    val refreshToken: String,
    val user: User,
    val isNewUser: Boolean,
    val providerId: String,
) : BaseResponse()