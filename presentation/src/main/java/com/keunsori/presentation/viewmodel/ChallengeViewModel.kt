package com.keunsori.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.domain.entity.ChallengeModeData
import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.domain.usecase.GetTodayChallengeDataUseCase
import com.keunsori.domain.usecase.SaveChallengeUserInputUseCase
import com.keunsori.domain.usecase.ShareChallengeResultUseCase
import com.keunsori.presentation.intent.ChallengeEffect
import com.keunsori.presentation.intent.ChallengeEvent
import com.keunsori.presentation.intent.ChallengeReducer
import com.keunsori.presentation.intent.ChallengeState
import com.keunsori.presentation.model.UserInput.Element.Companion.toDomainModel
import com.keunsori.presentation.model.UserInput.Element.Companion.toPresentationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val getTodayChallengeDataUseCase: GetTodayChallengeDataUseCase,
    private val saveChallengeUserInputUseCase: SaveChallengeUserInputUseCase,
    private val shareChallengeResultUseCase: ShareChallengeResultUseCase
) : ViewModel() {
    private val reducer = ChallengeReducer(ChallengeState.Loading)
    private var todayDate =
        SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(System.currentTimeMillis())

    val uiState get() = reducer.uiState

    private val effectChannel = Channel<ChallengeEffect>(Channel.BUFFERED)
    val effectFlow = effectChannel.receiveAsFlow()

    fun sendEvent(event: ChallengeEvent) {
        viewModelScope.launch {
            when (event) {
                ChallengeEvent.GetData -> {
                    getChallengeData()
                }

                is ChallengeEvent.SaveUserInput -> {
                    saveChallengeUserInputUseCase(
                        trialCount = event.trialCount,
                        input = event.userInput.elements.map { it.toDomainModel() })
                }

                is ChallengeEvent.ShareResult -> {
                    val text = shareChallengeResultUseCase.invoke(
                        todayDate,
                        event.quizInputResult.map { input -> input.map { it.toDomainModel() } })
                    effectChannel.send(ChallengeEffect.OpenIntent(text))
                }
            }
        }
    }

    private suspend fun getChallengeData() {
        reducer.setState(ChallengeState.Loading)
        todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(System.currentTimeMillis())
        when (val data = getTodayChallengeDataUseCase(todayDate)) {
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
                        data.quizInfo.count,
                        data.quizInfo.maxAttempts,
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
                        data.quizInfo.count,
                        data.quizInfo.maxAttempts,
                        null
                    )
                )
            }
        }
    }
}
