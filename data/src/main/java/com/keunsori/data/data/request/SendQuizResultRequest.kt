package com.keunsori.data.data.request

data class SendQuizResultRequest(
    val wordId: String,
    val attempts: Int,
    val isSolved: Boolean
)
