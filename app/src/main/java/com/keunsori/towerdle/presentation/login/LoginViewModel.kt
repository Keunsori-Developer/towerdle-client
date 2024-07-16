package com.keunsori.towerdle.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.towerdle.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
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

    private fun tryAutoLogin() {
        viewModelScope.launch {
            val refreshToken = userRepository.getRefreshToken()
            if(refreshToken.isNotEmpty()){
                sendEvent(LoginEvent.StartLoading)
                delay(1000)
                sendEvent(LoginEvent.FinishLoading)
                if(userRepository.verifyAccessToken(refreshToken)){
                    sendEffect(LoginEffect.MoveToMain)
                } else {
                    // TODO: refresh token 만료
                }

            }
        }
    }

    fun tryLogin(googleIdToken: String) {
        viewModelScope.launch {
            sendEvent(LoginEvent.StartLoading)
            val res = userRepository.tryLogin(googleIdToken)
            delay(1000)
            sendEvent(LoginEvent.FinishLoading)
            if(res){ // 로그인 성공
                sendEffect(LoginEffect.MoveToMain)
            } else {
                // 로그인 실패
            }
        }
    }

    // UI 변경
    private fun sendEvent(event: LoginEvent) {
        reducer.sendEvent(event)
    }

    // Side Effect 처리
    private fun sendEffect(effect: LoginEffect){
        viewModelScope.launch {
            effectChannel.send(effect)
        }
    }
}