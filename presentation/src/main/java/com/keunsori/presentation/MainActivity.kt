package com.keunsori.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keunsori.presentation.ui.main.MainEffect
import com.keunsori.presentation.ui.login.LoginEffect
import com.keunsori.presentation.ui.login.LoginViewModel
import com.keunsori.presentation.ui.login.screen.LoginScreen
import com.keunsori.presentation.ui.main.MainViewModel
import com.keunsori.presentation.ui.main.screen.GameScreen
import com.keunsori.presentation.ui.main.screen.InfoScreen
import com.keunsori.presentation.ui.main.screen.MainScreen
import com.keunsori.presentation.ui.theme.TowerdleTheme
import com.keunsori.presentation.utils.LocalCredentialManagerController
import com.keunsori.presentation.utils.MyCredentialManagerController
import com.keunsori.presentation.utils.Navigation
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
    onFinish: () -> Unit,
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
                    when(effect.route){
                        Navigation.Login.route -> { // 로그인 페이지로 돌아갈 시 스택을 남기지 않음
                            navHostController.navigate(effect.route){
                                popUpTo(effect.route) {
                                    inclusive = true
                                }
                            }
                        }
                        else -> {
                            navHostController.navigate(effect.route)
                        }
                    }
                }
            }
        }
    }

    CompositionLocalProvider(
        LocalCredentialManagerController provides MyCredentialManagerController(
            credentialManager
        )
    ) {
        NavHost(
            navController = navHostController,
            startDestination = Navigation.Login.route
        ) {
            composable(route = Navigation.Login.route) {
                LoginScreen(
                    viewModel = loginViewModel,
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
                )
            }
        }
    }
}