package com.keunsori.presentation.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class InGameViewModel @Inject constructor() : ViewModel() {
    private val event = Channel<InGameEvent>()

    val uiState = event.receiveAsFlow()
        .runningFold(InGameUiState.init(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), InGameUiState.init())

    suspend fun sendEvent(newEvent: InGameEvent) {
        event.send(newEvent)
    }

    private fun reduceState(currentState: InGameUiState, event: InGameEvent): InGameUiState {
        return when (event) {
            InGameEvent.ClickBackspaceButton -> TODO()
            InGameEvent.ClickEnterButton -> TODO()
            is InGameEvent.SelectLetter -> {
                val newElement = UserInput.Element(event.letter, Color.White)

                val list = currentState.userInputs.toMutableList()
                var currentInput: UserInput

                if (list.isEmpty()) {
                    currentInput = UserInput(listOf(newElement))
                    list.add(currentInput)
                } else {
                    val element = list.last().elements.toMutableList().also { it.add(newElement) }
                    currentInput = UserInput(element)
                    list[currentState.currentTrialCount] = currentInput
                }

                currentState.copy(userInputs = list)
            }
        }
    }

}
