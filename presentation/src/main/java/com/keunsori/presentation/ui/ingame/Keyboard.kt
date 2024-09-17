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
import androidx.compose.material3.MaterialTheme
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
import com.keunsori.presentation.ui.theme.surfaceVariantLight

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
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
private fun Letter(letter: Char, statusColor: Color?, onClicked: (Char) -> Unit) {
    Box(
        modifier = Modifier
            .size(30.dp, 40.dp)
            .customBackground(statusColor)
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
            .size(50.dp, 40.dp)
            .customBackground()
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
            .size(50.dp, 40.dp)
            .customBackground()
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
            .size(30.dp, 40.dp)
            .customBackground()
    )
}

@Composable
private fun Modifier.customBackground(color: Color? = MaterialTheme.colorScheme.surfaceVariant): Modifier {
    return this
        .background(color ?: MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
}

@Preview
@Composable
fun Keyboard_Preview() {
    Keyboard(keyboardItems = InGameUiState.init().keyboardItems, {}, { }, {})
}
