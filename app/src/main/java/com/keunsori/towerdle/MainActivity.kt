package com.keunsori.towerdle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keunsori.towerdle.presentation.login.LoginViewModel
import com.keunsori.towerdle.presentation.login.screen.LoginScreen
import com.keunsori.towerdle.presentation.main.MainViewModel
import com.keunsori.towerdle.presentation.main.screen.GameScreen
import com.keunsori.towerdle.presentation.main.screen.MainScreen
import com.keunsori.towerdle.ui.theme.TowerdleTheme
import com.keunsori.towerdle.utils.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TowerdleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(viewModel = viewModel, loginViewModel = loginViewModel)
                }
            }
        }
    }
}

@Composable
fun Navigation(viewModel: MainViewModel, loginViewModel: LoginViewModel) {
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = Navigation.Login.route
    ) {
        composable(route = Navigation.Login.route) {
            LoginScreen(viewModel = loginViewModel, navHostController = navHostController)
        }

        composable(route = Navigation.Main.route) {
            MainScreen(viewModel = viewModel, navHostController = navHostController)
        }

        composable(route = Navigation.Game.route) {
            GameScreen(viewModel = viewModel, navHostController = navHostController)
        }
    }
}