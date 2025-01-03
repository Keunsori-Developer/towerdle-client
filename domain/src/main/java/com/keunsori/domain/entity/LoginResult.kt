package com.keunsori.domain.entity

data class LoginResult (
    val accessToken: String,
    val refreshToken: String,
    val user: User,
    val isNewUser: Boolean = false,
    val providerId: String = ""
)