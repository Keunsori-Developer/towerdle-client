package com.keunsori.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.keunsori.presentation.R
import com.keunsori.presentation.intent.InGameEvent
import com.keunsori.presentation.ui.ingame.Keyboard
import com.keunsori.presentation.ui.ingame.MenuBar
import com.keunsori.presentation.ui.ingame.ResultScreen
import com.keunsori.presentation.ui.ingame.UserInputScreen
import com.keunsori.presentation.viewmodel.InGameViewModel
import kotlinx.coroutines.launch

@Composable
fun InGameScreen(inGameViewModel: InGameViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = inGameViewModel.uiState.collectAsState()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MenuBar(onBackClicked = { /*TODO*/ }, onHelpButtonClicked = { /*TODO*/ })
            UserInputScreen(
                userInputHistory = uiState.value.userInputsHistory,
                currentUserInput = uiState.value.currentUserInput,
                maxTrialSize = uiState.value.maxTrialSize,
                quizSize = uiState.value.maxTrialSize
            )
            Keyboard(
                keyboardItems = uiState.value.keyboardItems,
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
        if (uiState.value.isGameFinished) {
            ResultScreen(
                isCorrectAnswer = uiState.value.isCorrectAnswer,
                realAnswer = inGameViewModel.answer.first,
                congratImage = {
                    AsyncImage(
                        model = R.drawable.congrat,
                        contentDescription = null,
                        imageLoader = inGameViewModel.gifLoader.gifEnabledLoader,
                        modifier = Modifier.size(250.dp)
                    )
                }
            )
        }
    }
}
