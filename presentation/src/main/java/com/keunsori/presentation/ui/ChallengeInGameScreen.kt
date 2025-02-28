package com.keunsori.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.keunsori.presentation.R
import com.keunsori.presentation.intent.ChallengeEvent
import com.keunsori.presentation.intent.ChallengeState
import com.keunsori.presentation.intent.InGameEvent
import com.keunsori.presentation.intent.InGameUiState
import com.keunsori.presentation.ui.ingame.CommonInGameScreen
import com.keunsori.presentation.ui.ingame.ResultScreen
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
    CommonInGameScreen(
        inGameViewModel = inGameViewModel,
        navigateToMain = navigateToMain,
        resultScreenContent = { isSuccess ->
            ResultScreen(
                isCorrectAnswer = isSuccess,
                realAnswer = inGameViewModel.quizData.first.word,
                definitions = inGameViewModel.quizData.first.definitions,
                congratImage = {
                    AsyncImage(
                        model = R.drawable.congrat,
                        contentDescription = null,
                        imageLoader = inGameViewModel.gifLoader.gifEnabledLoader,
                        modifier = Modifier.size(200.dp)
                    )
                }) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {/*TODO*/},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("공유하기", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = navigateToMain,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("종료하기", color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        })
}
