package com.keunsori.towerdle.presentation.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.keunsori.towerdle.data.handler.InGameLogicHandler
import com.keunsori.towerdle.data.repository.InGameRepository
import com.keunsori.towerdle.domain.entity.QuizInputResult
import com.keunsori.towerdle.presentation.main.viewmodel.MainViewModel

@Composable
fun GameScreen(viewModel: MainViewModel, navHostController: NavHostController) {
    // TODO: 수정

    val inGameRepository = InGameRepository()
    val handler = InGameLogicHandler()
    val answer = handler.parseStringWordToArray("분홍")
    var text by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray)) {
        TextField(value = text, onValueChange = { value -> text = value })
        Trial(
            quizInputResult = inGameRepository.checkAnswer(
                handler.parseStringWordToArray(text),
                answer
            )
        )
    }

}

@Composable
fun Trial(quizInputResult: QuizInputResult) {
    Row(modifier = Modifier.height(50.dp)) {
        for (input in quizInputResult.list) {
            val color = when (input.type) {
                QuizInputResult.Type.MATCHED -> Color.Green
                QuizInputResult.Type.WRONG_SPOT -> Color.Yellow
                QuizInputResult.Type.NOT_EXIST -> Color.White
            }
            Box(
                modifier = Modifier
                    .background(color)
                    .size(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(input.letter.toString())
            }
        }
    }
}
