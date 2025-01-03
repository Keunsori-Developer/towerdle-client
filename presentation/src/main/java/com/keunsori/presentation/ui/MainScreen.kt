package com.keunsori.presentation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
        modifier = Modifier.padding(20.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.title),
            contentDescription = stringResource(id = R.string.game_start),
            modifier = Modifier.fillMaxWidth().height(147.dp))


        Image(
            painter = painterResource(id = R.drawable.start_button),
            contentDescription = stringResource(id = R.string.game_start),
            modifier = Modifier.width(194.dp).height(60.dp).clickable {
                viewModel.moveToScreen(Navigation.Main_ChooseLevel.route)
            })

        Image(
            painter = painterResource(id = R.drawable.challenge_button),
            contentDescription = stringResource(id = R.string.challenge),
            modifier = Modifier.width(194.dp).height(60.dp).clickable {

            })

        Image(
            painter = painterResource(id = R.drawable.setting_button),
            contentDescription = stringResource(id = R.string.my_info),
            modifier = Modifier.width(194.dp).height(60.dp).clickable {
                viewModel.moveToScreen(Navigation.Info.route)
            })

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.game_start),
            modifier = Modifier.fillMaxWidth().height(250.dp))


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
}