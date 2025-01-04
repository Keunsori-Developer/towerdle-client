package com.keunsori.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.keunsori.presentation.R
import com.keunsori.presentation.intent.InGameEvent
import com.keunsori.presentation.intent.InGameUiState
import com.keunsori.presentation.ui.ingame.Keyboard
import com.keunsori.presentation.ui.ingame.MenuBar
import com.keunsori.presentation.ui.ingame.ResultScreen
import com.keunsori.presentation.ui.ingame.UserInputScreen
import com.keunsori.presentation.viewmodel.InGameViewModel
import kotlinx.coroutines.launch

@Composable
fun InGameScreen(inGameViewModel: InGameViewModel, navigateToMain: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    val uiState = inGameViewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            MenuBar(onBackClicked = { /*TODO*/ }, onHelpButtonClicked = { /*TODO*/ })
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (uiState.value) {
                    InGameUiState.Loading -> Loading()
                    is InGameUiState.Main -> Main(
                        uiState = uiState.value as InGameUiState.Main,
                        inGameViewModel = inGameViewModel
                    )
                }
            }
        }

        val mainState = uiState.value as? InGameUiState.Main ?: return@Box
        if (mainState.isGameFinished) {
            ResultScreen(
                isCorrectAnswer = mainState.isCorrectAnswer,
                realAnswer = inGameViewModel.quizData.first.word,
                definitions = inGameViewModel.quizData.first.definitions,
                congratImage = {
                    AsyncImage(
                        model = R.drawable.congrat,
                        contentDescription = null,
                        imageLoader = inGameViewModel.gifLoader.gifEnabledLoader,
                        modifier = Modifier.size(200.dp)
                    )
                },
                onQuitButtonClicked = navigateToMain,
                onRetryButtonClicked = {
                    coroutineScope.launch {
                        inGameViewModel.sendEvent(InGameEvent.TryAgain)
                    }
                }
            )
        }
    }


}

@Composable
private fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }

}

@Composable
private fun Main(uiState: InGameUiState.Main, inGameViewModel: InGameViewModel) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        UserInputScreen(
            userInputHistory = uiState.userInputsHistory,
            currentUserInput = uiState.currentUserInput,
            maxTrialSize = uiState.maxTrialSize,
            quizSize = uiState.quizSize
        )
        Keyboard(
            keyboardItems = uiState.keyboardItems,
            onLetterClicked = {
                coroutineScope.launch { inGameViewModel.sendEvent(InGameEvent.SelectLetter(it)) }
            },
            onEnterClicked = {
                coroutineScope.launch { inGameViewModel.sendEvent(InGameEvent.ClickEnterButton) }
            },
            onBackspaceClicked = {
                coroutineScope.launch { inGameViewModel.sendEvent(InGameEvent.ClickBackspaceButton) }
            })
    }
}
