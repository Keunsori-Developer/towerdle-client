package com.keunsori.presentation.intent

import com.keunsori.presentation.utils.UiEvent

sealed interface InGameEvent : UiEvent {
    data class SelectLetter(val letter: Char) : InGameEvent
    data object ClickEnterButton : InGameEvent
    data object ClickBackspaceButton : InGameEvent
}


data class InGameUiState(
    val quizSize: Int,
    val currentTrialCount: Int,
    val maxTrialSize: Int,
    val userInputs: List<UserInput>,
    val keyboardItems: List<List<KeyboardItem>>,
    val isGameFinished: Boolean
) : UiState {
    companion object {
        fun init(): InGameUiState {
            val lastLine = mutableListOf<KeyboardItem>()
            lastLine.add(KeyboardItem.Enter)
            lastLine.addAll(
                charArrayOf(
                    'ㅋ', 'ㅌ', 'ㅊ', 'ㅍ', 'ㅠ', 'ㅜ', 'ㅡ'
                ).map { KeyboardItem.Letter(it, LetterMatchType.NONE) }.toTypedArray()
            )
            lastLine.add(KeyboardItem.Backspace)

            val letters: List<List<KeyboardItem>> = listOf(
                charArrayOf('ㅃ', 'ㅉ', 'ㄸ', 'ㄲ', 'ㅆ', ' ', ' ', ' ', 'ㅒ', 'ㅖ').map {
                    if (it.isWhitespace()) KeyboardItem.Empty
                    else KeyboardItem.Letter(it, LetterMatchType.NONE)
                },
                charArrayOf('ㅂ', 'ㅈ', 'ㄷ', 'ㄱ', 'ㅅ', 'ㅛ', 'ㅕ', 'ㅑ', 'ㅐ', 'ㅔ').map {
                    KeyboardItem.Letter(it, LetterMatchType.NONE)
                },
                charArrayOf(
                    'ㅁ', 'ㄴ', 'ㅇ', 'ㄹ', 'ㅎ', 'ㅗ', 'ㅓ', 'ㅏ', 'ㅣ'
                ).map { KeyboardItem.Letter(it, LetterMatchType.NONE) },
                lastLine
            )
            return InGameUiState(6, 0, 6, emptyList(), letters, false)
        }
    }
}