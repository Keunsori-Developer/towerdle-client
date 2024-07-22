package com.keunsori.towerdle.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.towerdle.data.repository.MainRepository
import com.keunsori.towerdle.presentation.main.intent.MainEffect
import com.keunsori.towerdle.presentation.main.intent.MainEvent
import com.keunsori.towerdle.presentation.main.intent.MainReducer
import com.keunsori.towerdle.presentation.main.intent.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {
    private val reducer = MainReducer(MainState.init())

    val uiState get() = reducer.uiState

    private val effectChannel = Channel<MainEffect>(Channel.BUFFERED)
    val effectFlow = effectChannel.receiveAsFlow()

    private fun sendEvent(event: MainEvent) {
        reducer.sendEvent(event)
    }

    fun increase(){
        sendEvent(MainEvent.Increase)
    }

    fun decrease(){
        sendEvent(MainEvent.Decrease)
    }

    fun showToastMessage(message: String) {
        viewModelScope.launch {
            effectChannel.send( MainEffect.ShowToast(message) )
        }
    }
}
