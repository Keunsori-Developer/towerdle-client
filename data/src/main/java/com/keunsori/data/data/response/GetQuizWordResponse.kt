package com.keunsori.data.data.response

data class GetQuizWordResponse(
    val id: String,
    val value: String,
    val length: Int,
    val count: Int,
    val definitions: String
) : BaseResponse()
