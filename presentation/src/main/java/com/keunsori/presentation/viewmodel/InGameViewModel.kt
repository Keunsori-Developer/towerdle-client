package com.keunsori.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.domain.entity.QuizInfo
import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.domain.entity.QuizLevel
import com.keunsori.domain.usecase.CheckAnswerUseCase
import com.keunsori.domain.usecase.GetQuizInfoUseCase
import com.keunsori.domain.usecase.IsExistWordUseCase
import com.keunsori.domain.usecase.SendQuizResultUseCase
import com.keunsori.presentation.intent.InGameEffect
import com.keunsori.presentation.intent.InGameEvent
import com.keunsori.presentation.intent.InGameUiState
import com.keunsori.presentation.intent.MainEffect
import com.keunsori.presentation.model.KeyboardItem
import com.keunsori.presentation.model.LetterMatchType
import com.keunsori.presentation.model.UserInput
import com.keunsori.presentation.ui.theme.Color
import com.keunsori.presentation.utils.GifLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
    private val savedStateHandle: SavedStateHandle,
    private val getQuizInfoUseCase: GetQuizInfoUseCase,
    private val isExistWordUseCase: IsExistWordUseCase,
    private val checkAnswerUseCase: CheckAnswerUseCase,
    private val sendQuizResultUseCase: SendQuizResultUseCase
) : ViewModel() {

    @Inject
    lateinit var gifLoader: GifLoader
    private val event = Channel<InGameEvent>()

    /**
     * ex)
     * first: QuizInfo(word = 안녕)
     * second: ㅇㅏㄴㄴㅕㅇ
     */
    lateinit var quizData: Pair<QuizInfo, CharArray>
        private set

    init {
        viewModelScope.launch {
            sendEvent(InGameEvent.GetQuizData)
        }
    }

    val uiState = event.receiveAsFlow()
        .runningFold(InGameUiState.Loading, ::reduceState)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), InGameUiState.Loading)

    fun sendEvent(newEvent: InGameEvent) {
        viewModelScope.launch {
            event.send(newEvent)
        }
    }

    private val effectChannel = Channel<InGameEffect>(Channel.BUFFERED)
    val effectFlow = effectChannel.receiveAsFlow()

    fun sendEffect(newEffect: InGameEffect) {
        viewModelScope.launch {
            effectChannel.send(newEffect)
        }
    }

    private suspend fun reduceState(state: InGameUiState, event: InGameEvent): InGameUiState {
        when (event) {
            InGameEvent.GetQuizData, InGameEvent.TryAgain -> return getQuizData()
            else -> {
                val currentState = state as? InGameUiState.Main ?: return state

                return when (event) {
                    InGameEvent.ClickBackspaceButton -> currentState.handleBackspaceButton()
                    InGameEvent.ClickEnterButton -> currentState.handleEnterButton(quizData.second)
                    is InGameEvent.SelectLetter -> currentState.handleClickedLetter(event.letter)
                    InGameEvent.TryAgain -> {
                        return getQuizData()
                    }

                    else -> return currentState
                }
            }
        }
    }

    private suspend fun getQuizData(): InGameUiState {
        val level = savedStateHandle.get<String>("level") ?: QuizLevel.EASY.name
        quizData = withContext(Dispatchers.IO) {
            getQuizInfoUseCase(QuizLevel.valueOf(level))
        }
        Log.d(this.javaClass.simpleName, "quizInfo: ${quizData.first}")
        return InGameUiState.Main.init(quizData.second.size, quizData.first.maxAttempts)
    }

    private fun InGameUiState.Main.handleBackspaceButton(): InGameUiState.Main {
        val updatedUserInput = currentUserInput.copy(
            elements = currentUserInput.elements.toMutableList()
                .also { e -> if (e.isNotEmpty()) e.removeLast() })
        return this.copy(currentUserInput = updatedUserInput)
    }

    private suspend fun InGameUiState.Main.handleEnterButton(answer: CharArray): InGameUiState.Main {
        if (currentUserInput.elements.size != quizSize) return this

        val userAnswer = currentUserInput.elements.map { it.letter }.toCharArray()

        val isExist = withContext<Boolean>(Dispatchers.IO) {
            return@withContext isExistWordUseCase(userAnswer)
        }
        if (!isExist) {
            sendEffect(InGameEffect.ShowToast("잘못된 단어를 입력했어요."))
            return this
        } // 유효하지 않은 정답 입력 시 그냥 리턴

        val newUserInputs = userInputsHistory.toMutableList()
        val answerResult = checkAnswerUseCase(userAnswer, answer)

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
        val gameFinished = currentTrialCount + 1 == this.maxTrialSize || isAnswer
        if (gameFinished) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    sendQuizResultUseCase.invoke(
                        quizData.first.uuid,
                        currentTrialCount + 1,
                        isAnswer
                    )
                }
            }
        }

        return this.copy(
            currentTrialCount = currentTrialCount + 1,
            userInputsHistory = newUserInputs,
            currentUserInput = UserInput.empty,
            keyboardItems = newKeyboardItems,
            isGameFinished = gameFinished,
            isCorrectAnswer = isAnswer

        )
    }

    private fun InGameUiState.Main.handleClickedLetter(clickedLetter: Char): InGameUiState.Main {
        val newElement = UserInput.Element(clickedLetter)

        val newElements = currentUserInput.elements.toMutableList()
        if (newElements.size >= quizSize) return this

        newElements.add(newElement)
        val newInput = currentUserInput.copy(elements = newElements)

        return this.copy(currentUserInput = newInput)
    }

}
