package com.keunsori.presentation.intent

import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.presentation.utils.Reducer
import com.keunsori.presentation.utils.UiEvent
import com.keunsori.presentation.utils.UiState

sealed interface ChallengeGuideState : UiState {
    data object Loading : ChallengeGuideState
    data object FailToLoad : ChallengeGuideState
    data class CanStart(val date: String, val isOnGoing: Boolean, val wordLength: Int) : ChallengeGuideState

    data class Finished(val date: String, val quizInputs: List<List<QuizInputResult.Element>>) :
        ChallengeGuideState
}

sealed interface ChallengeGuideEvent : UiEvent {
    data object RetryToGetData : ChallengeGuideEvent
    data class ToggleShowResultButton(val isOn: Boolean) : ChallengeGuideEvent
}

class ChallengeGuideReducer(initState: ChallengeGuideState) :
    Reducer<ChallengeGuideState, ChallengeGuideEvent>(initState) {
    override suspend fun reduce(oldState: ChallengeGuideState, event: ChallengeGuideEvent) {
        when (event) {
            is ChallengeGuideEvent.ToggleShowResultButton -> TODO()
            ChallengeGuideEvent.RetryToGetData -> {
                setState(ChallengeGuideState.Loading)
            }
        }
    }

}
