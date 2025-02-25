package com.keunsori.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.domain.entity.ChallengeModeData
import com.keunsori.domain.usecase.GetChallengeDataUseCase
import com.keunsori.presentation.intent.ChallengeGuideEvent
import com.keunsori.presentation.intent.ChallengeGuideReducer
import com.keunsori.presentation.intent.ChallengeGuideState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeGuideViewModel @Inject constructor(
    private val getChallengeDataUseCase: GetChallengeDataUseCase
) : ViewModel() {
    private val reducer = ChallengeGuideReducer(ChallengeGuideState.Loading)

    val uiState get() = reducer.uiState

    init {
        viewModelScope.launch {
            getChallengeData()
        }
    }

    fun sendEvent(event: ChallengeGuideEvent) {
        viewModelScope.launch {
            when (event) {
                ChallengeGuideEvent.RetryToGetData -> {
                    getChallengeData()
                }

                is ChallengeGuideEvent.ToggleShowResultButton -> TODO()
            }
        }
    }

    private suspend fun getChallengeData() {
        when (val data = getChallengeDataUseCase()) {
            ChallengeModeData.FailedToGetModeData -> {
                reducer.setState(ChallengeGuideState.FailToLoad)
            }

            is ChallengeModeData.Finished -> {
                reducer.setState(ChallengeGuideState.Finished(data.date, data.quizInputResults))
            }

            is ChallengeModeData.InGoing -> {
                reducer.setState(
                    ChallengeGuideState.CanStart(
                        data.date,
                        true,
                        data.quizInfo.length
                    )
                )
            }

            is ChallengeModeData.NotStarted -> {
                reducer.setState(
                    ChallengeGuideState.CanStart(
                        data.date,
                        false,
                        data.quizInfo.length
                    )
                )
            }
        }
    }

}
