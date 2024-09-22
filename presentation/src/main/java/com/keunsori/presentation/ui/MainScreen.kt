package com.keunsori.presentation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.keunsori.presentation.R
import com.keunsori.presentation.viewmodel.MainViewModel
import com.keunsori.presentation.utils.Navigation
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
            Text(text = stringResource(id = R.string.game_start))
        }
        Button(onClick = {  }) {
            Text(text = stringResource(id = R.string.challenge))
        }
        Button(onClick = { viewModel.moveToScreen(Navigation.Info.route) }) {
            Text(text = stringResource(id = R.string.my_info))
        }
    }

    var backHandlingEnabled by remember { mutableStateOf(true) }

    BackHandler {
        viewModel.showToast(R.string.double_tab_back)
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