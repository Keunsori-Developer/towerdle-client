package com.keunsori.domain.entity

data class LoginResult (
    val accessToken: String,
    val refreshToken: String
)