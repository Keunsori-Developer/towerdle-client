package com.keunsori.presentation.ui

import android.annotation.SuppressLint
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    // 로그인
    LaunchedEffect(key1 = isGoogleLoggedIn) {
        if (isGoogleLoggedIn != null) {
            if (isGoogleLoggedIn) {
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
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.game_start),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(10.dp)
        )

        Text("접속중", fontSize = 24.sp)

        CircularProgressIndicator(modifier = Modifier.padding(10.dp))
    }
}
