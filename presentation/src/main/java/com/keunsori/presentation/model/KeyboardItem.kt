package com.keunsori.presentation.model

import com.keunsori.presentation.ui.theme.Color

sealed interface KeyboardItem {
    data class Letter(val letter: Char, val matchType: LetterMatchType) : KeyboardItem
    data object Enter : KeyboardItem
    data object Backspace : KeyboardItem
    data object Empty : KeyboardItem
}

enum class LetterMatchType(val color: androidx.compose.ui.graphics.Color?) {
    MATCHED(Color.ingameMatched), WRONG_SPOT(Color.ingameWrongSpot), NOT_EXIST(Color.ingameNotExist),
    NONE(null)
}
