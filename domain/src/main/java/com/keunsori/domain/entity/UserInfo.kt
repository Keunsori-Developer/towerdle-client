package com.keunsori.domain.entity

data class UserInfo(
    val id: String,
    val email: String,
    val name: String,
    val solveCount: Int,
    val totalCnt: Int,
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
            id = "",
            email = "",
            name = "",
            solveCount = 0,
            totalCnt = 0,
            lastSolve = "",
            detailedStats = mapOf(),
        )
    }
}
