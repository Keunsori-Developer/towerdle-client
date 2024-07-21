package com.keunsori.towerdle.data.repository

import javax.inject.Inject

class InGameRepository @Inject constructor(

){
    suspend fun requestQuizWord(): String {
        return ""
    }

    fun checkAnswerIsCorrect(input: CharArray, answer: CharArray) : Boolean {
        return input.contentEquals(answer)
    }
}
