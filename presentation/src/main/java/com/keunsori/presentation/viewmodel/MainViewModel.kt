package com.keunsori.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.domain.usecase.UserUseCase
import com.keunsori.presentation.intent.MainEffect
import com.keunsori.presentation.intent.MainEvent
import com.keunsori.presentation.intent.MainReducer
import com.keunsori.presentation.intent.MainState
import com.keunsori.presentation.utils.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {
    // ui 상태
    private val reducer = MainReducer(MainState.init())
    val uiState get() = reducer.uiState
    // side effect 채널
    private val effectChannel = Channel<MainEffect>(Channel.BUFFERED)
    val effectFlow = effectChannel.receiveAsFlow()

    // UI 변경
    private suspend fun sendEvent(event: MainEvent) {
        reducer.sendEvent(event)
    }

    // Side Effect 처리
    private fun sendEffect(effect: MainEffect) {
        viewModelScope.launch {
            effectChannel.send(effect)
        }
    }

    // Screen 이동
    fun moveToScreen(route: String){
        sendEffect(MainEffect.MoveScreen(route))
    }

    fun showToast(message: Int){
        sendEffect(MainEffect.ShowToast(message))
    }

    fun logout(googleLogout: suspend () -> Unit) {
        viewModelScope.launch {
            // 로그아웃 (토큰 제거)
            userUseCase.logout()
            // 로그인 화면으로 이동
            moveToScreen(Navigation.Login.route)
            // 구글 계정 앱 로그아웃
            googleLogout.invoke()
        }
    }
}