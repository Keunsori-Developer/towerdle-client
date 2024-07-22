package com.keunsori.towerdle.domain.entity

data class QuizInputResult(
    val list: List<Element>,
    val correct: Boolean,
) {
    data class Element(val letter: Char, val type: Type) {
        companion object {
            val empty = Element(' ', Type.NOT_EXIST)
        }
    }
    enum class Type {
        MATCHED, WRONG_SPOT, NOT_EXIST,
    }
}

