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
        // refresh token 검증 후 성공 시 자동 로그인
        viewModelScope.launch {
            userUseCase.invoke {
                sendEvent(LoginEvent.StartLoading)
                delay(1000)
                sendEvent(LoginEvent.FinishLoading)
                sendEffect(LoginEffect.MoveToMain) // 메인 화면 이동
            }
        }
    }

    // UI 변경
    fun sendEvent(event: LoginEvent) {
        viewModelScope.launch {
            when (event) {
                is LoginEvent.GoogleLogin -> {
                    tryGoogleLogin(googleIdToken = event.idToken)
                }

                LoginEvent.GuestLogin -> {
                    tryGuestLogin("")
                }

                else -> {
                    reducer.sendEvent(event)
                }
            }
        }
    }

    // Side Effect 처리
    private fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch {
            effectChannel.send(effect)
        }
    }

    private fun tryGoogleLogin(googleIdToken: String) {
        viewModelScope.launch {
            sendEvent(LoginEvent.StartLoading)
            Log.d("LoginViewModel", "tryLogin")
            val res =
                userUseCase.tryGoogleLogin(googleIdToken) // 로그인 API -> googleIdToken으로 refresh, access token을 받아옴
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

    private fun tryGuestLogin(guestId: String) {
        viewModelScope.launch {
            sendEvent(LoginEvent.StartLoading)
            Log.d("LoginViewModel", "tryLogin")
            val res =
                userUseCase.tryGuestLogin(guestId) // 로그인 API -> googleIdToken으로 refresh, access token을 받아옴
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
}