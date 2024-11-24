package com.keunsori.presentation.ui

import android.annotation.SuppressLint
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.keunsori.presentation.R
import com.keunsori.presentation.intent.LoginEvent
import com.keunsori.presentation.viewmodel.LoginViewModel
import com.keunsori.presentation.utils.LocalCredentialManagerController
import com.keunsori.presentation.utils.googleLogin
import kotlinx.coroutines.launch

@SuppressLint("HardwareIds")
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.uiState.collectAsState().value
    val credentialManager = LocalCredentialManagerController.current.credentialManager

    val ssaid = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    val isGoogleLoggedIn =
        viewModel.getIsGoogleLoggedIn().collectAsState(initial = null).value

    LaunchedEffect(key1 = isGoogleLoggedIn) {
        if(isGoogleLoggedIn != null){
            if(isGoogleLoggedIn){
                viewModel.sendEvent(LoginEvent.AutoGoogleLogin)
            } else {
                viewModel.sendEvent(LoginEvent.GuestLogin(ssaid))
            }
        }
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("임시 화면")
        /*Button(onClick = {
            coroutineScope.launch {
                googleLogin(
                    credentialManager = credentialManager!!,
                    context = context,
                    onSuccess = {
                        viewModel.sendEvent(LoginEvent.GoogleLogin(idToken = it))
                    })
            }
        }) {
            Text(text = stringResource(id = R.string.google_login))
        }*/

    }


}
