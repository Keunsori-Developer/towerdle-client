package com.keunsori.presentation.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.keunsori.presentation.R
import com.keunsori.presentation.intent.InGameEffect
import com.keunsori.presentation.intent.InGameEvent
import com.keunsori.presentation.intent.InGameUiState
import com.keunsori.presentation.ui.ingame.GameGuideScreen
import com.keunsori.presentation.ui.ingame.Keyboard
import com.keunsori.presentation.ui.ingame.MenuBar
import com.keunsori.presentation.ui.ingame.ResultScreen
import com.keunsori.presentation.ui.ingame.UserInputScreen
import com.keunsori.presentation.ui.util.Dialog
import com.keunsori.presentation.viewmodel.InGameViewModel
import kotlinx.coroutines.launch

@Composable
fun InGameScreen(inGameViewModel: InGameViewModel, navigateToMain: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        inGameViewModel.effectFlow.collect {
            when (it) {
                is InGameEffect.ShowToast -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val uiState = inGameViewModel.uiState.collectAsState()

    var moveToBackDialogOpened by remember { mutableStateOf(false) }
    var guideScreenOpened by remember { mutableStateOf(false) }

    BackHandler {
        if (!moveToBackDialogOpened) moveToBackDialogOpened = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            MenuBar(
                onBackClicked = { moveToBackDialogOpened = true },
                onHelpButtonClicked = { guideScreenOpened = true })
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
                    inGameViewModel.sendEvent(InGameEvent.TryAgain)
                }
            )
        }
    }

    if (moveToBackDialogOpened) {
        Dialog(
            title = "게임 그만두기",
            message = "진행 중인 게임을\n" +
                    "정말로 종료하시겠어요?",
            oneButtonOnly = false,
            cancelButtonText = "그만두기",
            confirmButtonText = "계속하기",
            onDismissRequest = { moveToBackDialogOpened = false },
            onConfirm = { moveToBackDialogOpened = false },
            onCancel = { navigateToMain() })
    }
    if (guideScreenOpened) {
        GameGuideScreen(onClose = { guideScreenOpened = false })
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${uiState.currentTrialCount + 1}번째 시도 (${uiState.currentTrialCount + 1}/${uiState.maxTrialSize})",
            style = MaterialTheme.typography.labelMedium
        )
        UserInputScreen(
            userInputHistory = uiState.userInputsHistory,
            currentUserInput = uiState.currentUserInput,
            maxTrialSize = uiState.maxTrialSize,
            quizSize = uiState.quizSize
        )
        Keyboard(
            keyboardItems = uiState.keyboardItems,
            onLetterClicked = {
                inGameViewModel.sendEvent(InGameEvent.SelectLetter(it))
            },
            onEnterClicked = {
                inGameViewModel.sendEvent(InGameEvent.ClickEnterButton)
            },
            onBackspaceClicked = {
                inGameViewModel.sendEvent(InGameEvent.ClickBackspaceButton)
            })
    }
}
