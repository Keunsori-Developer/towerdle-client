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
import com.keunsori.presentation.intent.MainEffect
import com.keunsori.presentation.intent.LoginEffect
import com.keunsori.presentation.viewmodel.LoginViewModel
import com.keunsori.presentation.ui.LoginScreen
import com.keunsori.presentation.viewmodel.MainViewModel
import com.keunsori.presentation.ui.InGameScreen
import com.keunsori.presentation.ui.InfoScreen
import com.keunsori.presentation.ui.MainScreen
import com.keunsori.presentation.ui.theme.TowerdleTheme
import com.keunsori.presentation.utils.LocalCredentialManagerController
import com.keunsori.presentation.utils.MyCredentialManagerController
import com.keunsori.presentation.utils.Navigation
import com.keunsori.presentation.viewmodel.InGameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val inGameViewModel: InGameViewModel by viewModels()
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
                        inGameViewModel = inGameViewModel,
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
    inGameViewModel: InGameViewModel,
    loginViewModel: LoginViewModel,
    credentialManager: CredentialManager,
    onFinish: () -> Unit,
) {
    val navHostController = rememberNavController()
    val context = LocalContext.current

    // GoogleLogin side effect 처리
    LaunchedEffect(key1 = Unit) {
        loginViewModel.effectFlow.collect { effect ->
            when (effect) {
                is LoginEffect.ShowToastToResource -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

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
                    when (effect.route) {
                        Navigation.Login.route -> { // 로그인 페이지로 돌아갈 시 스택을 남기지 않음
                            navHostController.navigate(effect.route) {
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
            startDestination = Navigation.Game.route
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
                InGameScreen(inGameViewModel = inGameViewModel)
            }

            composable(route = Navigation.Info.route) {
                InfoScreen(
                    viewModel = viewModel,
                )
            }
        }
    }
}
