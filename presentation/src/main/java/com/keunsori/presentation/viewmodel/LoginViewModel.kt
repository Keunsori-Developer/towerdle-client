package com.keunsori.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.domain.entity.ApiResult
import com.keunsori.domain.usecase.UserUseCase
import com.keunsori.presentation.intent.LoginEffect
import com.keunsori.presentation.intent.LoginEvent
import com.keunsori.presentation.intent.LoginReducer
import com.keunsori.presentation.intent.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
) : ViewModel() {
    // ui 상태
    private val reducer = LoginReducer(LoginState.init())
    val uiState get() = reducer.uiState

    // side effect 채널
    private val effectChannel = Channel<LoginEffect>(Channel.BUFFERED)
    val effectFlow = effectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            userUseCase.invoke()
        }
    }

    // UI 변경
    fun sendEvent(event: LoginEvent) {
        viewModelScope.launch {
            when (event) {
                LoginEvent.AutoGoogleLogin -> {
                    autoGoogleLogin()
                }

                is LoginEvent.GoogleLogin -> {
                    tryGoogleLogin(googleIdToken = event.idToken)
                }

                is LoginEvent.GuestLogin -> {
                    tryGuestLogin(guestId = event.ssaid)
                }

                else -> {
                    reducer.sendEvent(event)
                }
            }
        }
    }

    // Side Effect 처리
    fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch {
            effectChannel.send(effect)
        }
    }

    private fun autoGoogleLogin() {
        viewModelScope.launch {
            sendEvent(LoginEvent.StartLoading)
            Log.d("LoginViewModel", "tryGoogleLogin")
            val res =
                userUseCase.autoGoogleLogin() // 로그인 API -> googleIdToken으로 refresh, access token을 받아옴
            sendEvent(LoginEvent.FinishLoading)
            when (res) {
                is ApiResult.Success -> {
                    sendEvent(
                        LoginEvent.SuccessGoogleLogin(
                            email = res.data.user.email,
                            name = res.data.user.name
                        )
                    )
                    sendEffect(LoginEffect.MoveToMain) // 메인 화면 이동
                }

                is ApiResult.Fail -> {
                    // api errorBody의 message를 그대로 반환하므로 앱 내의 string resource를 활용하는 쪽으로 변경 고려
                    sendEffect(LoginEffect.ShowToast(res.exception.message.toString()))
                }
            }
        }
    }

    private fun tryGoogleLogin(googleIdToken: String) {
        viewModelScope.launch {
            sendEvent(LoginEvent.StartLoading)
            Log.d("LoginViewModel", "tryGoogleLogin")
            val res =
                userUseCase.tryGoogleLogin(googleIdToken) // 로그인 API -> googleIdToken으로 refresh, access token을 받아옴
            sendEvent(LoginEvent.FinishLoading)
            when (res) {
                is ApiResult.Success -> {
                    sendEvent(
                        LoginEvent.SuccessGoogleLogin(
                            email = res.data.user.email,
                            name = res.data.user.name
                        )
                    )
                }

                is ApiResult.Fail -> {
                    // api errorBody의 message를 그대로 반환하므로 앱 내의 string resource를 활용하는 쪽으로 변경 고려
                    sendEffect(LoginEffect.ShowToast(res.exception.message.toString()))
                }
            }
        }
    }

    private fun tryGuestLogin(guestId: String) {
        viewModelScope.launch {
            sendEvent(LoginEvent.StartLoading)
            Log.d("LoginViewModel", "tryGuestLogin")
            val res =
                userUseCase.tryGuestLogin() // 로그인 API -> googleIdToken으로 refresh, access token을 받아옴
            sendEvent(LoginEvent.FinishLoading)
            when (res) {
                is ApiResult.Success -> {
                    sendEffect(LoginEffect.MoveToMain) // 메인 화면 이동
                }

                is ApiResult.Fail -> {
                    // api errorBody의 message를 그대로 반환하므로 앱 내의 string resource를 활용하는 쪽으로 변경 고려
                    sendEffect(LoginEffect.ShowToast(res.exception.message.toString()))
                }
            }
        }
    }


    fun getIsGoogleLoggedIn(): Flow<Boolean?> {
        return userUseCase.getIsGoogleLoggedIn()
    }

    fun logout(googleLogout: suspend () -> Unit) {
        viewModelScope.launch {
            // 로그아웃 (토큰 제거)
            sendEvent(LoginEvent.StartLoading)
            userUseCase.logout()
            sendEvent(LoginEvent.FinishLoading)
            sendEvent(LoginEvent.Logout)
            // 구글 계정 앱 로그아웃
            googleLogout.invoke()
        }
    }
}