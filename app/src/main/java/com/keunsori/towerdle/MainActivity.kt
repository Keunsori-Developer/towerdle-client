package com.keunsori.towerdle

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keunsori.towerdle.ui.login.LoginEffect
import com.keunsori.towerdle.ui.login.LoginViewModel
import com.keunsori.towerdle.ui.login.screen.LoginScreen
import com.keunsori.towerdle.ui.main.MainEffect
import com.keunsori.towerdle.ui.main.MainViewModel
import com.keunsori.towerdle.ui.main.screen.GameScreen
import com.keunsori.towerdle.ui.main.screen.InfoScreen
import com.keunsori.towerdle.ui.main.screen.MainScreen
import com.keunsori.towerdle.ui.theme.TowerdleTheme
import com.keunsori.towerdle.utils.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val credentialManager = CredentialManager.create(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TowerdleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(
                        viewModel = viewModel,
                        loginViewModel = loginViewModel,
                        credentialManager = credentialManager,
                        onFinish = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Navigation(
    viewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    credentialManager: CredentialManager,
    onFinish: () -> Unit
) {
    val navHostController = rememberNavController()
    val context = LocalContext.current

    // Login side effect 처리
    LaunchedEffect(key1 = Unit) {
        loginViewModel.effectFlow.collect { effect ->
            when (effect) {
                is LoginEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                is LoginEffect.MoveToMain -> {
                    navHostController.navigate(Navigation.Main.route)
                }
            }
        }
    }

    // Main side effect 처리
    LaunchedEffect(key1 = Unit) {
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is MainEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                is MainEffect.MoveScreen -> {
                    navHostController.navigate(effect.route)
                }
            }
        }
    }

    NavHost(
        navController = navHostController,
        startDestination = Navigation.Login.route
    ) {
        composable(route = Navigation.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                credentialManager = credentialManager
            )
        }

        composable(route = Navigation.Main.route) {
            MainScreen(viewModel = viewModel, onFinish = onFinish)
        }

        composable(route = Navigation.Game.route) {
            GameScreen(viewModel = viewModel)
        }

        composable(route = Navigation.Info.route) {
            InfoScreen(
                viewModel = viewModel,
                credentialManager = credentialManager
            )
        }
    }
}