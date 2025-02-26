package com.keunsori.presentation.intent

import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.presentation.model.UserInput
import com.keunsori.presentation.utils.Reducer
import com.keunsori.presentation.utils.UiEvent
import com.keunsori.presentation.utils.UiState

sealed interface ChallengeState : UiState {
    data object Loading : ChallengeState
    data object FailToLoad : ChallengeState
    data class CanStart(
        val date: String,
        val isOnGoing: Boolean,
        val wordLength: Int,
        val quizInputs: List<List<QuizInputResult.Element>>?
    ) : ChallengeState

    data class Finished(val date: String, val quizInputs: List<List<QuizInputResult.Element>>) :
        ChallengeState
}

sealed interface ChallengeEvent : UiEvent {
    data object GetData : ChallengeEvent
    data class ToggleShowResultButton(val isOn: Boolean) : ChallengeEvent
    data class SaveUserInput(val trialCount: Int, val userInput: UserInput) : ChallengeEvent
}

class ChallengeReducer(initState: ChallengeState) :
    Reducer<ChallengeState, ChallengeEvent>(initState) {
    override suspend fun reduce(oldState: ChallengeState, event: ChallengeEvent) {
        when (event) {
            is ChallengeEvent.ToggleShowResultButton -> TODO()
            ChallengeEvent.GetData -> {
                setState(ChallengeState.Loading)
            }

            is ChallengeEvent.SaveUserInput -> { /*상태값 변경할 것이 없음*/}
        }
    }

}
