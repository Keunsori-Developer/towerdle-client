package com.keunsori.presentation.ui.ingame

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keunsori.presentation.R
import com.keunsori.presentation.model.UserInput

@Composable
fun UserInputScreen(
    userInputHistory: List<UserInput>,
    currentUserInput: UserInput,
    maxTrialSize: Int,
    quizSize: Int
) {
    Box(
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            for (input in userInputHistory) {
                // 이미 입력 완료된 Row
                UserTrialRow(input = input, trialOnGoing = false, quizSize = quizSize)
            }
            for (i in userInputHistory.size until maxTrialSize) {
                if (i == userInputHistory.size) {
                    // 현재 입력중인 Row
                    UserTrialRow(
                        input = currentUserInput,
                        trialOnGoing = true,
                        quizSize = quizSize
                    )
                } else {
                    // 입력하지 않은 Row
                    UserTrialRow(input = null, trialOnGoing = false, quizSize = quizSize)
                }
            }
        }
    }
}

@Composable
private fun UserTrialRow(input: UserInput?, trialOnGoing: Boolean, quizSize: Int) {
    Row(
        modifier = Modifier.wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0..<quizSize) {
            val element = try {
                input?.elements?.get(i)
            } catch (_: Exception) {
                null
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ingame_container),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = if (trialOnGoing) MaterialTheme.colorScheme.onPrimaryContainer
                        else element?.color ?: MaterialTheme.colorScheme.outline
                    ),
                )
                Text(element?.run { letter.toString() } ?: "", fontSize = 20.sp)
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
