package com.keunsori.presentation.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.credentials.ClearCredentialStateRequest
import com.keunsori.presentation.R
import com.keunsori.presentation.intent.LoginEffect
import com.keunsori.presentation.intent.LoginEvent
import com.keunsori.presentation.ui.util.TopBar
import com.keunsori.presentation.utils.LocalCredentialManagerController
import com.keunsori.presentation.utils.googleLogin
import com.keunsori.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {
        TopBar(
            stringResource(id = R.string.my_info),
            onBackButtonClicked = { loginViewModel.sendEffect(LoginEffect.MoveToMain) })

        if (uiState.isGoogleLogin) {
            LoginStateScreen(buttonTitle = "로그아웃", onClick = {
                loginViewModel.logout {
                    credentialManager!!.clearCredentialState(ClearCredentialStateRequest())
                }
            }) {
                Text(text = "구글 로그인 완료", fontSize = 20.sp)

                Column(
                    Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.5.dp, // 너비 5dp
                            color = Color.Black, // 색상 파란색
                            shape = RoundedCornerShape(10.dp) // 네모 모양
                        ).padding(10.dp)
                ) {
                    Text(uiState.email)
                    Text(uiState.name)
                }
            }

        } else {
            LoginStateScreen(buttonTitle = "구글 로그인", onClick = {
                coroutineScope.launch {
                    googleLogin(
                        credentialManager = credentialManager!!,
                        context = context,
                        onSuccess = {
                            loginViewModel.sendEvent(LoginEvent.GoogleLogin(idToken = it))
                        })
                }
            }) {
                Text(text = "게스트 계정 상태", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "계정 연동을 통해\n게임 데이터를 유지해주세요!")
            }
        }
    }

    if (uiState.loading) {
        Dialog(onDismissRequest = {}) {
            CircularProgressIndicator() // 로딩 다이얼로그
        }
    }
}

@Composable
fun LoginStateScreen(buttonTitle: String, onClick: () -> Unit, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
    ) {
        content.invoke()
    }
    Button(onClick = {
        onClick.invoke()
    }) {
        Text(text = buttonTitle)
    }
}