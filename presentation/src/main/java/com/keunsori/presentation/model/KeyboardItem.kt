package com.keunsori.presentation.model

data class KeyboardItem(val type: KeyboardItemType) {

}

sealed interface KeyboardItemType {
    data class Letter(val letter: Char) : KeyboardItemType
    data object Enter : KeyboardItemType
    data object Backspace : KeyboardItemType
}