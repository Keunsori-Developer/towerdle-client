package com.keunsori.presentation.ui.ingame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keunsori.presentation.model.KeyboardItem
import com.keunsori.presentation.model.KeyboardItemType

@Composable
fun Keyboard(keyboardItems: Array<Array<KeyboardItem>>, onLetterClicked: (Char) -> Unit) {
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
                        when (item.type) {
                            KeyboardItemType.Backspace -> Backspace {}
                            KeyboardItemType.Enter -> Enter {}
                            is KeyboardItemType.Letter -> Letter(item.type.letter, Color.Gray) {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Letter(letter:Char, statusColor: Color, onClicked: (Char) -> Unit) {
    Box(
        modifier = Modifier
            .size(20.dp, 30.dp)
            .background(statusColor, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = letter.toString())
    }
}

@Composable
private fun Enter(onClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp, 30.dp)
            .background(Color.Gray, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Enter")
    }
}

@Composable
private fun Backspace(onClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp, 30.dp)
            .background(Color.Gray, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Backspace")
    }
}

@Preview
@Composable
fun Keyboard_Preview() {

    val lastIndex = mutableListOf<KeyboardItem>()
    lastIndex.add(KeyboardItem(KeyboardItemType.Enter))
    lastIndex.addAll(
        charArrayOf(
            'ㅋ',
            'ㅌ',
            'ㅊ',
            'ㅍ',
            'ㅠ',
            'ㅜ',
            'ㅡ'
        ).map { KeyboardItem(KeyboardItemType.Letter(it)) }.toTypedArray()
    )
    lastIndex.add(KeyboardItem(KeyboardItemType.Backspace))
    val letters: Array<Array<KeyboardItem>> = arrayOf<Array<KeyboardItem>>(
        charArrayOf('ㅂ', 'ㅈ', 'ㄷ', 'ㄱ', 'ㅅ', 'ㅛ', 'ㅕ', 'ㅑ', 'ㅐ', 'ㅔ').map {
            KeyboardItem(
                KeyboardItemType.Letter(it)
            )
        }.toTypedArray(),
        charArrayOf(
            'ㅁ',
            'ㄴ',
            'ㅇ',
            'ㄹ',
            'ㅎ',
            'ㅗ',
            'ㅓ',
            'ㅏ',
            'ㅣ'
        ).map { KeyboardItem(KeyboardItemType.Letter(it)) }.toTypedArray(),
        lastIndex.toTypedArray()
    )
    Keyboard(keyboardItems = letters) {

    }
}