package com.keunsori.presentation.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.domain.usecase.CheckAnswerUseCase
import com.keunsori.presentation.intent.InGameEvent
import com.keunsori.presentation.intent.InGameUiState
import com.keunsori.presentation.model.UserInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InGameViewModel @Inject constructor(private val checkAnswerUseCase: CheckAnswerUseCase) :
    ViewModel() {
    private val event = Channel<InGameEvent>()

    private val answer = charArrayOf('ㅂ', 'ㅜ', 'ㄴ', 'ㅎ', 'ㅗ', 'ㅇ') // TODO: 나중에 수정

    val uiState = event.receiveAsFlow()
        .runningFold(InGameUiState.init(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), InGameUiState.init())

    suspend fun sendEvent(newEvent: InGameEvent) {
        event.send(newEvent)
    }

    private fun reduceState(currentState: InGameUiState, event: InGameEvent): InGameUiState {
        return when (event) {
            InGameEvent.ClickBackspaceButton -> currentState.handleClickBackspaceButton()
            InGameEvent.ClickEnterButton -> currentState.handleClickEnterButton(answer)
            is InGameEvent.SelectLetter -> currentState.handleClickLetter(event.letter)
        }
    }

    private fun InGameUiState.handleClickBackspaceButton(): InGameUiState {
        val newList = userInputsHistory[currentTrialCount].elements.toMutableList()
        newList.removeLast()

        val newUserInputs = userInputsHistory
        return this.copy()
    }

    private fun InGameUiState.handleClickEnterButton(answer: CharArray): InGameUiState {
        val newUserInputs = userInputsHistory.toMutableList()
        val answerResult =
            checkAnswerUseCase(currentUserInput.elements.map { it.letter }.toCharArray(), answer)
        
        newUserInputs.add(currentUserInput)
        return this.copy(
            currentTrialCount = currentTrialCount + 1,
            userInputsHistory = newUserInputs,
            currentUserInput = UserInput.empty
        )
    }

    private fun InGameUiState.handleClickLetter(clickedLetter: Char): InGameUiState {
        val newElement = UserInput.Element(clickedLetter)

        val newElements = currentUserInput.elements.toMutableList()
        newElements.add(newElement)
        val newInput = currentUserInput.copy(elements = newElements)

        return this.copy(currentUserInput = newInput)
    }

}
