package com.keunsori.presentation.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.domain.usecase.UserUseCase
import com.keunsori.presentation.R
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
        sendEffect(LoginEffect.ShowToast(message))
    }

    private fun tryAutoLogin() {
        viewModelScope.launch {
            if (userUseCase.verifyRefreshToken()) { // refresh token 검증 -> 토큰이 있는지 + 유효한지 -> true 반환 시 메인 화면 이동 및 access token 저장
                sendEvent(LoginEvent.StartLoading)
                delay(1000)
                sendEvent(LoginEvent.FinishLoading)
                moveToMain()
            } else {
                // TODO: refresh token 만료
            }
        }
    }

    fun tryLogin(googleIdToken: String) {
        viewModelScope.launch {
            sendEvent(LoginEvent.StartLoading)
            Log.d("LoginViewModel", "tryLogin")

            val res =
                userUseCase.tryLogin(googleIdToken) // 로그인 API -> googleIdToken으로 refresh, access token을 받아옴
            delay(1000)
            sendEvent(LoginEvent.FinishLoading)
            if (res) { // 로그인 성공
                moveToMain() // 메인 화면 이동
            } else { // 로그인 실패
                sendEffect(LoginEffect.ShowToast(R.string.cant_login))
            }

        }
    }

    fun guestLogin() {
        moveToMain()
    }
}