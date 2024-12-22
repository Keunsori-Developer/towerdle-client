package com.keunsori.domain.entity

data class QuizInfo(
    val uuid: String,
    val word: String,
    val length: Int, // 음절 수
    val count: Int, // 자모음의 총 갯수
    val definitions: List<WordDefinition>,
    val maxAttempts: Int
)

data class WordDefinition(
    val pos: String,
    val meanings: List<String>
)
