package com.keunsori.towerdle.presentation.main.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.keunsori.towerdle.utils.Navigation
import com.keunsori.towerdle.presentation.main.MainEffect
import com.keunsori.towerdle.presentation.main.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel, navHostController: NavHostController) {
    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is MainEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                MainEffect.NavigateToDetail -> {
                    navHostController.navigate(Navigation.Game.route)
                    // Navigate to detail screen
                }
            }
        }
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = state.data.toString())
        Text(text = state.boolean.toString())

        Button(onClick = { viewModel.showToastMessage("test") }) {
            Text(text = "Toast")
        }
        Button(onClick = { viewModel.increase() }) {
            Text(text = "Increase")
        }
        Button(onClick = { viewModel.decrease() }) {
            Text(text = "Decrease")
        }
    }
}