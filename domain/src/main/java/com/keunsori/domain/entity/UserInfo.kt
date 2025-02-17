package com.keunsori.domain.entity

data class UserInfo(
    val solveCount: Int,
    val lastSolve: String,
    val detailedStats: Map<QuizLevel, DetailedStats>
) {
    data class DetailedStats(
        val solvedCnt: Int = 0,
        val totalCnt: Double = 0.0,
        val solvedAttemptsStats: List<Int> = listOf(0,0,0,0,0,0,0),
        val solveStreak: Int = 0,
    )

    companion object {
        val Empty = UserInfo(
            solveCount = 0,
            lastSolve = "",
            detailedStats = mapOf(),
        )
    }
}
