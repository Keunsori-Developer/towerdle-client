package com.keunsori.presentation.ui.ingame

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keunsori.presentation.R
import com.keunsori.presentation.ui.theme.AppTypography

@Composable
internal fun GameGuideScreen(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.3f)), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    RoundedCornerShape(20.dp)
                )
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
                .fillMaxWidth(0.8f)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "close")
                }
            }

            Text(
                "게임 설명",
                style = AppTypography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(
                text = buildAnnotatedString {
                    append("입력한 결과에 따라 타일 색상이 변경돼요.\n\n")
                    withStyle(
                        style = SpanStyle(
                            color = com.keunsori.presentation.ui.theme.Color.ingameMatched,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("초록색")
                    }
                    append(
                        " 이라면\n" +
                                ": 표시된 자모가 정확히 같은 위치에 위치해요.\n"
                    )
                    withStyle(
                        style = SpanStyle(
                            color = com.keunsori.presentation.ui.theme.Color.ingameWrongSpot,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("노란색")
                    }
                    append(
                        " 이라면\n" +
                                ": 표시된 자모가 존재하지만 해당 위치는 아니에요.\n"
                    )
                    withStyle(
                        style = SpanStyle(
                            color = com.keunsori.presentation.ui.theme.Color.ingameNotExist,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("회색")
                    }
                    append(
                        " 이라면\n" +
                                ": 해당 자모는 단어에 포함되지 않아요."
                    )
                },
                style = AppTypography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 15.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "정답 단어가 ‘인사’ 인 경우",
                style = AppTypography.bodyMedium,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Example()

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun Example() {
    val example1 = listOf<Pair<String, Color>>(
        "ㄱ" to com.keunsori.presentation.ui.theme.Color.ingameNotExist,
        "ㅏ" to com.keunsori.presentation.ui.theme.Color.ingameNotExist,
        "ㅁ" to com.keunsori.presentation.ui.theme.Color.ingameNotExist,
        "ㅈ" to com.keunsori.presentation.ui.theme.Color.ingameNotExist,
        "ㅏ" to com.keunsori.presentation.ui.theme.Color.ingameMatched,
    )
    val example2 = listOf<Pair<String, Color>>(
        "ㄱ" to com.keunsori.presentation.ui.theme.Color.ingameNotExist,
        "ㅕ" to com.keunsori.presentation.ui.theme.Color.ingameNotExist,
        "ㅇ" to com.keunsori.presentation.ui.theme.Color.ingameWrongSpot,
        "ㄱ" to com.keunsori.presentation.ui.theme.Color.ingameNotExist,
        "ㅣ" to com.keunsori.presentation.ui.theme.Color.ingameWrongSpot,
    )
    val example3 = listOf<Pair<String, Color>>(
        "ㅇ" to com.keunsori.presentation.ui.theme.Color.ingameMatched,
        "ㅣ" to com.keunsori.presentation.ui.theme.Color.ingameMatched,
        "ㄴ" to com.keunsori.presentation.ui.theme.Color.ingameMatched,
        "ㅅ" to com.keunsori.presentation.ui.theme.Color.ingameMatched,
        "ㅏ" to com.keunsori.presentation.ui.theme.Color.ingameMatched,
    )

    @Composable
    fun exampleRow(data: List<Pair<String, Color>>, description: String) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier
                    .height(35.dp)
                    .wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (d in data) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ingame_container),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(d.second),
                        )
                        Text(d.first, fontSize = 15.sp)
                    }
                }
            }
            Text(description, style = AppTypography.bodyMedium)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        exampleRow(
            example1, "'ㅏ' 가 정확히 다섯 번째 자모에 위치해요.\n" +
                    "나머지 자모들은 단어에 포함되지 않아요."
        )
        exampleRow(
            example2, "'ㅇ'과 'ㅣ' 가 단어에는 포함되지만,\n" +
                    "정확한 위치에 있지는 않아요."
        )
        exampleRow(
            example3, "모든 자모가 완벽히 제 자리에 위치해 있어요.\n" +
                    "문제 풀이 성공!"
        )

    }
}

@Preview
@Composable
private fun GameGuideScreen_Preview() {
    GameGuideScreen(onClose = {})
}