package com.keunsori.presentation.ui.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keunsori.presentation.R
import com.keunsori.presentation.ui.util.TopBar

@Composable
fun ChooseLevelScreen(navigateToHome: () -> Unit, navigateToInGame: (Int) -> Unit) {
    var selectedLevel by rememberSaveable {
        mutableIntStateOf(1)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {
        TopBar("시작하기", onBackButtonClicked = navigateToHome)
        Text("난이도 선택", fontSize = 24.sp)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
            LevelItem(
                iconRes = R.drawable.level_1_easy,
                title = "쉬움",
                description = "6자모의 2글자 단어가 나옵니다.\n" +
                        "복합자모 (ex. ㅚ, ㅟ, ㄺ)가 들어가지 않습니다.",
                isSelected = selectedLevel == 1,
                onClicked = { selectedLevel = 1 }
            )
            LevelItem(
                iconRes = R.drawable.level_2_normal,
                title = "보통",
                description = "복합자모가 포함된 6자모의 2글자 단어가 나옵니다.\n" +
                        "겹치는 자음이 있을수도 있습니다.",
                isSelected = selectedLevel == 2,
                onClicked = { selectedLevel = 2 }
            )
            LevelItem(
                iconRes = R.drawable.level_3_hard,
                title = "어려움",
                description = "8자모 혹은 9자모의 3글자 단어가 나옵니다.\n" +
                        "복합자모가 들어가지 않습니다.\n" +
                        "겹치는 자음이 있을수도 있습니다.",
                isSelected = selectedLevel == 3,
                onClicked = { selectedLevel = 3 }
            )
            LevelItem(
                iconRes = R.drawable.level_4_very_hard,
                title = "매우 어려움",
                description = "8자모 혹은 9자모의 3글자 단어가 나옵니다.\n" +
                        "복합자모가 포함됩니다.\n" +
                        "겹치는 자음이 있을수도 있습니다.",
                isSelected = selectedLevel == 4,
                onClicked = { selectedLevel = 4 }
            )
        }
        ElevatedButton(
            onClick = { navigateToInGame(selectedLevel) },
            modifier = Modifier.padding(bottom = 30.dp)
        ) {
            Text("시작하기")
        }
    }
}

@Composable
private fun LevelItem(
    @DrawableRes iconRes: Int,
    title: String,
    description: String,
    isSelected: Boolean,
    onClicked: () -> Unit
) {
    Card(
        onClick = onClicked,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
        ),
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(title, style = MaterialTheme.typography.titleMedium)
            }
            Text(description, style = MaterialTheme.typography.bodyMedium)
        }
    }


}

@Preview
@Composable
private fun ChooseLevelScreen_Preview() {
    Surface {

        ChooseLevelScreen({}) {

        }
    }
}