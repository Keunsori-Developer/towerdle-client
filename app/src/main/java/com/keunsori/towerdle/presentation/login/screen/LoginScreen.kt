package com.keunsori.towerdle.presentation.login.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.keunsori.towerdle.presentation.login.LoginViewModel
import com.keunsori.towerdle.utils.Navigation

@Composable
fun LoginScreen(viewModel: LoginViewModel, navHostController: NavHostController) {
    val test = viewModel.test.collectAsState().value
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = test)

        Button(onClick = { viewModel.setTestDataSource("Test1") }) {
            Text(text = "TEST1")
        }
        Button(onClick = { viewModel.setTestDataSource("Test2") }) {
            Text(text = "TEST2")
        }
        Button(onClick = { navHostController.navigate(Navigation.Main.route) }) {
            Text(text = "NEXT")
        }
    }
}