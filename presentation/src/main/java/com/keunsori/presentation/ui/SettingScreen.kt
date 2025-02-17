package com.keunsori.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.credentials.ClearCredentialStateRequest
import com.keunsori.domain.entity.QuizLevel
import com.keunsori.domain.entity.UserInfo
import com.keunsori.presentation.R
import com.keunsori.presentation.intent.LoginEffect
import com.keunsori.presentation.intent.LoginEvent
import com.keunsori.presentation.intent.LoginState
import com.keunsori.presentation.ui.util.TopBar
import com.keunsori.presentation.utils.LocalCredentialManagerController
import com.keunsori.presentation.utils.googleLogin
import com.keunsori.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    loginViewModel: LoginViewModel,
) {
    LaunchedEffect(Unit) {
        loginViewModel.sendEvent(LoginEvent.GetUserInfo)
    }

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
            LoginStateScreen(
                title = "구글 로그인 완료",
                buttonTitle = "로그아웃",
                uiState = uiState,
                onClick = {
                    loginViewModel.logout {
                        credentialManager!!.clearCredentialState(ClearCredentialStateRequest())
                    }
                }, onClickDetailButton = {
                    loginViewModel.sendEvent(LoginEvent.SetDetailDialogState(true))
                }
            ) {
                Text("계정: " + uiState.email)
                Text("이름: " + uiState.name)
            }
        } else {
            LoginStateScreen(
                title = "게스트 계정 상태",
                buttonTitle = "구글 로그인",
                uiState = uiState,
                onClick = {
                    coroutineScope.launch {
                        googleLogin(
                            credentialManager = credentialManager!!,
                            context = context,
                            onSuccess = {
                                loginViewModel.sendEvent(LoginEvent.GoogleLogin(idToken = it))
                            })
                    }
                }, onClickDetailButton = {
                    loginViewModel.sendEvent(LoginEvent.SetDetailDialogState(true))
                }) {
                Text("이름: " + uiState.name, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "계정 연동을 통해\n게임 데이터를 유지해주세요!", fontSize = 15.sp)
            }
        }
    }


    if (uiState.loading) {
        Dialog(onDismissRequest = {}) {
            CircularProgressIndicator() // 로딩 다이얼로그
        }
    }

    if (uiState.detailDialogState) {
        Dialog(
            onDismissRequest = {
                loginViewModel.sendEvent(LoginEvent.SetDetailDialogState(false))
            }, properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            )
        ) {
            Column(
                modifier = Modifier
                    .width(320.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        RoundedCornerShape(20.dp)
                    )
                    .border(
                        1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(bottom = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {
                        loginViewModel.sendEvent(LoginEvent.ChangeDetailState(isForward = false))
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            uiState.quizLevelDetailState.title,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    IconButton(onClick = {
                        loginViewModel.sendEvent(LoginEvent.ChangeDetailState(isForward = true))
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
                DetailedStats(
                    quizLevel = uiState.quizLevelDetailState,
                    detailedStats = uiState.userInfo.detailedStats[uiState.quizLevelDetailState]
                        ?: UserInfo.DetailedStats()
                )
            }
        }
    }
}

@Composable
fun LoginStateScreen(
    title: String,
    buttonTitle: String,
    onClick: () -> Unit,
    onClickDetailButton: () -> Unit,
    uiState: LoginState,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, fontSize = 20.sp, modifier = Modifier.fillMaxWidth())

        Column(
            Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
                .border(
                    width = 1.5.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp),
        ) {
            content.invoke()
            Text("통계", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 15.dp))
            Text("맞춘 문제 수: " + uiState.userInfo.solveCount.toString(), fontSize = 15.sp)
            Text("마지막 푼 날짜: " + uiState.userInfo.lastSolve, fontSize = 15.sp)

            ElevatedButton(onClick = {
                onClickDetailButton.invoke()
            }) {
                Text(text = "자세히")
            }
        }

        ElevatedButton(onClick = {
            onClick.invoke()
        }) {
            Text(text = buttonTitle)
        }
    }

}

@Composable
fun DetailedStats(quizLevel: QuizLevel, detailedStats: UserInfo.DetailedStats) {
    Text("시도 횟수: " + detailedStats.totalCnt.toInt(), fontSize = 15.sp)
    Text("맞춘 문제 수: " + detailedStats.solvedCnt, fontSize = 15.sp)
    Text("연속 맞춘 문제 수: " + detailedStats.solveStreak, fontSize = 15.sp)
    Text("시도 횟수 별 정답 수")
    Text("1: " + detailedStats.solvedAttemptsStats[0] + "회")
    Text("2: " + detailedStats.solvedAttemptsStats[1] + "회")
    Text("3: " + detailedStats.solvedAttemptsStats[2] + "회")
    Text("4: " + detailedStats.solvedAttemptsStats[3] + "회")
    Text("5: " + detailedStats.solvedAttemptsStats[4] + "회")
    Text("6: " + detailedStats.solvedAttemptsStats[5] + "회")
    if (quizLevel == QuizLevel.EASY) {
        Text("7: " + detailedStats.solvedAttemptsStats[6] + "회")
    }

}