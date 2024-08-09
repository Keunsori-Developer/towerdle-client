package com.keunsori.domain.entity

data class UserInfo(
    val userName: String,
    val loginType: LoginType
) {
    enum class LoginType { Google, Guest }
}