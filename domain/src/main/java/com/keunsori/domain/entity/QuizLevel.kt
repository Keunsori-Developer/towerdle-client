package com.keunsori.domain.entity

/**
 * EASY :      2~3글자, 4~6개 자모음, 복합자모음 없음, 최대 6번 시도
 *
 * MEDIUM :    2~3글자, 6~9개 자모음, 복합자모음 랜덤, 최대 6번 시도
 *
 * HARD :      3~3글자, 7~11개 자모음, 복합자모음 랜덤, 최대 6번 시도
 *
 * VERYHARD :  3~4글자, 8~16개 자모음, 복합자모음 랜덤, 최대 6번 시도
 *
 */
enum class QuizLevel(val title: String) {
    EASY("쉬움"),
    MEDIUM("보통"),
    HARD("어려움"),
    VERYHARD("매우어려움")
}
