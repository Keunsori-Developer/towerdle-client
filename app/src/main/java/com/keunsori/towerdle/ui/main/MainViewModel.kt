package com.keunsori.towerdle.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.towerdle.data.repository.MainRepository
import com.keunsori.towerdle.data.repository.UserRepository
import com.keunsori.towerdle.utils.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val userRepository: UserRepository
): ViewModel() {
    // ui 상태
    private val reducer = MainReducer(MainState.init())
    val uiState get() = reducer.uiState
    // side effect 채널
    private val effectChannel = Channel<MainEffect>(Channel.BUFFERED)
    val effectFlow = effectChannel.receiveAsFlow()

    // UI 변경
    private fun sendEvent(event: MainEvent) {
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
            userRepository.logout()
            // 로그인 화면으로 이동
            moveToScreen(Navigation.Login.route)
            // 구글 계정 앱 로그아웃
            googleLogout.invoke()
        }
    }
}