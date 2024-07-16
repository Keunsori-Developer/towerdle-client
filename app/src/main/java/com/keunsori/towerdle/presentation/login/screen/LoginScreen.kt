package com.keunsori.towerdle.presentation.login.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.credentials.CredentialManager
import androidx.navigation.NavHostController
import com.keunsori.towerdle.presentation.login.LoginEffect
import com.keunsori.towerdle.presentation.login.LoginViewModel
import com.keunsori.towerdle.presentation.main.MainEffect
import com.keunsori.towerdle.utils.googleLogin
import com.keunsori.towerdle.utils.Navigation
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: LoginViewModel, credentialManager: CredentialManager) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.uiState.collectAsState().value

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            coroutineScope.launch {
                googleLogin(credentialManager = credentialManager, context = context, onSuccess = {
                    viewModel.tryLogin(it)
                })
            }
        }) {
            Text(text = "Login")
        }
        Button(onClick = {

        }) {
            Text(text = "Guest")
        }
    }


    if(state.loading){
        Dialog(onDismissRequest = {}) {
            CircularProgressIndicator() // 로딩 다이얼로그
        }
    }
}
