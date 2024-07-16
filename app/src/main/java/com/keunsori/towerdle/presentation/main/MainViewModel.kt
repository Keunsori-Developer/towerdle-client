package com.keunsori.towerdle.presentation.main

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

    fun logout(googleLogout: suspend () -> Unit) {
        viewModelScope.launch {
            userRepository.logout()
            moveToScreen(Navigation.Login.route)
            googleLogout.invoke()
        }
    }
}