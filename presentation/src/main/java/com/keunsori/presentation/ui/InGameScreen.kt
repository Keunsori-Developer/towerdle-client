package com.keunsori.presentation.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keunsori.presentation.intent.InGameEvent
import com.keunsori.presentation.ui.ingame.Keyboard
import com.keunsori.presentation.ui.ingame.UserInputScreen
import com.keunsori.presentation.viewmodel.InGameViewModel
import kotlinx.coroutines.launch

@Composable
fun InGameScreen(inGameViewModel: InGameViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = inGameViewModel.uiState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserInputScreen(
            userInputs = uiState.value.userInputs,
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
}
