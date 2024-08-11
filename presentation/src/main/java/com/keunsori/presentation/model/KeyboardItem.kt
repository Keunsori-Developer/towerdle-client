package com.keunsori.presentation.model

import androidx.compose.ui.graphics.Color

sealed interface KeyboardItem {
    data class Letter(val letter: Char, val matchType: LetterMatchType) : KeyboardItem
    data object Enter : KeyboardItem
    data object Backspace : KeyboardItem
}

enum class LetterMatchType(val color: Color) {
    MATCHED(Color.Green), WRONG_SPOT(Color.Yellow), NOT_EXIST(Color.DarkGray), NONE(Color.Gray)
}
