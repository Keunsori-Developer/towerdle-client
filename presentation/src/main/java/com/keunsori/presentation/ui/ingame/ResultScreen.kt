package com.keunsori.presentation.ui.ingame

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    buttonContents: @Composable ColumnScope.() -> Unit,
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
            buttonContents()
        }
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
        }, {})
}
