package com.keunsori.data.data.response

data class CheckWordResponse(
    val id: String,
    val value: String,
    val definitions: String
) : BaseResponse()
