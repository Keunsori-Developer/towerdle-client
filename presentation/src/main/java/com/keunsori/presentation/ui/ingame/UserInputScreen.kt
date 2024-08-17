package com.keunsori.presentation.ui.ingame

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
    import com.keunsori.presentation.model.UserInput

@Composable
fun UserInputScreen(
    userInputHistory: List<UserInput>,
    currentUserInput: UserInput,
    maxTrialSize: Int,
    quizSize: Int
) {
    Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (input in userInputHistory) {
                UserTrialRow(input = input, quizSize = quizSize)
            }
            for (i in userInputHistory.size until maxTrialSize) {
                if (i == userInputHistory.size) {
                    UserTrialRow(input = currentUserInput, quizSize = quizSize)
                } else {
                    UserTrialRow(input = null, quizSize = quizSize)
                }
            }
        }
    }
}

@Composable
private fun UserTrialRow(input: UserInput?, quizSize: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (i in 0..<quizSize) {
            val element = try {
                input?.elements?.get(i)
            } catch (_: Exception) {
                null
            }
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .border(width = 0.5.dp, color = element?.color ?: Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(element?.run { letter.toString() } ?: "")
            }
        }
    }
}

@Preview
@Composable
private fun UserInputScreen_Preview() {
    val history = listOf(
        UserInput(
            elements = listOf(
                UserInput.Element('ㅇ', Color.Red),
                UserInput.Element('ㅇ', Color.Red),
                UserInput.Element('ㅇ', Color.Red),
                UserInput.Element('ㅇ', Color.Red),
                UserInput.Element('ㅇ', Color.Red),
                UserInput.Element('ㅇ', Color.Red)
            )
        )
    )
    UserInputScreen(
        userInputHistory = history,
        currentUserInput = UserInput(elements = listOf(UserInput.Element('ㅇ'))),
        maxTrialSize = 6,
        quizSize = 6
    )
}
