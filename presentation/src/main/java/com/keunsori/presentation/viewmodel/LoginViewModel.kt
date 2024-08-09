package com.keunsori.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.domain.entity.ApiResult
import com.keunsori.domain.usecase.UserUseCase
import com.keunsori.presentation.R
import com.keunsori.presentation.intent.LoginEffect
import com.keunsori.presentation.intent.LoginEvent
import com.keunsori.presentation.intent.LoginReducer
import com.keunsori.presentation.intent.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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
        tryAutoLogin()
    }

    // UI 변경
    private fun sendEvent(event: LoginEvent) {
        reducer.sendEvent(event)
    }

    // Side Effect 처리
    private fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch {
            effectChannel.send(effect)
        }
    }

    fun moveToMain() {
        sendEffect(LoginEffect.MoveToMain) // 메인 화면 이동
    }

    fun showToast(message: Int) {
        sendEffect(LoginEffect.ShowToastToResource(message))
    }

    /**
     * 자동로그인
     * refresh token 검증 -> 토큰이 있는지 + 유효한지 -> true 반환 시 메인 화면 이동 및 access token 저장
     */
    private fun tryAutoLogin() {
        viewModelScope.launch {
            if (userUseCase.verifyRefreshToken()) {
                sendEvent(LoginEvent.StartLoading)
                delay(1000)
                sendEvent(LoginEvent.FinishLoading)
                moveToMain()
            }
        }
    }

    fun tryLogin(googleIdToken: String) {
        viewModelScope.launch {
            sendEvent(LoginEvent.StartLoading)
            Log.d("LoginViewModel", "tryLogin")
            val res =
                userUseCase.tryLogin(googleIdToken) // 로그인 API -> googleIdToken으로 refresh, access token을 받아옴
            sendEvent(LoginEvent.FinishLoading)
            when (res) {
                is ApiResult.Success -> {
                    moveToMain() // 메인 화면 이동
                }

                is ApiResult.Fail -> {
                    // TODO: api errorBody의 message를 그대로 반환하므로 서버에서 메시지를 사용자 친화적으로 수정하거나 앱 내의 string resource를 활용
                    sendEffect(LoginEffect.ShowToast(res.exception.message.toString()))
                }
            }
        }
    }

    fun guestLogin() {
        moveToMain()
    }
}