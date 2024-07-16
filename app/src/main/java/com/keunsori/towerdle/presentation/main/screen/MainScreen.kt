package com.keunsori.towerdle.presentation.main.screen

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.keunsori.towerdle.presentation.login.LoginEffect
import com.keunsori.towerdle.utils.Navigation
import com.keunsori.towerdle.presentation.main.MainEffect
import com.keunsori.towerdle.presentation.main.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainViewModel, onFinish: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { viewModel.moveToScreen(Navigation.Game.route) }) {
            Text(text = "게임시작")
        }
        Button(onClick = {  }) {
            Text(text = "챌린지")
        }
        Button(onClick = { viewModel.moveToScreen(Navigation.Info.route) }) {
            Text(text = "내 정보")
        }
    }

    var backHandlingEnabled by remember { mutableStateOf(true) }

    BackHandler {
        Toast.makeText(
            context,
            "뒤로가기를 한번 더 누르면 종료합니다.",
            Toast.LENGTH_SHORT
        ).show()
        if (!backHandlingEnabled) {
            onFinish.invoke()
        }
        backHandlingEnabled = false
        coroutineScope.launch {
            delay(2000) // 2초안에 다시 누를 경우 앱 종료
            backHandlingEnabled = true
        }
    }
}