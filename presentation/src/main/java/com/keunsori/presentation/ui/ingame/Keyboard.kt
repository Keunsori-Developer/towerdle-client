package com.keunsori.presentation.ui.ingame

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keunsori.presentation.R
import com.keunsori.presentation.intent.InGameUiState
import com.keunsori.presentation.model.KeyboardItem
import com.keunsori.presentation.model.LetterMatchType

@Composable
fun Keyboard(
    keyboardItems: List<List<KeyboardItem>>,
    onLetterClicked: (Char) -> Unit,
    onEnterClicked: () -> Unit,
    onBackspaceClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (items in keyboardItems) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (item in items) {
                        when (item) {
                            KeyboardItem.Backspace -> Backspace(onBackspaceClicked)
                            KeyboardItem.Enter -> Enter(onEnterClicked)
                            is KeyboardItem.Letter -> Letter(
                                item.letter,
                                item.matchType.color,
                                onLetterClicked
                            )
                            KeyboardItem.Empty -> Empty()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Letter(letter: Char, statusColor: Color, onClicked: (Char) -> Unit) {
    Box(
        modifier = Modifier
            .size(30.dp, 50.dp)
            .customBackgroundWithBorder(statusColor)
            .clickable { onClicked(letter) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = letter.toString())
    }
}

@Composable
private fun Enter(onClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .size(50.dp, 50.dp)
            .customBackgroundWithBorder()
            .clickable { onClicked() },
        contentAlignment = Alignment.Center
    ) {
        Text("Enter")
    }
}

@Composable
private fun Backspace(onClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .size(50.dp, 50.dp).customBackgroundWithBorder()
            .clickable { onClicked() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.backspace_solid),
            contentDescription = "Backspace",
            modifier = Modifier.size(25.dp)
        )
    }
}

@Composable
private fun Empty() {
    Box(
        modifier = Modifier
            .size(30.dp, 50.dp).customBackgroundWithBorder()
    )
}

@Composable
private fun Modifier.customBackgroundWithBorder(color: Color = Color.Gray): Modifier {
    return this.background(color, RoundedCornerShape(4.dp))
        .border(width = 0.1.dp, color = Color.Black, shape = RoundedCornerShape(4.dp))
}

@Preview
@Composable
fun Keyboard_Preview() {
    Keyboard(keyboardItems = InGameUiState.init().keyboardItems, {}, {  },{})
}
