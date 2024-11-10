package com.keunsori.domain.entity

import kotlin.random.Random

/**
 * @property length 단어의 길이
 * @property count 자모음의 총 갯수
 * @property complexVowel 복합모음의 유무
 * @property complexConsonant 복합자음의 유무
 */
data class QuizOption(
    val length: Int,
    val count: Int,
    val complexVowel: Boolean,
    val complexConsonant: Boolean
) {
    companion object {
        fun getOption(level: Int): QuizOption {
            return when (level) {
                1 -> QuizOption(2, 6, false, false)
                2 -> {
                    val complexVowel = Random.nextInt(3) % 2 == 1
                    val complexConsonant = !complexVowel
                    QuizOption(2, 6, complexVowel, complexConsonant)
                }

                3 -> {
                    val count = Random.nextInt(8, 10)
                    QuizOption(3, count, false, false)
                }

                4 -> {
                    val count = Random.nextInt(8, 10)
                    val complexVowel = Random.nextInt(3) % 2 == 1
                    val complexConsonant = !complexVowel
                    QuizOption(3, count, complexVowel, complexConsonant)
                }

                else -> QuizOption(2, 6, false, false)
            }
        }
    }
}