package com.keunsori.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.domain.entity.ChallengeModeData
import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.domain.usecase.GetTodayChallengeDataUseCase
import com.keunsori.domain.usecase.SaveChallengeUserInputUseCase
import com.keunsori.presentation.intent.ChallengeEvent
import com.keunsori.presentation.intent.ChallengeReducer
import com.keunsori.presentation.intent.ChallengeState
import com.keunsori.presentation.model.UserInput.Element.Companion.toPresentationModel
import com.keunsori.presentation.ui.theme.Color
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val getTodayChallengeDataUseCase: GetTodayChallengeDataUseCase,
    private val saveChallengeUserInputUseCase: SaveChallengeUserInputUseCase
) : ViewModel() {
    private val reducer = ChallengeReducer(ChallengeState.Loading)

    val uiState get() = reducer.uiState

    fun sendEvent(event: ChallengeEvent) {
        viewModelScope.launch {
            when (event) {
                ChallengeEvent.GetData -> {
                    getChallengeData()
                }

                is ChallengeEvent.ToggleShowResultButton -> TODO()
                is ChallengeEvent.SaveUserInput -> {
                    saveChallengeUserInputUseCase(
                        trialCount = event.trialCount,
                        input = event.userInput.elements.map {
                            QuizInputResult.Element(
                                it.letter, type = when (it.color) {
                                    Color.ingameMatched -> QuizInputResult.Type.MATCHED
                                    Color.ingameWrongSpot -> QuizInputResult.Type.WRONG_SPOT
                                    else -> QuizInputResult.Type.NOT_EXIST
                                }
                            )
                        })
                }
            }
        }
    }

    private suspend fun getChallengeData() {
        reducer.setState(ChallengeState.Loading)
        when (val data = getTodayChallengeDataUseCase()) {
            ChallengeModeData.FailedToGetModeData -> {
                reducer.setState(ChallengeState.FailToLoad)
            }

            is ChallengeModeData.Finished -> {
                reducer.setState(
                    ChallengeState.Finished(
                        data.date,
                        data.quizInputResults.map { result -> result.map { it.toPresentationModel() } })
                )
            }

            is ChallengeModeData.InGoing -> {
                reducer.setState(
                    ChallengeState.CanStart(
                        data.date,
                        true,
                        data.quizInfo.length,
                        data.quizInputResults
                    )
                )
            }

            is ChallengeModeData.NotStarted -> {
                reducer.setState(
                    ChallengeState.CanStart(
                        data.date,
                        false,
                        data.quizInfo.length,
                        null
                    )
                )
            }
        }
    }
}
