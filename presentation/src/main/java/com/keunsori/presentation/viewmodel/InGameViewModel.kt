package com.keunsori.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.domain.usecase.CheckAnswerUseCase
import com.keunsori.domain.usecase.GetQuizWordUseCase
import com.keunsori.presentation.intent.InGameEvent
import com.keunsori.presentation.intent.InGameUiState
import com.keunsori.presentation.model.KeyboardItem
import com.keunsori.presentation.model.LetterMatchType
import com.keunsori.presentation.model.UserInput
import com.keunsori.presentation.ui.theme.Color
import com.keunsori.presentation.utils.GifLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InGameViewModel @Inject constructor(
    private val getQuizWordUseCase: GetQuizWordUseCase,
    private val checkAnswerUseCase: CheckAnswerUseCase
) : ViewModel() {

    @Inject lateinit var gifLoader: GifLoader
    private val event = Channel<InGameEvent>()

    /**
     * ex)
     * first: 안녕
     * second: ㅇㅏㄴㄴㅕㅇ
     */
    lateinit var answer: Pair<String, CharArray>
        private set

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                answer = getQuizWordUseCase()
                println("answer: ${answer.first}")
            }
        }
    }

    val uiState = event.receiveAsFlow()
        .runningFold(InGameUiState.init(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), InGameUiState.init())

    suspend fun sendEvent(newEvent: InGameEvent) {
        event.send(newEvent)
    }

    private fun reduceState(currentState: InGameUiState, event: InGameEvent): InGameUiState {
        return when (event) {
            InGameEvent.ClickBackspaceButton -> currentState.handleBackspaceButton()
            InGameEvent.ClickEnterButton -> currentState.handleEnterButton(answer.second)
            is InGameEvent.SelectLetter -> currentState.handleClickedLetter(event.letter)
        }
    }

    private fun InGameUiState.handleBackspaceButton(): InGameUiState {
        val updatedUserInput = currentUserInput.copy(
            elements = currentUserInput.elements.toMutableList()
                .also { e -> if (e.isNotEmpty()) e.removeLast() })
        return this.copy(currentUserInput = updatedUserInput)
    }

    private fun InGameUiState.handleEnterButton(answer: CharArray): InGameUiState {
        if (currentUserInput.elements.size != quizSize) return this

        val newUserInputs = userInputsHistory.toMutableList()
        val answerResult =
            checkAnswerUseCase(currentUserInput.elements.map { it.letter }.toCharArray(), answer)

        // 유저가 입력한 정답 체크
        val checkedUserInput = answerResult.list.map { e ->
            when (e.type) {
                QuizInputResult.Type.MATCHED -> UserInput.Element(e.letter, Color.ingameMatched)
                QuizInputResult.Type.WRONG_SPOT -> UserInput.Element(
                    e.letter,
                    Color.ingameWrongSpot
                )

                QuizInputResult.Type.NOT_EXIST -> UserInput.Element(e.letter, Color.ingameNotExist)
            }
        }
        newUserInputs.add(UserInput(checkedUserInput))

        // 키보드 색상 변경
        val newKeyboardItems = keyboardItems.toMutableList()

        for (result in answerResult.list) {
            var y = 0
            for (x in keyboardItems.indices) {
                y =
                    keyboardItems[x].indexOfFirst { item -> item is KeyboardItem.Letter && item.letter == result.letter }

                if (y != -1) {
                    val newList = newKeyboardItems[x].toMutableList().apply {
                        val currentMatchType =
                            (this[y] as? KeyboardItem.Letter)?.matchType ?: LetterMatchType.NONE

                        val newMatchType = when (result.type) {
                            QuizInputResult.Type.MATCHED -> LetterMatchType.MATCHED
                            QuizInputResult.Type.WRONG_SPOT -> {
                                if (currentMatchType == LetterMatchType.MATCHED) currentMatchType else LetterMatchType.WRONG_SPOT
                            }

                            QuizInputResult.Type.NOT_EXIST -> {
                                if (currentMatchType == LetterMatchType.MATCHED || currentMatchType == LetterMatchType.WRONG_SPOT) currentMatchType
                                else LetterMatchType.NOT_EXIST
                            }
                        }
                        this[y] = KeyboardItem.Letter(result.letter, newMatchType)
                    }
                    newKeyboardItems[x] = newList
                }
            }
        }

        val isAnswer = answerResult.list.all { it.type == QuizInputResult.Type.MATCHED }

        return this.copy(
            currentTrialCount = currentTrialCount + 1,
            userInputsHistory = newUserInputs,
            currentUserInput = UserInput.empty,
            keyboardItems = newKeyboardItems,
            isGameFinished = currentTrialCount + 1 == this.maxTrialSize || isAnswer,
            isCorrectAnswer = isAnswer

        )
    }

    private fun InGameUiState.handleClickedLetter(clickedLetter: Char): InGameUiState {
        val newElement = UserInput.Element(clickedLetter)

        val newElements = currentUserInput.elements.toMutableList()
        if (newElements.size >= quizSize) return this

        newElements.add(newElement)
        val newInput = currentUserInput.copy(elements = newElements)

        return this.copy(currentUserInput = newInput)
    }

}
