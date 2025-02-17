package com.keunsori.data.data.response

import com.google.gson.annotations.SerializedName

data class GetUserInfoResponse (
    val id: String,
    val email: String,
    val name: String,
    val quizStats: QuizStat
):BaseResponse() {
    data class QuizStat (
        val solveCount: Int,
        val lastSolve: String?,
        val details: Details
    ) {
        data class Details (
            @SerializedName("EASY")
            val easy: DetailedStat? = null,
            @SerializedName("MEDIUM")
            val medium: DetailedStat? = null,
            @SerializedName("HARD")
            val hard: DetailedStat? = null,
            @SerializedName("VERYHARD")
            val veryHard: DetailedStat? = null,
        ) {
            data class DetailedStat (
                val solvedCnt : Int,
                val totalCnt : Double,
                val solvedAttemptsStats : AttemptCounts,
                val solveStreak: Int
            ) {
                data class AttemptCounts (
                    @SerializedName("1")
                    val firstAttempt: Int,
                    @SerializedName("2")
                    val secondAttempt: Int,
                    @SerializedName("3")
                    val thirdAttempt: Int,
                    @SerializedName("4")
                    val fourthAttempt: Int,
                    @SerializedName("5")
                    val fifthAttempt: Int,
                    @SerializedName("6")
                    val sixthAttempt: Int,
                    @SerializedName("7")
                    val seventhAttempt: Int,
                )
            }
        }
    }
}
