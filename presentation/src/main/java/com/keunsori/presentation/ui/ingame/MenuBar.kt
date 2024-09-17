package com.keunsori.presentation.ui.ingame

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keunsori.presentation.R

@Composable
fun MenuBar(onBackClicked: () -> Unit, onHelpButtonClicked: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)) {
        IconButton(onClick = onBackClicked) {
            Icon(painterResource(id = R.drawable.icon_arrow_back), contentDescription = "back")
        }
        Box(modifier = Modifier.weight(1f))
        IconButton(onClick =onHelpButtonClicked) {
            Icon(painterResource(id = R.drawable.icon_help), contentDescription = "help")
        }
    }
}


@Preview
@Composable
fun MenuBarPreview() {
    Box(modifier = Modifier.width(360.dp)) {
        MenuBar({}, {})
    }
}
