package com.keunsori.presentation.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.keunsori.presentation.R
import com.keunsori.presentation.intent.InGameEvent
import com.keunsori.presentation.ui.ingame.CommonInGameScreen
import com.keunsori.presentation.ui.ingame.ResultScreen
import com.keunsori.presentation.viewmodel.InGameViewModel

@Composable
fun DefaultInGameScreen(inGameViewModel: InGameViewModel, navigateToMain: () -> Unit) {
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = navigateToMain,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("끝내기", color = MaterialTheme.colorScheme.secondary)
                    }
                    VerticalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.height(55.dp)
                    )
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        onClick = {
                            inGameViewModel.sendEvent(InGameEvent.TryAgain)
                        }
                    ) {
                        Text("한번 더 하기", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }
        })
}
