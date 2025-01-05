package com.keunsori.presentation.intent

import com.keunsori.presentation.model.KeyboardItem
import com.keunsori.presentation.model.LetterMatchType
import com.keunsori.presentation.model.UserInput
import com.keunsori.presentation.utils.UiEvent
import com.keunsori.presentation.utils.UiState

sealed interface InGameEvent : UiEvent {
    data class SelectLetter(val letter: Char) : InGameEvent
    data object ClickEnterButton : InGameEvent
    data object ClickBackspaceButton : InGameEvent
    data object TryAgain : InGameEvent
    data object GetQuizData : InGameEvent
}

sealed interface InGameUiState : UiState {
    data object Loading : InGameUiState

    /**
     * 인게임 화면에 표시될 정보
     *
     * @property quizSize 퀴즈 사이즈 (몇 개의 자모로 구성된 단어인지)
     * @property currentTrialCount 현재 도전한 횟수 (0부터 시작)
     * @property maxTrialSize 최대로 도전 가능한 횟수
     * @property userInputsHistory 사용자가 그동안 입력한 정답 정보를 담은 리스트
     * @property currentUserInput 사용자가 현재 입력 중인 정답 정보
     * @property keyboardItems 키보드에 표시될 요소 (2차원 리스트)
     * @property isGameFinished 게임 종료 여부
     * @property isCorrectAnswer 정답 맞췄는지 여부
     */
    data class Main(
        val quizSize: Int,
        val currentTrialCount: Int,
        val maxTrialSize: Int,
        val userInputsHistory: List<UserInput>,
        val currentUserInput: UserInput,
        val keyboardItems: List<List<KeyboardItem>>,
        val isGameFinished: Boolean,
        val isCorrectAnswer: Boolean,
    ) : InGameUiState {
        companion object {
            fun init(size: Int): Main {
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
                return Main(
                    quizSize = size,
                    0,
                    6,
                    emptyList(),
                    UserInput(emptyList()),
                    letters,
                    isGameFinished = false,
                    isCorrectAnswer = false
                )
            }
        }
    }

}

