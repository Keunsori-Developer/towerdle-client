package com.keunsori.presentation.ui.ingame

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keunsori.presentation.R
import com.keunsori.presentation.intent.InGameUiState
import com.keunsori.presentation.model.KeyboardItem

@Composable
fun Keyboard(
    keyboardItems: List<List<KeyboardItem>>,
    onLetterClicked: (Char) -> Unit,
    onEnterClicked: () -> Unit,
    onBackspaceClicked: () -> Unit
) {
    val rowPaddingValue = 4
    val itemWidth = (LocalConfiguration.current.screenWidthDp - 16 - (rowPaddingValue * 9)) / 10
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 20.dp, start = 8.dp, end = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (items in keyboardItems) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(rowPaddingValue.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (item in items) {
                        when (item) {
                            KeyboardItem.Backspace -> Backspace(
                                onBackspaceClicked,
                                modifier = Modifier.size((itemWidth * 1.5).dp, 50.dp)
                            )

                            KeyboardItem.Enter -> Enter(
                                onEnterClicked,
                                modifier = Modifier.size((itemWidth * 1.5).dp, 50.dp)
                            )

                            is KeyboardItem.Letter -> Letter(
                                item.letter,
                                item.matchType.color,
                                onLetterClicked,
                                modifier = Modifier.size(itemWidth.dp, 50.dp)
                            )

                            KeyboardItem.Empty -> Empty(
                                modifier = Modifier.size(itemWidth.dp,50.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Letter(
    letter: Char,
    statusColor: Color?,
    onClicked: (Char) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .customBackground(statusColor)
            .clickable { onClicked(letter) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = letter.toString(), fontSize = 16.sp)
    }
}

@Composable
private fun Enter(onClicked: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .customBackground()
            .clickable { onClicked() },
        contentAlignment = Alignment.Center
    ) {
        Text("Enter", fontSize = 16.sp)
    }
}

@Composable
private fun Backspace(onClicked: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .customBackground()
            .clickable { onClicked() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.backspace_solid),
            contentDescription = "Backspace",
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
private fun Empty(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
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
private fun Keyboard_Preview() {
    Box(modifier = Modifier.width(800.dp)) {
        Keyboard(keyboardItems = InGameUiState.init().keyboardItems, {}, { }, {})
    }
}
