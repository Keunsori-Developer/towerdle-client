package com.keunsori.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.keunsori.presentation.intent.ChallengeEvent
import com.keunsori.presentation.intent.ChallengeState
import com.keunsori.presentation.intent.InGameEvent
import com.keunsori.presentation.intent.InGameUiState
import com.keunsori.presentation.ui.ingame.CommonInGameScreen
import com.keunsori.presentation.viewmodel.ChallengeViewModel
import com.keunsori.presentation.viewmodel.InGameViewModel

@Composable
fun ChallengeInGameScreen(
    inGameViewModel: InGameViewModel,
    challengeViewModel: ChallengeViewModel,
    navigateToMain: () -> Unit
) {
    val state = inGameViewModel.uiState.collectAsState()
    val historyUpdated = rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(key1 = state.value) {
        if (state.value is InGameUiState.Main && !historyUpdated.value) {
            historyUpdated.value = true
            val savedQuizInputs =
                (challengeViewModel.uiState.value as? ChallengeState.CanStart)?.quizInputs
            savedQuizInputs?.let {
                inGameViewModel.sendEvent(InGameEvent.UpdateInGoingHistory(it))
            }
        }
    }
    (state.value as? InGameUiState.Main)?.let {
        LaunchedEffect(key1 = it.currentTrialCount) {
            if (it.currentTrialCount == 0) return@LaunchedEffect
            challengeViewModel.sendEvent(
                ChallengeEvent.SaveUserInput(it.currentTrialCount, it.userInputsHistory.last())
            )
        }
    }
    CommonInGameScreen(inGameViewModel = inGameViewModel, navigateToMain = navigateToMain)
}
