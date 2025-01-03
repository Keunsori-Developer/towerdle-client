package com.keunsori.presentation

import android.annotation.SuppressLint
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.keunsori.presentation.intent.LoginEffect
import com.keunsori.presentation.intent.MainEffect
import com.keunsori.presentation.ui.InGameScreen
import com.keunsori.presentation.ui.LoginScreen
import com.keunsori.presentation.ui.MainScreen
import com.keunsori.presentation.ui.SettingScreen
import com.keunsori.presentation.ui.main.ChooseLevelScreen
import com.keunsori.presentation.ui.theme.TowerdleTheme
import com.keunsori.presentation.utils.LocalCredentialManagerController
import com.keunsori.presentation.utils.MyCredentialManagerController
import com.keunsori.presentation.utils.Navigation
import com.keunsori.presentation.viewmodel.InGameViewModel
import com.keunsori.presentation.viewmodel.LoginViewModel
import com.keunsori.presentation.viewmodel.MainViewModel
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

@SuppressLint("HardwareIds")
@Composable
fun Navigation(
    viewModel: MainViewModel,
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
                    navHostController.navigate(effect.route) {
                        if (effect.popUp) popUpTo(effect.route) { inclusive = true }
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

            composable(route = Navigation.Main_ChooseLevel.route) {
                ChooseLevelScreen(navigateToHome = {
                    navHostController.popBackStack()
                }, navigateToInGame = { level ->
                    navHostController.navigate(
                        Navigation.Game.route.replace(
                            "{level}",
                            level.toString()
                        )
                    ) {
                        popUpTo(Navigation.Main_ChooseLevel.route) {
                            inclusive = true
                        }
                    }
                })
            }

            composable(
                route = Navigation.Game.route, arguments = listOf(
                    navArgument("level") {
                        type = NavType.IntType
                    },
                )
            ) { navBackStackEntry ->
                /* Extracting the level from the route */
                val level = navBackStackEntry.arguments?.getInt("level")
                val inGameViewModel = hiltViewModel<InGameViewModel>()
                InGameScreen(
                    inGameViewModel = inGameViewModel,
                    navigateToMain = {
                        navHostController.navigate(Navigation.Main.route) {
                            popUpTo(Navigation.Main.route) { inclusive = true }
                        }
                    })
            }

            composable(route = Navigation.Info.route) {
                SettingScreen(
                    loginViewModel = loginViewModel,
                )
            }
        }
    }
}
