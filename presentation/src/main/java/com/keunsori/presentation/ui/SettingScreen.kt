package com.keunsori.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.credentials.ClearCredentialStateRequest
import com.keunsori.presentation.R
import com.keunsori.presentation.intent.LoginEffect
import com.keunsori.presentation.intent.LoginEvent
import com.keunsori.presentation.ui.widget.MenuBar
import com.keunsori.presentation.viewmodel.MainViewModel
import com.keunsori.presentation.utils.LocalCredentialManagerController
import com.keunsori.presentation.utils.googleLogin
import com.keunsori.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.launch
import kotlin.math.log

@Composable
fun SettingScreen(
    loginViewModel: LoginViewModel,
) {
    val credentialManager = LocalCredentialManagerController.current.credentialManager

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val uiState = loginViewModel.uiState.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MenuBar(title = R.string.my_info) {
            loginViewModel.sendEffect(LoginEffect.MoveToMain)
        }
        if (uiState.isGoogleLogin) {
            Column(modifier = Modifier.padding(15.dp).fillMaxWidth()) {
                Text(text = "구글 로그인 완료", fontSize = 20.sp)
            }
            Button(onClick = {
                loginViewModel.logout {
                    credentialManager!!.clearCredentialState(ClearCredentialStateRequest())
                }
            }) {
                Text(text = "로그아웃")
            }
        } else {
            Column(modifier = Modifier.padding(15.dp).fillMaxWidth()) {
                Text(text = "게스트 계정 상태", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "계정 연동을 통해\n게임 데이터를 유지해주세요!")
            }
            Button(onClick = {
                coroutineScope.launch {
                    googleLogin(
                        credentialManager = credentialManager!!,
                        context = context,
                        onSuccess = {
                            loginViewModel.sendEvent(LoginEvent.GoogleLogin(idToken = it))
                        })
                }
            }) {
                Text(text = "로그인")
            }
        }
    }

    if (uiState.loading) {
        Dialog(onDismissRequest = {}) {
            CircularProgressIndicator() // 로딩 다이얼로그
        }
    }
}