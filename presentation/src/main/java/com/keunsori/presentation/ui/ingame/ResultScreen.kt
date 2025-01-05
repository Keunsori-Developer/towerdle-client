package com.keunsori.presentation.ui.ingame

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keunsori.domain.entity.WordDefinition

@Composable
fun ResultScreen(
    isCorrectAnswer: Boolean,
    realAnswer: String,
    definitions: List<WordDefinition>,
    congratImage: @Composable () -> Unit,
    onQuitButtonClicked: () -> Unit = {},
    onRetryButtonClicked: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.3f)), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    RoundedCornerShape(20.dp)
                )
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
                .fillMaxWidth(0.8f)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (isCorrectAnswer) {
                congratImage()
                Spacer(modifier = Modifier.height(10.dp))
                Text("정답이에요!", fontSize = 22.sp)
            } else {
                Text("정답은...", fontSize = 22.sp)
            }

            Text(realAnswer, fontSize = 50.sp, modifier = Modifier.padding(vertical = 30.dp))
            Column(modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp)) {
                for (definition in definitions) {
                    Text(
                        definition.pos,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(definition.meanings.joinToString("\n"), fontSize = 15.sp)
                }
            }
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    "끝내기",
                    MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f),
                    onClick = onQuitButtonClicked
                )
                VerticalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.height(55.dp)
                )
                Button(
                    "한번 더 하기",
                    MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f),
                    onClick = onRetryButtonClicked
                )
            }
        }
    }
}

@Composable
private fun Button(text: String, color: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Text(text, color = color)
    }
}


@Preview
@Composable
fun ResultScreen_Preview() {
    ResultScreen(
        isCorrectAnswer = true,
        realAnswer = "안녕",
        definitions = listOf(
            WordDefinition("명사", listOf("사회나 국가가 안전하고 태평한 것.")),
            WordDefinition("감탄사", listOf("사람들 사이에서 헤어지거나 만날 때 나누는 인사말."))

        ),
        congratImage = {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(Color.Yellow)
            )
        }, {}, {})
}
